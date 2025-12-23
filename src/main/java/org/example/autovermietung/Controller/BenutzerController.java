package org.example.autovermietung.Controller;

import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.persistence.EntityManager;
import org.example.autovermietung.JpaUtil;
import org.example.autovermietung.Model.Benutzer;

public class BenutzerController {

    // ====== TABLE + SPALTEN (Anzeige) ======
    @FXML private TableView<Benutzer> tableBenutzer;

    @FXML private TableColumn<Benutzer, String> colVorname;
    @FXML private TableColumn<Benutzer, String> colNachname;
    @FXML private TableColumn<Benutzer, Integer> colAlter;
    @FXML private TableColumn<Benutzer, String> colEmail;
    @FXML private TableColumn<Benutzer, String> colTelefon;
    @FXML private TableColumn<Benutzer, String> colAdresse;

    // ====== EINGABEFELDER ======
    @FXML private TextField txtVorname;
    @FXML private TextField txtNachname;
    @FXML private TextField txtAlter;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtSuche;

    // ====== DATENHALTUNG ======
    // Hauptliste mit allen Benutzern
    private final ObservableList<Benutzer> benutzerListe =
            FXCollections.observableArrayList();

    // Gefilterte + sortierte Sicht auf die Liste
    private FilteredList<Benutzer> filtered;
    private SortedList<Benutzer> sorted;

    @FXML
    private void initialize() {

        // ====== TABLE <-> MODEL VERKNÜPFUNG ======
        // Property-Namen müssen exakt den Getter-Namen entsprechen
        colVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        colNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        colAlter.setCellValueFactory(new PropertyValueFactory<>("alter"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        // ====== FILTER + SORT SETUP ======
        filtered = new FilteredList<>(benutzerListe, b -> true);
        sorted = new SortedList<>(filtered);

        // Sortierung der Tabelle (Klick auf Spaltenkopf)
        sorted.comparatorProperty().bind(tableBenutzer.comparatorProperty());
        tableBenutzer.setItems(sorted);

        // ====== SUCHFUNKTION ======
        txtSuche.textProperty().addListener((obs, oldV, q) -> {
            String s = q == null ? "" : q.toLowerCase().trim();

            filtered.setPredicate(b -> {
                if (s.isEmpty()) return true;

                // Suche in mehreren Feldern
                return (b.getVorname() != null && b.getVorname().toLowerCase().contains(s))
                        || (b.getNachname() != null && b.getNachname().toLowerCase().contains(s))
                        || (b.getEmail() != null && b.getEmail().toLowerCase().contains(s))
                        || (b.getTelefon() != null && b.getTelefon().toLowerCase().contains(s));
            });
        });

        // ====== DB LADEN ======
        loadFromDb();
    }

    // Holt alle Benutzer aus der Datenbank
    private void loadFromDb() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            benutzerListe.setAll(
                    em.createQuery("select b from Benutzer b", Benutzer.class)
                            .getResultList()
            );
        } finally {
            em.close();
        }
    }

    // Formular zurücksetzen
    @FXML
    private void onNeu() {
        txtVorname.clear();
        txtNachname.clear();
        txtAlter.clear();
        txtEmail.clear();
        txtTelefon.clear();
        txtAdresse.clear();
        tableBenutzer.getSelectionModel().clearSelection();
    }

    // Neuen Benutzer speichern
    @FXML
    private void onHinzufuegen() {
        String alterText = txtAlter.getText().trim();
        if (alterText.isEmpty()) return; // Minimal-Validierung

        Benutzer b = new Benutzer(
                txtVorname.getText(),
                txtNachname.getText(),
                Integer.parseInt(alterText),
                txtEmail.getText(),
                txtTelefon.getText(),
                txtAdresse.getText()
        );

        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(b);              // INSERT
        em.getTransaction().commit();
        em.close();

        benutzerListe.add(b);       // UI aktualisieren
        onNeu();
    }

    // Ausgewählten Benutzer löschen
    @FXML
    private void onLoeschen() {
        Benutzer selected = tableBenutzer.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge(selected)); // merge nötig bei detached Entity
        em.getTransaction().commit();
        em.close();

        benutzerListe.remove(selected); // UI aktualisieren
    }
}