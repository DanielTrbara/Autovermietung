package org.example.autovermietung.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.example.autovermietung.Model.Earning;
import org.example.autovermietung.Repository.EarningRepository;

public class EarningsController {
    private final EarningRepository repository;

    public EarningsController() {
        this.repository = new EarningRepository();
    }

    // Neue Einnahme hinzufügen
    public void addEarning(double betrag, LocalDate datum) {
        Earning e = new Earning(betrag, datum.atStartOfDay());
        e.setType(Earning.TYPE_MANUAL_INCOME);
        repository.save(e);
    }

    // Alle Einnahmen abrufen
    public List<Earning> getAllEarnings() {
        return repository.findAll();
    }

    public List<Earning> getEarningsBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findBetween(start, end);
    }

    // Gesamtsumme berechnen
    public double getTotalEarnings() {
        return repository.findAll()
                .stream()
                .mapToDouble(Earning::getAmount)
                .sum();
    }

    public double getTotalEarnings(List<Earning> earnings) {
        return earnings.stream()
                .mapToDouble(Earning::getAmount)
                .sum();
    }

    // Einnahme löschen
    public void deleteEarning(Earning earning) {
        repository.delete(earning);
    }
}