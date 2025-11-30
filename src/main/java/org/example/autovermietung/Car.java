package org.example.autovermietung;

import jakarta.persistence.*;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String marke;
    private String modell;
    private double preisProTag;

    public Car() {
    }

    public Car(String marke, String modell, double preisProTag) {
        this.marke = marke;
        this.modell = modell;
        this.preisProTag = preisProTag;
    }

    // *** WICHTIG: public Getter für TableView ***

    public int getId() {
        return id;
    }

    public String getMarke() {
        return marke;
    }

    public String getModell() {
        return modell;
    }

    public double getPreisProTag() {
        return preisProTag;
    }

    // Setter (für ORM / Formulare)

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public void setPreisProTag(double preisProTag) {
        this.preisProTag = preisProTag;
    }
}