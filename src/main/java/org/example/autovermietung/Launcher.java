package org.example.autovermietung;

import org.example.autovermietung.Databases.MaintenanceDB;
import org.example.autovermietung.Model.Dashboard;

import java.sql.SQLException;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        MaintenanceDB.initDB();
        Dashboard.main(args);
    }
}
