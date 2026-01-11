package org.example.autovermietung.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class Earning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double Betrag;
    private LocalDate datum;

    protected Earning() {}

    public Earning(double Betrag, LocalDate datum){
        this.Betrag = Betrag;
        this.datum = datum;
    }
    public double getBetrag(){
        return Betrag;
    }
    public LocalDate getDatum(){
        return datum;
    }
    public void setBetrag(double Betrag){
        this.Betrag = Betrag;
    }
    public void setDatum(LocalDate datum){
        this.datum = datum;
    }
}
