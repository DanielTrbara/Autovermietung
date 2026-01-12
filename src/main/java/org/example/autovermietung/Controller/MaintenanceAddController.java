package org.example.autovermietung.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MaintenanceAddController {

    @FXML
    private TextField carIdField;

    @FXML
    private TextField herstellerField;

    @FXML
    private TextField modellField;

    @FXML
    private TextField baujahrField;

    @FXML
    private TextField oilKmField;

    @FXML
    private TextField engineDamageField;

    @FXML
    private TextField repairsField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    // Database connection
    private static final String DB_URL = "jdbc:sqlite:autovermietung.db";

    @FXML
    public void initialize() {
        // Set default values
        engineDamageField.setText("-");
        repairsField.setText("0711 6709300");
    }

    @FXML
    private void handleSave() {
        // Hide previous errors
        errorLabel.setVisible(false);

        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        try {
            int carId = Integer.parseInt(carIdField.getText().trim());
            int oilKm = Integer.parseInt(oilKmField.getText().trim());
            String engineDamage = engineDamageField.getText().trim();
            String repairs = repairsField.getText().trim();

            // Insert into database
            if (insertMaintenance(carId, oilKm, engineDamage, repairs)) {
                // Success - navigate back to list
                handleBackToList();
            } else {
                showError("Fehler beim Speichern des Eintrags in der Datenbank.");
            }

        } catch (NumberFormatException e) {
            showError("Ungültige Zahlen in Fahrzeug-ID, Baujahr oder Ölwechsel-Kilometer.");
        }
    }

    private boolean validateInputs() {
        // Check required fields
        if (carIdField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Fahrzeug-ID ein.");
            return false;
        }

        if (herstellerField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie einen Hersteller ein.");
            return false;
        }

        if (modellField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return false;
        }

        if (baujahrField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Baujahr ein.");
            return false;
        }

        if (oilKmField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie die Kilometer bis zum Ölwechsel ein.");
            return false;
        }

        // Validate numeric fields
        try {
            Integer.parseInt(carIdField.getText().trim());
            Integer.parseInt(baujahrField.getText().trim());
            Integer.parseInt(oilKmField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Fahrzeug-ID, Baujahr und Ölwechsel müssen Zahlen sein.");
            return false;
        }

        return true;
    }

    private boolean insertMaintenance(int carId, int oilKm, String engineDamage, String repairs) {
        String insertQuery = """
                    INSERT INTO maintenance (car_id, oil_km_remaining, engine_damage, repairs)
                    VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, carId);
            stmt.setInt(2, oilKm);
            stmt.setString(3, engineDamage);
            stmt.setString(4, repairs);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleBackToList() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/autovermietung/Maintenance.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root, 1440, 1200);
            scene.getStylesheets().add(
                    getClass().getResource("/style/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.err.println("Error navigating back to list: " + e.getMessage());
            e.printStackTrace();
        }
    }
}