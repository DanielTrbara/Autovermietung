package org.example.autovermietung.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private AddCar addCar;

    private LocalDate startDatum;
    private LocalDate endDatum;

    public Rental() {}

    public Rental(Customer customer, AddCar addCar,
                  LocalDate startDatum, LocalDate endDatum) {
        this.customer = customer;
        this.addCar = addCar;
        this.startDatum = startDatum;
        this.endDatum = endDatum;
    }
}