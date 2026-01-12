package org.example.autovermietung.Model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "FinanceTransaction")
public class Earning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "occurred_at", columnDefinition = "TEXT")
    @Convert(converter = LocalDateTimeIsoConverter.class)
    private LocalDateTime occurredAt;

    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "rental_id")
    private Integer rentalId;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;

    public static final String TYPE_RENTAL_INCOME = "RENTAL_INCOME";
    public static final String TYPE_MANUAL_INCOME = "MANUAL_INCOME";

    protected Earning() {
    }

    public Earning(double amount, LocalDateTime occurredAt) {
        this.amount = amount;
        this.occurredAt = occurredAt;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public Integer getCarId() {
        return carId;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Converter(autoApply = false)
    public static class LocalDateTimeIsoConverter implements AttributeConverter<LocalDateTime, String> {
        private static final DateTimeFormatter SQLITE_DATE_TIME = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendPattern(".SSS")
                .optionalEnd()
                .toFormatter();

        @Override
        public String convertToDatabaseColumn(LocalDateTime attribute) {
            return attribute == null ? null : attribute.toString();
        }

        @Override
        public LocalDateTime convertToEntityAttribute(String dbData) {
            if (dbData == null || dbData.isBlank()) {
                return null;
            }
            String trimmed = dbData.trim();
            try {
                return LocalDateTime.parse(trimmed);
            } catch (DateTimeParseException ignored) {
                return LocalDateTime.parse(trimmed, SQLITE_DATE_TIME);
            }
        }
    }
}