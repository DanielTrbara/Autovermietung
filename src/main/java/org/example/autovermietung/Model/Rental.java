package org.example.autovermietung.Model;

import jakarta.persistence.*;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import java.time.LocalDate;

@Entity
@Table(name = "Rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kunden_id", referencedColumnName = "kunden_id")
    private Benutzer kunde;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id", referencedColumnName = "car_id")
    private AddCar auto;

    @Column(name = "start_date", nullable = false, columnDefinition = "TEXT")
    @Convert(converter = LocalDateIsoConverter.class)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "TEXT")
    @Convert(converter = LocalDateIsoConverter.class)
    private LocalDate endDate;

    @Column(name = "price_per_day_at_rental", nullable = false)
    private double pricePerDay;

    public Rental() {}

    public Rental(Benutzer kunde, AddCar auto, LocalDate startDate, LocalDate endDate) {
        this.kunde = kunde;
        this.auto = auto;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getRentalId() { return rentalId; }
    public Benutzer getKunde() { return kunde; }
    public AddCar getAuto() { return auto; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    public void setKunde(Benutzer kunde) { this.kunde = kunde; }
    public void setAuto(AddCar auto) { this.auto = auto; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    @Converter(autoApply = false)
    public static class LocalDateIsoConverter implements AttributeConverter<LocalDate, String> {
        @Override
        public String convertToDatabaseColumn(LocalDate attribute) {
            return attribute == null ? null : attribute.toString(); // yyyy-MM-dd
        }

        @Override
        public LocalDate convertToEntityAttribute(String dbData) {
            return (dbData == null || dbData.isBlank()) ? null : LocalDate.parse(dbData.trim());
        }
    }
}