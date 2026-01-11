package org.example.autovermietung.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;



@Entity
@Table(name = "Kunden")
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kunden_id")
    private Integer kundenId;

    private String vorname;
    private String nachname;
    @Column(name = "birth_date")
    @Convert(converter = LocalDateIsoConverter.class)
    private LocalDate birthDate;
    private String email;
    private String telefon;
    private String adresse;

    public Benutzer() {}

    public Benutzer(String vorname, String nachname, LocalDate birthDate, String email, String telefon, String adresse) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.birthDate = birthDate;
        this.email = email;
        this.telefon = telefon;
        this.adresse = adresse;
    }

    public Integer getKundenId() { return kundenId; }
    public Integer getId() { return kundenId; }

    public String getVorname() { return vorname; }
    public String getNachname() { return nachname; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getEmail() { return email; }
    public String getTelefon() { return telefon; }
    public String getAdresse() { return adresse; }

    public void setKundenId(Integer id) { this.kundenId = id; }
    public void setVorname(String vorname) { this.vorname = vorname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Converter
    public static class LocalDateIsoConverter implements AttributeConverter<LocalDate, String> {
        @Override
        public String convertToDatabaseColumn(LocalDate attribute) {
            return attribute == null ? null : attribute.toString(); // yyyy-MM-dd
        }

        @Override
        public LocalDate convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            String s = dbData.trim();
            if (s.isEmpty()) return null;

            // If old data is stored as milliseconds since epoch, convert it.
            // Example: "1128031200000"
            if (s.matches("\\d{10,13}")) {
                long millis = Long.parseLong(s);
                if (s.length() == 10) millis *= 1000; // seconds -> millis
                return java.time.Instant.ofEpochMilli(millis)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
            }

            // Accept plain ISO date (yyyy-MM-dd)
            if (s.length() >= 10) {
                // also handles timestamps like "2005-08-31 00:00:00.000" by taking the date-part
                return LocalDate.parse(s.substring(0, 10));
            }
            return LocalDate.parse(s);
        }
    }
}