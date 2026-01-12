package org.example.autovermietung.Controller;

import org.example.autovermietung.Model.Earning;
import org.example.autovermietung.Repository.EarningRepository;

import java.time.LocalDate;
import java.util.List;

public class EarningsController {

    private EarningRepository repository;

    public EarningsController() {
        this.repository = new EarningRepository();
    }

    // Neue Einnahme hinzufügen
    public void addEarning(double betrag, LocalDate datum) {
        Earning e = new Earning(betrag, datum);
        repository.save(e);
    }

    // Alle Einnahmen abrufen
    public List<Earning> getAllEarnings() {
        return repository.findAll();
    }

    // Gesamtsumme berechnen
    public double getTotalEarnings() {
        return repository.findAll()
                .stream()
                .mapToDouble(Earning::getBetrag)
                .sum();
    }

    // Einnahme löschen
    public void deleteEarning(Earning earning) {
        repository.delete(earning);
    }
}
