package org.example.autovermietung.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

            stage.setTitle("Autovermietung");
            stage.initStyle(StageStyle.UNDECORATED);

            // ✅ Start „maximiert“ aber stabil: setX/Y + setWidth/Height auf Visual Bounds
            var vb = Screen.getPrimary().getVisualBounds();
            stage.setX(vb.getMinX());
            stage.setY(vb.getMinY());
            stage.setWidth(vb.getWidth());
            stage.setHeight(vb.getHeight());

            stage.setScene(scene);

            // ✅ Optional: Fullscreen Toggle per F11 / ESC
            scene.setOnKeyPressed(e -> {
                switch (e.getCode()) {
                    case F11 -> stage.setFullScreen(!stage.isFullScreen());
                    case ESCAPE -> { if (stage.isFullScreen()) stage.setFullScreen(false); }
                }
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}