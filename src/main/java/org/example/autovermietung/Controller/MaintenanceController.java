package org.example.autovermietung.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class MaintenanceController {

    @FXML
    private TableView<MaintenanceRecord> maintenanceTable;

    @FXML
    private TableColumn<MaintenanceRecord, String> herstellerColumn;

    @FXML
    private TableColumn<MaintenanceRecord, String> modellColumn;

    @FXML
    private TableColumn<MaintenanceRecord, Integer> baujahrColumn;

    @FXML
    private TableColumn<MaintenanceRecord, Integer> oilKmColumn;

    @FXML
    private TableColumn<MaintenanceRecord, String> engineDamageColumn;

    @FXML
    private TableColumn<MaintenanceRecord, String> repairsColumn;

    @FXML
    private Button backButton;

    @FXML
    private Button addButton;

    // Database connection details
    private static final String DB_URL = "jdbc:sqlite:autovermietung.db";
    // If using MySQL/PostgreSQL instead:
    // private static final String DB_URL = "jdbc:mysql://localhost:3306/autovermietung";
    // private static final String DB_USER = "your_username";
    // private static final String DB_PASSWORD = "your_password";

    private ObservableList<MaintenanceRecord> maintenanceData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        setupTableColumns();

        // Load data from database
        loadMaintenanceData();
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/autovermietung/dashboard.fxml")
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
            System.err.println("Error navigating back to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddNew() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/autovermietung/MaintenanceAdd.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) addButton.getScene().getWindow();
            Scene scene = new Scene(root, 1440, 1200);
            scene.getStylesheets().add(
                    getClass().getResource("/style/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.err.println("Error opening add form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openDetailView(MaintenanceRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/autovermietung/MaintenanceDetail.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) maintenanceTable.getScene().getWindow();
            Scene scene = new Scene(root, 1440, 1200);
            scene.getStylesheets().add(
                    getClass().getResource("/style/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.err.println("Error opening detail view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        herstellerColumn.setCellValueFactory(new PropertyValueFactory<>("hersteller"));
        modellColumn.setCellValueFactory(new PropertyValueFactory<>("modell"));
        baujahrColumn.setCellValueFactory(new PropertyValueFactory<>("baujahr"));
        oilKmColumn.setCellValueFactory(new PropertyValueFactory<>("oilKmRemaining"));
        engineDamageColumn.setCellValueFactory(new PropertyValueFactory<>("engineDamage"));
        repairsColumn.setCellValueFactory(new PropertyValueFactory<>("repairs"));
    }
    
    private void loadMaintenanceData() {
        maintenanceData.clear();

        // JOIN mit AddCar Tabelle
        String query = """
                    SELECT 
                        m.maintenance_id,
                        m.car_id,
                        m.oil_km_remaining,
                        m.engine_damage,
                        m.repairs,
                        a.brand,
                        a.model,
                        a.year
                    FROM Maintenance m
                    INNER JOIN AddCar a ON m.car_id = a.car_id
                    ORDER BY m.maintenance_id
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                int oilKm = rs.getInt("oil_km_remaining");
                String engineDamage = rs.getString("engine_damage");
                String repairs = rs.getString("repairs");

                // Aus der AddCar Tabelle
                String hersteller = rs.getString("brand");
                String modell = rs.getString("model");
                int baujahr = rs.getInt("year");

                MaintenanceRecord record = new MaintenanceRecord(
                        carId,
                        hersteller,
                        modell,
                        baujahr,
                        oilKm,
                        engineDamage != null && !engineDamage.isEmpty() ? engineDamage : "-",
                        repairs != null && !repairs.isEmpty() ? repairs : "0711 6709300"
                );

                maintenanceData.add(record);
            }

            maintenanceTable.setItems(maintenanceData);

        } catch (SQLException e) {
            System.err.println("Error loading maintenance data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        // For SQLite
        return DriverManager.getConnection(DB_URL);

        // For MySQL/PostgreSQL, use:
        // return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Inner class for table data model
    public static class MaintenanceRecord {
        private final int carId;
        private final String hersteller;
        private final String modell;
        private final int baujahr;
        private final int oilKmRemaining;
        private final String engineDamage;
        private final String repairs;

        public MaintenanceRecord(int carId, String hersteller, String modell, int baujahr,
                                 int oilKmRemaining, String engineDamage, String repairs) {
            this.carId = carId;
            this.hersteller = hersteller;
            this.modell = modell;
            this.baujahr = baujahr;
            this.oilKmRemaining = oilKmRemaining;
            this.engineDamage = engineDamage;
            this.repairs = repairs;
        }

        // Getters for TableView binding
        public int getCarId() {
            return carId;
        }

        public String getHersteller() {
            return hersteller;
        }

        public String getModell() {
            return modell;
        }

        public int getBaujahr() {
            return baujahr;
        }

        // Format oil km for display
        public String getOilKmRemaining() {
            return oilKmRemaining + " km";
        }

        public String getEngineDamage() {
            return engineDamage;
        }

        public String getRepairs() {
            return repairs;
        }
    }

    // Refresh data from database
    public void refreshData() {
        loadMaintenanceData();
    }
}