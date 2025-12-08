package org.example.autovermietung.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.autovermietung.Controller.DashboardController;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        try {
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-Regular.otf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-Medium.otf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-SemiBold.otf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-Bold.otf"), 14);

            // Laden der FXML
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Dashboard.class.getResource("/org/example/autovermietung/dashboard.fxml")
            );
            Parent root = fxmlLoader.load();

            // Laden der CSS
            Scene scene = new Scene(root, 1440, 1200);
            scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());


//            --- DEBUGGING ---
//            System.out.println("Working dir = " + new File(".").getAbsolutePath());
//            System.out.println("Check target = " + new File("target/classes").exists());
//            System.out.println("Check style.css = " + new File("src/main/resources/style/style.css").exists());


            DashboardController dashboardController = fxmlLoader.getController();


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