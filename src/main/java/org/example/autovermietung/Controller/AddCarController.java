package org.example.autovermietung.Controller;

import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Repository.CarRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AddCarController {

    @FXML private TextField txtBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtColor;
    @FXML private TextField txtPrice;
    @FXML private Label lblImageSelected;

    private String selectedImageName = null;

    private AddCar editingCar = null;

    private final CarRepository carRepository = new CarRepository();

    private void saveImageToResources(File file) {
        try {
            // 1. Quelle
            Path source = file.toPath();

            // 2. Ziel (resources)
            Path resourcesPath = Paths.get(
                    "src/main/resources/org/example/autovermietung/assets/cars/" + file.getName()
            );

            // 3. Ziel (BUILD ORDNER → JavaFX lädt von hier!)
            Path buildPath = Paths.get(
                    "target/classes/org/example/autovermietung/assets/cars/" + file.getName()
            );

            // Ordner bei Bedarf erzeugen
            Files.createDirectories(resourcesPath.getParent());
            Files.createDirectories(buildPath.getParent());

            // Bild in beide Ordner kopieren
            Files.copy(source, resourcesPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(source, buildPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Bild gespeichert in:");
            System.out.println(" → " + resourcesPath);
            System.out.println(" → " + buildPath);

        } catch (IOException e) {
            System.err.println("FEHLER beim Kopieren des Bildes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(AddCar car) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(car);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @FXML
    private void handleSaveCar(javafx.event.ActionEvent event) {

        try {
            AddCar car;

            // 🔁 EDIT vs NEU
            if (editingCar != null) {
                car = editingCar;
            } else {
                car = new AddCar();
            }

            car.setBrand(txtBrand.getText());
            car.setModel(txtModel.getText());
            car.setYear(Integer.parseInt(txtYear.getText()));
            car.setColor(txtColor.getText());
            car.setPricePerDay(Double.parseDouble(txtPrice.getText()));
            car.setImageName(selectedImageName);

            if (editingCar == null) {
                carRepository.save(car);
            } else {
                carRepository.update(car);
            }

            handleBack(event);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Bitte überprüfe alle Eingaben.").show();
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

    @FXML
    private void handleSelectImage() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Bild auswählen");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            selectedImageName = file.getName();
            lblImageSelected.setText(selectedImageName);

            // Bild in Ressourcenordner kopieren
            saveImageToResources(file);
        }
    }

    public void setCarToEdit(AddCar car) {
        this.editingCar = car;

        txtBrand.setText(car.getBrand());
        txtModel.setText(car.getModel());
        txtYear.setText(String.valueOf(car.getYear()));
        txtColor.setText(car.getColor());
        txtPrice.setText(String.valueOf(car.getPricePerDay()));
        selectedImageName = car.getImageName();

        lblImageSelected.setText(
                (selectedImageName == null || selectedImageName.isBlank())
                        ? "Kein Bild gewählt"
                        : selectedImageName
        );

    }




}
