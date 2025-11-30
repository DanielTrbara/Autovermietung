package org.example.autovermietung;

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
    private Car car;

    private LocalDate startDatum;
    private LocalDate endDatum;

    public Rental() {}

    public Rental(Customer customer, Car car,
                  LocalDate startDatum, LocalDate endDatum) {
        this.customer = customer;
        this.car = car;
        this.startDatum = startDatum;
        this.endDatum = endDatum;
    }
}