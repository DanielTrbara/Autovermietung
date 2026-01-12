package org.example.autovermietung.Controller;

import jakarta.persistence.EntityManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Model.Benutzer;
import org.example.autovermietung.Model.Earning;
import org.example.autovermietung.Model.Rental;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.hibernate.exception.GenericJDBCException;

public class RentalController {

    // Auswahl
    @FXML private ComboBox<Benutzer> cbKunde;
    @FXML private ComboBox<AddCar> cbAuto;
    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;

    // Tabelle Rentals
    @FXML private TableView<Rental> tableRentals;
    @FXML private TableColumn<Rental, String> colKunde;
    @FXML private TableColumn<Rental, String> colAuto;
    @FXML private TableColumn<Rental, String> colStart;
    @FXML private TableColumn<Rental, String> colEnd;

    private final ObservableList<Benutzer> kunden = FXCollections.observableArrayList();
    private final ObservableList<AddCar> autos = FXCollections.observableArrayList();
    private final ObservableList<Rental> rentals = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupComboBoxes();
        setupTable();

        loadKunden();
        loadAvailableAutos();
        loadRentals();

        cbKunde.setItems(kunden);
        cbAuto.setItems(autos);
        tableRentals.setItems(rentals);
    }

    private void setupComboBoxes() {
        cbKunde.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Benutzer b, boolean empty) {
                super.updateItem(b, empty);
                setText(empty || b == null ? "" : b.getVorname() + " " + b.getNachname());
            }
        });
        cbKunde.setButtonCell(cbKunde.getCellFactory().call(null));

        cbAuto.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AddCar a, boolean empty) {
                super.updateItem(a, empty);
                setText(empty || a == null ? "" : a.getBrand() + " " + a.getModel() + " (" + a.getYear() + ")");
            }
        });
        cbAuto.setButtonCell(cbAuto.getCellFactory().call(null));
    }

    private void setupTable() {
        colKunde.setCellValueFactory(cell -> {
            Rental r = cell.getValue();
            if (r == null || r.getKunde() == null) return new ReadOnlyStringWrapper("");
            return new ReadOnlyStringWrapper(r.getKunde().getVorname() + " " + r.getKunde().getNachname());
        });

        colAuto.setCellValueFactory(cell -> {
            Rental r = cell.getValue();
            if (r == null || r.getAuto() == null) return new ReadOnlyStringWrapper("");
            AddCar a = r.getAuto();
            return new ReadOnlyStringWrapper(a.getBrand() + " " + a.getModel() + " (" + a.getYear() + ")");
        });

        colStart.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue() == null || cell.getValue().getStartDate() == null
                        ? ""
                        : cell.getValue().getStartDate().toString()
        ));

        colEnd.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue() == null || cell.getValue().getEndDate() == null
                        ? ""
                        : cell.getValue().getEndDate().toString()
        ));
    }

    private void loadKunden() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            kunden.setAll(em.createQuery("select b from Benutzer b", Benutzer.class).getResultList());
        } finally {
            em.close();
        }
    }

    private void loadAvailableAutos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            autos.setAll(em.createQuery("select a from AddCar a where a.available = true", AddCar.class).getResultList());
        } finally {
            em.close();
        }
    }

    private void loadRentals() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            rentals.setAll(
                    em.createQuery("select r from Rental r order by r.rentalId desc", Rental.class)
                            .getResultList()
            );
        } catch (Exception ex) {
            // Workaround/Migration: some older rows may contain epoch-millis in DATE columns (SQLite)
            // which the JDBC driver cannot parse (e.g. "1768172400000").
            if (isDateParseProblem(ex)) {
                try {
                    em.getTransaction().begin();
                    migrateRentalDateColumnsToIsoDateTime(em);
                    em.getTransaction().commit();

                    // retry
                    rentals.setAll(
                            em.createQuery("select r from Rental r order by r.rentalId desc", Rental.class)
                                    .getResultList()
                    );
                } catch (Exception migrationEx) {
                    try { em.getTransaction().rollback(); } catch (Exception ignored) {}
                    migrationEx.printStackTrace();
                    throw migrationEx;
                }
            } else {
                throw ex;
            }
        } finally {
            em.close();
        }
    }

    private boolean isDateParseProblem(Exception ex) {
        // We check message chain because Hibernate wraps JDBC exceptions.
        Throwable t = ex;
        while (t != null) {
            String msg = t.getMessage();
            if (msg != null) {
                String m = msg.toLowerCase();
                if (m.contains("error parsing date") || m.contains("unparseable date")) {
                    return true;
                }
            }
            t = t.getCause();
        }
        return false;
    }

    private void migrateRentalDateColumnsToIsoDateTime(EntityManager em) {
        // Convert epoch-millis (stored as TEXT or INTEGER) into a parsable SQLite datetime string
        // format expected by the sqlite-jdbc parser: yyyy-MM-dd HH:mm:ss.SSS
        // We only touch values that look numeric.

        // start_date
        em.createNativeQuery(
                "UPDATE Rental " +
                        "SET start_date = (datetime(CAST(start_date AS INTEGER)/1000, 'unixepoch') || '.000') " +
                        "WHERE start_date IS NOT NULL AND (typeof(start_date)='integer' OR (start_date GLOB '[0-9]*' AND length(start_date) >= 10))"
        ).executeUpdate();

        // end_date
        em.createNativeQuery(
                "UPDATE Rental " +
                        "SET end_date = (datetime(CAST(end_date AS INTEGER)/1000, 'unixepoch') || '.000') " +
                        "WHERE end_date IS NOT NULL AND (typeof(end_date)='integer' OR (end_date GLOB '[0-9]*' AND length(end_date) >= 10))"
        ).executeUpdate();
    }

    @FXML
    private void onVermieten() {
        Benutzer kunde = cbKunde.getValue();
        AddCar auto = cbAuto.getValue();
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        if (kunde == null || auto == null || start == null || end == null) {
            new Alert(Alert.AlertType.ERROR, "Bitte Kunde, Auto, Start und Enddatum auswählen.").show();
            return;
        }
        if (end.isBefore(start)) {
            new Alert(Alert.AlertType.ERROR, "Enddatum darf nicht vor dem Startdatum liegen.").show();
            return;
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Benutzer managedKunde = em.find(Benutzer.class, kunde.getKundenId());
            AddCar managedAuto = em.find(AddCar.class, auto.getCarId());

            if (managedKunde == null || managedAuto == null) {
                em.getTransaction().rollback();
                new Alert(Alert.AlertType.ERROR, "Kunde/Auto nicht gefunden (DB).").show();
                return;
            }

            if (!managedAuto.isAvailable()) {
                em.getTransaction().rollback();
                new Alert(Alert.AlertType.ERROR, "Auto ist nicht verfügbar.").show();
                return;
            }

            Rental rental = new Rental(managedKunde, managedAuto, start, end);

            // WICHTIG: Preis pro Tag im Rental speichern (Snapshot zum Zeitpunkt der Vermietung)
            rental.setPricePerDay(managedAuto.getPricePerDay());

            em.persist(rental);

            em.flush();

            long rentalDays = ChronoUnit.DAYS.between(start, end) + 1;
            if (rentalDays < 1) {
                rentalDays = 1;
            }
            double totalEarning = rentalDays * managedAuto.getPricePerDay();
            Earning earning = new Earning(totalEarning, LocalDateTime.now());
            earning.setCarId(managedAuto.getCarId());
            earning.setRentalId(rental.getRentalId());
            earning.setType(Earning.TYPE_RENTAL_INCOME);
            em.persist(earning);

            managedAuto.setAvailable(false); // sperren
            em.merge(managedAuto);

            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            try { em.getTransaction().rollback(); } catch (Exception ignored) {}
            new Alert(Alert.AlertType.ERROR, "Fehler beim Vermieten: " + e.getMessage()).show();
            return;
        } finally {
            em.close();
        }

        // UI refresh
        loadAvailableAutos();
        loadRentals();
        cbAuto.getSelectionModel().clearSelection();
        dpStart.setValue(null);
        dpEnd.setValue(null);

        new Alert(Alert.AlertType.INFORMATION, "Vermietung erstellt ✅").show();
    }

    @FXML
    private void onRueckgabe() {
        Rental selected = tableRentals.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Rental managedRental = em.find(Rental.class, selected.getRentalId());
            if (managedRental == null) {
                em.getTransaction().rollback();
                return;
            }

            AddCar managedAuto = managedRental.getAuto();
            if (managedAuto != null) {
                managedAuto.setAvailable(true);
                em.merge(managedAuto);
            }

            em.remove(managedRental);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            try { em.getTransaction().rollback(); } catch (Exception ignored) {}
            new Alert(Alert.AlertType.ERROR, "Fehler bei Rückgabe: " + e.getMessage()).show();
        } finally {
            em.close();
        }

        loadAvailableAutos();
        loadRentals();
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/Dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
