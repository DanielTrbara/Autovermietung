package org.example.autovermietung.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class MaintenanceAddController {

    @FXML
    private ComboBox<CarItem> carComboBox;

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
        repairsField.setText("0711 312077"); // TÜV Süd Esslingen

        // Load cars into ComboBox
        loadCars();
    }

    private void loadCars() {
        ObservableList<CarItem> cars = FXCollections.observableArrayList();

        String query = "SELECT car_id, brand, model, year FROM AddCar ORDER BY brand, model";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");

                cars.add(new CarItem(carId, brand, model, year));
            }

            carComboBox.setItems(cars);

            if (cars.isEmpty()) {
                showError("Keine Fahrzeuge gefunden. Bitte fügen Sie zuerst Fahrzeuge hinzu.");
                saveButton.setDisable(true);
            }

        } catch (SQLException e) {
            System.err.println("Error loading cars: " + e.getMessage());
            e.printStackTrace();
            showError("Fehler beim Laden der Fahrzeuge aus der Datenbank.");
        }
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
            CarItem selectedCar = carComboBox.getValue();
            int carId = selectedCar.getCarId();
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
            showError("Ungültige Zahl in Ölwechsel-Kilometer.");
        }
    }

    private boolean validateInputs() {
        // Check if car is selected
        if (carComboBox.getValue() == null) {
            showError("Bitte wählen Sie ein Fahrzeug aus.");
            return false;
        }

        if (oilKmField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie die Kilometer bis zum Ölwechsel ein.");
            return false;
        }

        // Validate numeric field
        try {
            Integer.parseInt(oilKmField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Ölwechsel muss eine Zahl sein.");
            return false;
        }

        return true;
    }

    private boolean insertMaintenance(int carId, int oilKm, String engineDamage, String repairs) {
        String insertQuery = """
                    INSERT INTO Maintenance (car_id, oil_km_remaining, engine_damage, repairs)
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

    // Inner class for ComboBox items
    public static class CarItem {
        private final int carId;
        private final String brand;
        private final String model;
        private final int year;

        public CarItem(int carId, String brand, String model, int year) {
            this.carId = carId;
            this.brand = brand;
            this.model = model;
            this.year = year;
        }

        public int getCarId() {
            return carId;
        }

        @Override
        public String toString() {
            return year + " " + brand + " " + model + " (ID: " + carId + ")";
        }
    }
}