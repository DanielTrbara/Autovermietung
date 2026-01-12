package org.example.autovermietung.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.autovermietung.Controller.EarningsController;
import org.example.autovermietung.Model.Earning;

import java.time.LocalDate;

public class EarningsViewController {

    @FXML
    private TextField betragField;

    @FXML
    private DatePicker datumPicker;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> earningsList;

    @FXML
    private Label totalLabel;

    private EarningsController controller = new EarningsController();

    @FXML
    public void initialize() {
        datumPicker.setValue(LocalDate.now());

        addButton.setOnAction(e -> {
            try {
                double betrag = Double.parseDouble(betragField.getText());
                LocalDate datum = datumPicker.getValue();
                controller.addEarning(betrag, datum);
                updateList();
                betragField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Ungültiger Betrag", "Bitte eine gültige Zahl eingeben.");
            }
        });

        updateList(); // initiale Anzeige
    }

    private void updateList() {
        earningsList.getItems().clear();
        for (Earning e : controller.getAllEarnings()) {
            earningsList.getItems().add(String.format("%.2f € - %s", e.getBetrag(), e.getDatum()));
        }
        totalLabel.setText(String.format("Gesamt: %.2f €", controller.getTotalEarnings()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
