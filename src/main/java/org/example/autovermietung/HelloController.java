package org.example.autovermietung;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, String> colMarke;

    @FXML
    private TableColumn<Car, String> colModell;

    @FXML
    private TableColumn<Car, Double> colPreis;

    @FXML
    private TextField txtMarke;

    @FXML
    private TextField txtModell;

    @FXML
    private TextField txtPreis;

    private final CarRepository carRepository = new CarRepository();
    private final ObservableList<Car> cars = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colMarke.setCellValueFactory(new PropertyValueFactory<>("marke"));
        colModell.setCellValueFactory(new PropertyValueFactory<>("modell"));
        colPreis.setCellValueFactory(new PropertyValueFactory<>("preisProTag"));

        cars.setAll(carRepository.findAll());
        carTable.setItems(cars);
    }

    @FXML
    private void onSaveCarClick() {
        String marke = txtMarke.getText();
        String modell = txtModell.getText();
        double preis = Double.parseDouble(txtPreis.getText());

        Car car = new Car(marke, modell, preis);
        carRepository.save(car);
        cars.add(car);

        txtMarke.clear();
        txtModell.clear();
        txtPreis.clear();
    }
}