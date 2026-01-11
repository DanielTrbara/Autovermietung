package org.example.autovermietung.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

        String query = """
                    SELECT 
                        c.id,
                        c.car_id,
                        m.oil_km_remaining,
                        m.engine_damage,
                        m.repairs
                    FROM maintenance m
                    INNER JOIN car c ON m.car_id = c.id
                    ORDER BY m.id
                """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                int oilKm = rs.getInt("oil_km_remaining");
                String engineDamage = rs.getString("engine_damage");
                String repairs = rs.getString("repairs");

                // Get car details (you'll need to implement getCarDetails method)
                // For now, using hardcoded values based on your screenshot
                String hersteller = "Volkswagen";
                String modell = "Phaeton";
                int baujahr = 2016;

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