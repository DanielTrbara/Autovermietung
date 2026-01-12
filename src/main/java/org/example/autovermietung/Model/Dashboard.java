package org.example.autovermietung.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Fonts (optional – null-check wäre sauber, aber ok)
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-Regular.otf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-Medium.otf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-SemiBold.otf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/org/example/autovermietung/fonts/Pretendard-Bold.otf"), 14);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/dashboard.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1200, 1080);
            scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

            stage.setTitle("Autovermietung");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}