package org.example.autovermietung.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.autovermietung.Model.AddCar;
import org.example.autovermietung.Repository.CarRepository;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CarListController implements Initializable {

    @FXML private GridPane carGrid;
    @FXML private ScrollPane scrollPane;

    private final CarRepository carRepository = new CarRepository();

    private static final int ROWS = 3;
    private static final int CARD_WIDTH = 300;
    private static final int CARD_HEIGHT = 350;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // ScrollPane/Viewport transparent -> entfernt den weißen/grauen Hintergrund
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            // sorgt dafür, dass der Viewport nicht "weiß" rendert
            scrollPane.lookup(".viewport").setStyle("-fx-background-color: transparent;");
        });

        loadCars();

        // Mausrad: vertikales scrollen -> horizontal verschieben
        scrollPane.addEventFilter(ScrollEvent.SCROLL, e -> {
            if (e.getDeltaY() != 0) {
                double delta = -e.getDeltaY(); // hoch/runter
                double width = scrollPane.getContent().getBoundsInLocal().getWidth();
                double viewport = scrollPane.getViewportBounds().getWidth();

                if (width > viewport) {
                    double maxH = width - viewport;
                    double currentPixel = scrollPane.getHvalue() * maxH;
                    double newPixel = currentPixel + delta * 2.0; // Speed-Faktor

                    newPixel = Math.max(0, Math.min(maxH, newPixel));
                    scrollPane.setHvalue(newPixel / maxH);
                    e.consume();
                }
            }
        });
    }

    private void loadCars() {
        carGrid.getChildren().clear();

        List<AddCar> cars = carRepository.findAll();

        for (int i = 0; i < cars.size(); i++) {
            int column = i / ROWS;   // nach 3 Items -> neue Spalte
            int row = i % ROWS;      // 0,1,2

            Pane card = createCarCard(cars.get(i));
            carGrid.add(card, column, row);
        }
    }

    private Pane createCarCard(AddCar car) {

        VBox card = new VBox();
        card.setSpacing(12);
        card.setPadding(new Insets(15));
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMaxWidth(CARD_WIDTH);

        card.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 20, 0.2, 0, 5);"
        );

        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #242424; " +
                        "-fx-background-radius: 15; " +
                        "-fx-scale-x: 1.03; " +
                        "-fx-scale-y: 1.03; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.08), 25, 0.3, 0, 6);"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-background-radius: 15; " +
                        "-fx-scale-x: 1.0; " +
                        "-fx-scale-y: 1.0; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 20, 0.2, 0, 5);"
        ));

        ImageView imageView = new ImageView(loadCarImage(car.getImageName()));
        imageView.setFitWidth(CARD_WIDTH);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label title = new Label(car.getBrand() + " " + car.getModel());
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");

        VBox details = new VBox(5);
        details.getChildren().addAll(
                makeDetail("Baujahr: " + car.getYear()),
                makeDetail("Farbe: " + car.getColor()),
                makeDetail("Preis/Tag: " + car.getPricePerDay() + " €")
        );

        Button removeButton = new Button("Löschen");
        removeButton.setStyle("-fx-background-color: #d93636; -fx-text-fill: white; -fx-background-radius: 10;");
        removeButton.setOnAction(event -> {
            carRepository.delete(car);
            loadCars(); // ✅ Grid neu laden (sauber, keine falschen Removes)
        });

        Button editButton = new Button("Bearbeiten");
        editButton.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 10;");
        editButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/example/autovermietung/AddCar-View.fxml")
                );
                Scene scene = new Scene(loader.load());

                AddCarController controller = loader.getController();
                controller.setCarToEdit(car);

                Stage stage = (Stage) card.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        card.getChildren().addAll(imageView, title, details, removeButton, editButton);
        return card;
    }

    private Label makeDetail(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13;");
        return label;
    }

    private Image loadCarImage(String name) {
        String basePath = "/org/example/autovermietung/assets/cars/";
        String fullPath = basePath + "placeholder.png";

        if (name != null && !name.isBlank()) {
            fullPath = basePath + name;
        }

        try {
            return new Image(Objects.requireNonNull(getClass().getResource(fullPath)).toString());
        } catch (Exception e) {
            return new Image(Objects.requireNonNull(getClass().getResource(basePath + "placeholder.png")).toString());
        }
    }

    @FXML
    private void handleAddCar(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/AddCar-View.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void handleBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/autovermietung/Dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
