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

    @FXML private Button KundenButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button maintenanceButton;

    @FXML
    private HBox titleBar;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button closeButton;


    // Variables for window dragging
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isMaximized = false;

    @FXML
    public void initialize() {
        setupButtonHoverEffects();
    }

    // ========== WINDOW CONTROL METHODS ==========

    @FXML
    private void handleMinimize(ActionEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleMaximize(ActionEvent event) {
        Stage stage = (Stage) maximizeButton.getScene().getWindow();

        if (isMaximized) {
            // Restore to normal size
            stage.setMaximized(false);
            maximizeButton.setText("▢");
            isMaximized = false;
        } else {
            // Maximize
            stage.setMaximized(true);
            maximizeButton.setText("❐");
            isMaximized = true;
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleTitleBarPressed(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void handleOpenRental(javafx.event.ActionEvent event) throws IOException {
        switchScene(event, "/org/example/autovermietung/Rental.fxml");
    }

    @FXML
    private void handleTitleBarDragged(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();

        if (isMaximized) {
            stage.setMaximized(false);
            isMaximized = false;
            maximizeButton.setText("▢");

            xOffset = stage.getWidth() / 2;
        }

        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    private void setupButtonHoverEffects() {
        minimizeButton.setOnMouseEntered(e ->
                minimizeButton.setStyle(
                        "-fx-background-color: #1a1a1a; " +
                                "-fx-text-fill: #FFFFFF; " +
                                "-fx-font-size: 20px; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-cursor: hand; "
                )
        );
        minimizeButton.setOnMouseExited(e ->
                minimizeButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: #CCCCCC; " +
                                "-fx-font-size: 20px; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-cursor: hand; "
                )
        );

        maximizeButton.setOnMouseEntered(e ->
                maximizeButton.setStyle(
                        "-fx-background-color: #1a1a1a; " +
                                "-fx-text-fill: #FFFFFF; " +
                                "-fx-font-size: 20px; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-cursor: hand; "
                )
        );
        maximizeButton.setOnMouseExited(e ->
                maximizeButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: #CCCCCC; " +
                                "-fx-font-size: 20px; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-cursor: hand; "
                )
        );

        // Close button hover (red)
        closeButton.setOnMouseEntered(e ->
                closeButton.setStyle(
                        "-fx-background-color: #E81123; " +
                                "-fx-text-fill: #FFFFFF; " +
                                "-fx-font-size: 14px; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-cursor: hand; "
                )
        );
        closeButton.setOnMouseExited(e ->
                closeButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: #CCCCCC; " +
                                "-fx-font-size: 14px; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-cursor: hand; "
                )
        );
    }

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
            stage.setMaximized(true);   // 🔥 wichtig
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
