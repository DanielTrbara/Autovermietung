package org.example.autovermietung.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Repository.CarRepository;

import java.io.IOException;

public class CarListController {

    @FXML
    private TableView<AddCar> carTable;

    @FXML
    private TableColumn<AddCar, String> colBrand;
    @FXML
    private TableColumn<AddCar, String> colModel;
    @FXML
    private TableColumn<AddCar, Integer> colYear;
    @FXML
    private TableColumn<AddCar, String> colColor;
    @FXML
    private TableColumn<AddCar, Double> colPrice;

    private final ObservableList<AddCar> cars = FXCollections.observableArrayList();
    private final CarRepository carRepository = new CarRepository();

    @FXML
    public void initialize() {
        // Spalten mit AddCar-Properties verbinden (Getter-Namen!)
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        // Daten aus DB holen
        cars.setAll(carRepository.findAll());
        carTable.setItems(cars);
    }

    @FXML
    private void handleAddCar() {
        System.out.println("AddCar: Noch nicht implementiert");
    }

    @FXML
    private void handleDeleteCar() {
        AddCar selected = carTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            carRepository.delete(selected);   // Methode im Repo muss so existieren
            cars.remove(selected);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/autovermietung/dashboard.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}