package org.example.autovermietung.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Benutzer benutzer;

    @ManyToOne
    private AddCar addCar;

    private LocalDate startDatum;
    private LocalDate endDatum;

    public Rental() {}

    public Rental(Benutzer benutzer, AddCar addCar,
                  LocalDate startDatum, LocalDate endDatum) {
        this.benutzer = benutzer;
        this.addCar = addCar;
        this.startDatum = startDatum;
        this.endDatum = endDatum;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public AddCar getAddCar() {
        return addCar;
    }

    public void setAddCar(AddCar addCar) {
        this.addCar = addCar;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public LocalDate getEndDatum() {
        return endDatum;
    }

    public void setEndDatum(LocalDate endDatum) {
        this.endDatum = endDatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
