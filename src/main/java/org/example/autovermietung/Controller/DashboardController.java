package org.example.autovermietung.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {


    @FXML
    private Button btnOpenGarage;

    private Label usenameLabel;

    public Label getUsenameLabel() {
        return usenameLabel;
    }

    @FXML
    private Button maintenanceButton;

    public Button getMaintenanceButton() {
        return maintenanceButton;
    }

    /**
     * Wird ausgeführt, wenn der User auf "Zur Garage" klickt.
     */
    @FXML
    private void handleOpenGarage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org.example.autovermietung/carlist.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/Maintenance.fxml"));
            Parent root = loader.load();

            // get the current stage
            Stage stage = (Stage) maintenanceButton.getScene().getWindow();

            Scene scene = new Scene(root, 1440, 1200);
            stage.setMaxWidth(1440);
            stage.setMaxHeight(1200);
            stage.setResizable(true);
            scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}