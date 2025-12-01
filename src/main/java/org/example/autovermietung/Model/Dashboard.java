package org.example.autovermietung.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.autovermietung.Controller.DashboardController;
// Debugging
import java.io.File;
import java.net.URL;
import java.util.Objects;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {

            // Laden der FXML
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Dashboard.class.getResource("/org/example/autovermietung/dashboard.fxml")
            );
            Parent root = fxmlLoader.load();

            // Laden der CSS
            Scene scene = new Scene(root, 1440, 1200);

            System.out.println("Working dir = " + new File(".").getAbsolutePath());
            System.out.println("Check target = " + new File("target/classes").exists());
            System.out.println("Check style.css = " +
                    new File("target/classes/org/example/autovermietung/style/style.css").exists());



            // Controller und Username setzen
            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.setUsername("MaxMustermann");

            // Stage-Konfig
            stage.setTitle("Autovermietung");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}