package org.example.autovermietung.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DashboardController {

    @FXML
    private Button btnOpenGarage;

    @FXML
    private Button KundenButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button maintenanceButton;


    // ========== NAVIGATION ==========

    @FXML
    private void handleOpenGarage(ActionEvent event) {
        switchScene(event, "/org/example/autovermietung/CarList.fxml");
    }

    @FXML
    private void handleOpenBenutzer(ActionEvent event) {

        switchScene(event, "/org/example/autovermietung/Benutzer.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/style/style.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/autovermietung/Maintenance.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) maintenanceButton.getScene().getWindow();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/style/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Label getUsernameLabel() {
        return usernameLabel;
    }

    public Button getMaintenanceButton() {
        return maintenanceButton;
    }
}
