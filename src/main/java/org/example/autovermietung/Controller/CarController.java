package org.example.autovermietung.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Repository.CarRepository;

public class CarController {

    @FXML
    private TableView<AddCar> carTable;

    @FXML
    private TableColumn<AddCar, String> colBrand;

    @FXML
    private TableColumn<AddCar, String> colModel;

    @FXML
    private TableColumn<AddCar, Double> colPricePerDay;

    @FXML
    private TableColumn<AddCar, Integer> colYear;

    @FXML
    private TableColumn<AddCar, String> colColor;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtPricePerDay;

    @FXML
    private TextField txtYear;

    @FXML
    private TextField txtColor;

    private final CarRepository carRepository = new CarRepository();
    private final ObservableList<AddCar> addCars = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Spalten an Model-Properties binden (Namen müssen zu Gettern passen!)
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPricePerDay.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

        addCars.setAll(carRepository.findAll());
        carTable.setItems(addCars);
    }

    @FXML
    private void onSaveCarClick() {
        try {
            String brand = txtBrand.getText();
            String model = txtModel.getText();
            double pricePerDay = Double.parseDouble(txtPricePerDay.getText());
            int year = Integer.parseInt(txtYear.getText());
            String color = txtColor.getText();

            AddCar addCar = new AddCar(brand, model, pricePerDay, year, color);
            carRepository.save(addCar);
            addCars.add(addCar);

            txtBrand.clear();
            txtModel.clear();
            txtPricePerDay.clear();
            txtYear.clear();
            txtColor.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}