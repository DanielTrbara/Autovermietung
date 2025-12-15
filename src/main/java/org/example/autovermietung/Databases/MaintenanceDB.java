package org.example.autovermietung.Databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class MaintenanceDB {
    private MaintenanceDB() {
    }

    public static void initDB() throws SQLException {
        try (Connection connnection = Database.getConnection();
             Statement statement = connnection.createStatement()
        ) {
            statement.execute("""
                        CREATE TABLE IF NOT EXISTS maintenance (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            car_id INTEGER NOT NULL,
                            oil_km_remaining INTEGER,
                            engine_damage TEXT,
                            repairs TEXT,
                            FOREIGN KEY (car_id) REFERENCES car(id)
                        )
                    """);
        } catch (SQLException error) {
            throw new RuntimeException("Database initialization failed", error);
        }
    }
}
