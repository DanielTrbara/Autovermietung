package org.example.autovermietung.View;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Controller.EarningsController;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Model.Benutzer;
import org.example.autovermietung.Model.Earning;
import org.example.autovermietung.Model.Rental;
import jakarta.persistence.EntityManager;

public class EarningsViewController {
    // ====== FXML Felder ======
    @FXML
    private ChoiceBox<String> rangeChoice;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private TableView<Earning> earningsTable;
    @FXML
    private TableColumn<Earning, String> betragColumn;
    @FXML
    private TableColumn<Earning, String> datumColumn;
    @FXML
    private TableColumn<Earning, String> kundeColumn;
    @FXML
    private TableColumn<Earning, String> autoColumn;
    @FXML
    private Label totalLabel;
    @FXML
    private LineChart<String, Number> earningsChart;

    // ====== Business Controller ======
    private final EarningsController controller = new EarningsController();
    private Map<Integer, Rental> rentalCache = new HashMap<>();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ====== Initialisierung ======
    @FXML
    public void initialize() {
        setupRangeChoice();
        betragColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.format("%.2f €", data.getValue().getAmount()))
        );
        datumColumn.setCellValueFactory(
                data -> new SimpleStringProperty(formatDateTime(data.getValue().getOccurredAt()))
        );
        kundeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(resolveKundeName(data.getValue()))
        );
        autoColumn.setCellValueFactory(
                data -> new SimpleStringProperty(resolveAutoName(data.getValue()))
        );
        refresh();
    }

    // ====== Button Action ======
    @FXML
    private void refresh() {
        List<Earning> earnings = controller.getEarningsBetween(resolveStartDateTime(), resolveEndDateTime());
        rentalCache = loadRentals(earnings);
        earningsTable.setItems(FXCollections.observableArrayList(earnings));
        totalLabel.setText(
                String.format("Gesamt: %.2f €", controller.getTotalEarnings(earnings))
        );
        updateChart(earnings);
    }

    private void setupRangeChoice() {
        rangeChoice.setItems(FXCollections.observableArrayList(
                "Alle",
                "Heute",
                "Letzte 7 Tage",
                "Dieser Monat",
                "Dieses Jahr"
        ));
        rangeChoice.getSelectionModel().selectFirst();
        rangeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            applyQuickRange(newValue);
            refresh();
        });

        fromDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> refresh());
        toDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> refresh());
    }

    private void applyQuickRange(String range) {
        LocalDate today = LocalDate.now();
        switch (range) {
            case "Heute" -> {
                fromDatePicker.setValue(today);
                toDatePicker.setValue(today);
            }
            case "Letzte 7 Tage" -> {
                fromDatePicker.setValue(today.minusDays(6));
                toDatePicker.setValue(today);
            }
            case "Dieser Monat" -> {
                fromDatePicker.setValue(today.withDayOfMonth(1));
                toDatePicker.setValue(today);
            }
            case "Dieses Jahr" -> {
                fromDatePicker.setValue(today.withDayOfYear(1));
                toDatePicker.setValue(today);
            }
            default -> {
                fromDatePicker.setValue(null);
                toDatePicker.setValue(null);
            }
        }
    }

    private LocalDateTime resolveStartDateTime() {
        LocalDate startDate = fromDatePicker.getValue();
        return startDate == null ? null : startDate.atStartOfDay();
    }

    private LocalDateTime resolveEndDateTime() {
        LocalDate endDate = toDatePicker.getValue();
        return endDate == null ? null : endDate.atTime(LocalTime.MAX);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }

    private Map<Integer, Rental> loadRentals(List<Earning> earnings) {
        Map<Integer, Rental> rentals = new HashMap<>();
        List<Integer> rentalIds = earnings.stream()
                .map(Earning::getRentalId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        if (rentalIds.isEmpty()) {
            return rentals;
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Rental> results = em.createQuery(
                            "SELECT r FROM Rental r WHERE r.rentalId IN :ids",
                            Rental.class
                    )
                    .setParameter("ids", rentalIds)
                    .getResultList();
            for (Rental rental : results) {
                rentals.put(rental.getRentalId(), rental);
            }
        } finally {
            em.close();
        }
        return rentals;
    }

    private String resolveKundeName(Earning earning) {
        Integer rentalId = earning.getRentalId();
        if (rentalId == null) {
            return "-";
        }
        Rental rental = rentalCache.get(rentalId);
        if (rental == null) {
            return "-";
        }
        Benutzer kunde = rental.getKunde();
        if (kunde == null) {
            return "-";
        }
        return String.format("%s %s", kunde.getVorname(), kunde.getNachname()).trim();
    }

    private String resolveAutoName(Earning earning) {
        Integer rentalId = earning.getRentalId();
        if (rentalId == null) {
            return "-";
        }
        Rental rental = rentalCache.get(rentalId);
        if (rental == null) {
            return "-";
        }
        AddCar auto = rental.getAuto();
        if (auto == null) {
            return "-";
        }
        return String.format("%s %s (%d)", auto.getBrand(), auto.getModel(), auto.getYear());
    }

    private void updateChart(List<Earning> earnings) {
        Map<LocalDate, Double> totalsByDate = new TreeMap<>();
        for (Earning earning : earnings) {
            LocalDateTime occurredAt = earning.getOccurredAt();
            if (occurredAt == null) {
                continue;
            }
            LocalDate date = occurredAt.toLocalDate();
            totalsByDate.merge(date, earning.getAmount(), Double::sum);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Einnahmen");
        totalsByDate.forEach((date, amount) -> series.getData().add(
                new XYChart.Data<>(date.toString(), amount)
        ));

        earningsChart.getData().setAll(series);
        applyChartStyles(series);
    }

    private void applyChartStyles(XYChart.Series<String, Number> series) {
        series.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-stroke: #EB512B;");
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/Dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}