package org.example.autovermietung.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Repository.CarRepository;

import java.io.IOException;

public class AddCarController {

    @FXML private TextField txtBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtColor;
    @FXML private TextField txtPrice;

    private final CarRepository carRepository = new CarRepository();

    @FXML
    private void handleSaveCar(javafx.event.ActionEvent event) {

        try {
            String brand = txtBrand.getText();
            String model = txtModel.getText();
            int year = Integer.parseInt(txtYear.getText());
            String color = txtColor.getText();
            double pricePerDay = Double.parseDouble(txtPrice.getText());

            // Reihenfolge wie im AddCar-Konstruktor (brand, model, pricePerDay, year, color)
            AddCar car = new AddCar(brand, model, pricePerDay, year, color);

            carRepository.save(car);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Erfolg");
            alert.setContentText("Auto wurde gespeichert.");
            alert.showAndWait();

            handleBack(event);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Fehler");
            alert.setContentText("Bitte überprüfe alle Eingaben (Jahr, Preis usw.).");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/example/autovermietung/CarList.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
