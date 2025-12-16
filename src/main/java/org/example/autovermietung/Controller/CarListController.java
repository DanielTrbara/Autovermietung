package org.example.autovermietung.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
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

    @FXML
    private FlowPane carContainer;

    @FXML
    private ScrollPane scrollPane;

    private final CarRepository carRepository = new CarRepository();

    private static final int CARD_WIDTH = 300;
    private static final int CARD_HEIGHT = 350;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // ScrollPane / Viewport transparent
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-control-inner-background: transparent;");
        scrollPane.applyCss();
        Node viewport = scrollPane.lookup(".viewport");
        if (viewport != null) {
            viewport.setStyle("-fx-background-color: transparent;");
        }

        // Autos aus der DB holen
        List<AddCar> cars = carRepository.findAll();

        // Karten erzeugen
        for (AddCar car : cars) {
            Pane card = createCarCard(car);
            carContainer.getChildren().add(card);
        }

        scrollPane.setOnScroll(event -> {
            double delta = event.getDeltaY();   // Mausrad-Bewegung (hoch/runter)
            double current = scrollPane.getHvalue(); // aktueller Horizontal-Scroll

            // Stärke des Scrollens einstellen (0.002–0.02)
            double scrollSpeed = 0.003;

            scrollPane.setHvalue(current - delta * scrollSpeed);
        });

        // WICHTIG: keine feste Höhe/Breite mehr im Controller setzen
        // Das übernimmt jetzt FlowPane mit orientation=VERTICAL + prefWrapLength.
    }

    private Pane createCarCard(AddCar car) {

        VBox card = new VBox();
        card.setSpacing(12);
        card.setPadding(new Insets(15));
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMaxWidth(CARD_WIDTH);

        // Default Design
        card.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 20, 0.2, 0, 5);"
        );

        // Hover-Effekt
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

        // Bild
        ImageView imageView = new ImageView(loadCarImage(car.getImageName()));
        imageView.setFitWidth(CARD_WIDTH);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        // Titel
        Label title = new Label(car.getBrand() + " " + car.getModel());
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");

        // Details
        VBox details = new VBox(5);
        details.getChildren().addAll(
                makeDetail("Baujahr: " + car.getYear()),
                makeDetail("Farbe: " + car.getColor()),
                makeDetail("Preis/Tag: " + car.getPricePerDay() + " €")
        );

        // Löschen-Button
        Button removeButton = new Button("Löschen");
        removeButton.setStyle(
                "-fx-background-color: #d93636; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        removeButton.setOnAction(event -> {
            carRepository.delete(car);
            carContainer.getChildren().remove(card);
        });

        card.getChildren().addAll(imageView, title, details, removeButton);
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
            return new Image(
                    Objects.requireNonNull(getClass().getResource(fullPath)).toString()
            );
        } catch (Exception e) {
            return new Image(
                    Objects.requireNonNull(getClass().getResource(basePath + "placeholder.png")).toString()
            );
        }
    }

    @FXML
    private void handleAddCar(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/org/example/autovermietung/AddCar-View.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/style/style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);   // 🔥 DAS war der fehlende Teil
        stage.show();
    }


    @FXML
    private void handleBack(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/org/example/autovermietung/Dashboard.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/style/style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);   // 🔥 WICHTIG
        stage.show();
    }

}
