package org.example.autovermietung.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class DashboardController {


    @FXML
    private Button btnOpenGarage;

    /**
     * Wird ausgeführt, wenn der User auf "Zur Garage" klickt.
     */
    @FXML
    private void handleOpenGarage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org.example.autovermietung/carlist.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String event) {
    }


}