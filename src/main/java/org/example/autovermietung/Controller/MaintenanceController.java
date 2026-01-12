package org.example.autovermietung.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MaintenanceController {

    @FXML
    private ImageView carImage;

    @FXML
    private Label oilStatus;

    @FXML
    private Label engineStatus;

    @FXML
    private Label repairStatus;

    @FXML
    public void initialize() {
        // Load car image
        carImage.setImage(new Image("file:src/main/resources/images/vw_phaeton.png"));

        // Example data
        oilStatus.setText("Ölwechsel: noch 8000 km");
        engineStatus.setText("Motorschaden: -");
        repairStatus.setText("Reparaturen: -");
    }
}


