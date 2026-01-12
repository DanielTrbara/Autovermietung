package org.example.autovermietung.View;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.autovermietung.Controller.EarningsController;
import org.example.autovermietung.Model.Earning;

public class EarningsViewController {
    // ====== FXML Felder ======
    @FXML
    private TextField betragField;
    @FXML
    private DatePicker datumPicker;
    @FXML
    private TableView<Earning> earningsTable;
    @FXML
    private TableColumn<Earning, Double> betragColumn;
    @FXML
    private TableColumn<Earning, LocalDateTime> datumColumn;
    @FXML
    private Label totalLabel;

    // ====== Business Controller ======
    private final EarningsController controller = new EarningsController();

    // ====== Initialisierung ======
    @FXML
    public void initialize() {
        datumPicker.setValue(LocalDate.now());
        betragColumn.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().getAmount())
        );
        datumColumn.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().getOccurredAt())
        );
        refresh();
    }

    // ====== Button Action ======
    @FXML
    private void handleAddEarning() {
        try {
            double betrag = Double.parseDouble(betragField.getText());
            LocalDate datum = datumPicker.getValue();
            controller.addEarning(betrag, datum);
            betragField.clear();
            refresh();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte einen gültigen Betrag eingeben.");
            alert.showAndWait();
        }
    }

    private void refresh() {
        earningsTable.setItems(
                FXCollections.observableArrayList(controller.getAllEarnings())
        );
        totalLabel.setText(
                String.format("Gesamt: %.2f €", controller.getTotalEarnings())
        );
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