package org.example.autovermietung.Controller;

import jakarta.persistence.EntityManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Model.Benutzer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BenutzerController {

    // ====== TABLE + SPALTEN (Anzeige) ======
    @FXML private TableView<Benutzer> tableBenutzer;

    @FXML private TableColumn<Benutzer, String> colVorname;
    @FXML private TableColumn<Benutzer, String> colNachname;
    @FXML private TableColumn<Benutzer, String> colGeburtsdatum;
    @FXML private TableColumn<Benutzer, String> colEmail;
    @FXML private TableColumn<Benutzer, String> colTelefon;
    @FXML private TableColumn<Benutzer, String> colAdresse;

    // ====== EINGABEFELDER ======
    @FXML private TextField txtVorname;
    @FXML private TextField txtNachname;
    @FXML private TextField txtGeburtsdatum; // YYYY-MM-DD oder YYYY.MM.DD
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtSuche;

    // ====== DATENHALTUNG ======
    private final ObservableList<Benutzer> benutzerListe = FXCollections.observableArrayList();
    private FilteredList<Benutzer> filtered;
    private SortedList<Benutzer> sorted;

    // Anzeigeformat in der Tabelle (einheitlich)
    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    // Eingabeformate (wir akzeptieren beides)
    private static final DateTimeFormatter INPUT_DASH_FMT = DateTimeFormatter.ISO_LOCAL_DATE;     // yyyy-MM-dd
    private static final DateTimeFormatter INPUT_DOT_FMT  = DateTimeFormatter.ofPattern("yyyy.MM.dd"); // yyyy.MM.dd

    @FXML
    private void initialize() {

        // ====== TABLE <-> MODEL VERKNÜPFUNG ======
        colVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        colNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        // Geburtsdatum in der Tabelle schön anzeigen (immer yyyy-MM-dd)
        colGeburtsdatum.setCellValueFactory(cell -> {
            LocalDate d = cell.getValue() == null ? null : cell.getValue().getBirthDate();
            return new ReadOnlyStringWrapper(d == null ? "" : d.format(DISPLAY_FMT));
        });

        // ====== FILTER + SORT SETUP ======
        filtered = new FilteredList<>(benutzerListe, b -> true);
        sorted = new SortedList<>(filtered);

        sorted.comparatorProperty().bind(tableBenutzer.comparatorProperty());
        tableBenutzer.setItems(sorted);

        // ====== SUCHFUNKTION ======
        txtSuche.textProperty().addListener((obs, oldV, q) -> {
            String s = q == null ? "" : q.toLowerCase().trim();

            filtered.setPredicate(b -> {
                if (s.isEmpty()) return true;

                return (b.getVorname() != null && b.getVorname().toLowerCase().contains(s))
                        || (b.getNachname() != null && b.getNachname().toLowerCase().contains(s))
                        || (b.getEmail() != null && b.getEmail().toLowerCase().contains(s))
                        || (b.getTelefon() != null && b.getTelefon().toLowerCase().contains(s));
            });
        });

        loadFromDb();
    }

    private void loadFromDb() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            benutzerListe.setAll(
                    em.createQuery("select b from Benutzer b", Benutzer.class)
                            .getResultList()
            );
        } finally {
            em.close();
        }
    }

    @FXML
    private void onNeu() {
        txtVorname.clear();
        txtNachname.clear();
        txtGeburtsdatum.clear();
        txtEmail.clear();
        txtTelefon.clear();
        txtAdresse.clear();
        tableBenutzer.getSelectionModel().clearSelection();
    }

    @FXML
    private void onHinzufuegen() {

        String birthDateText = txtGeburtsdatum.getText() == null ? "" : txtGeburtsdatum.getText().trim();
        if (birthDateText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Bitte Geburtsdatum eingeben (YYYY-MM-DD oder YYYY.MM.DD).").show();
            return;
        }

        LocalDate birthDate;
        try {
            birthDate = parseBirthDate(birthDateText);
        } catch (DateTimeParseException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Geburtsdatum ungültig.\nErlaubt: YYYY-MM-DD (z.B. 2005-08-31) oder YYYY.MM.DD (z.B. 2005.08.31)."
            ).show();
            return;
        }

        Benutzer b = new Benutzer(
                txtVorname.getText(),
                txtNachname.getText(),
                birthDate,
                txtEmail.getText(),
                txtTelefon.getText(),
                txtAdresse.getText()
        );

        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(b);
        em.getTransaction().commit();
        em.close();

        benutzerListe.add(b);
        onNeu();
    }

    private static LocalDate parseBirthDate(String raw) throws DateTimeParseException {
        String input = raw.trim()
                .replace('–', '-') // En-dash
                .replace('—', '-') // Em-dash
                .replace('−', '-'); // Minus

        if (input.contains(".")) {
            return LocalDate.parse(input, INPUT_DOT_FMT);
        }
        return LocalDate.parse(input, INPUT_DASH_FMT);
    }

    @FXML
    private void onLoeschen() {
        Benutzer selected = tableBenutzer.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge(selected));
        em.getTransaction().commit();
        em.close();

        benutzerListe.remove(selected);
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/Dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/style/style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}