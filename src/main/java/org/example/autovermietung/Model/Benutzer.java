package org.example.autovermietung.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Kunden")
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kunden_id")
    private Integer kundenId;

    private String vorname;
    private String nachname;
    private Integer age;  // ← Von 'int' zu 'Integer' geändert (kann null sein)
    private String email;
    private String telefon;
    private String adresse;

    public Benutzer() {}

    public Benutzer(String vorname, String nachname, int alter, String email, String telefon, String adresse) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.age = alter;
        this.email = email;
        this.telefon = telefon;
        this.adresse = adresse;
    }

    public Integer getKundenId() { return kundenId; }
    public Integer getId() { return kundenId; }

    public String getVorname() { return vorname; }
    public String getNachname() { return nachname; }
    public Integer getAlter() { return age; }  // ← Auch 'Integer' statt 'int'
    public String getEmail() { return email; }
    public String getTelefon() { return telefon; }
    public String getAdresse() { return adresse; }

    public void setKundenId(Integer id) { this.kundenId = id; }
    public void setVorname(String vorname) { this.vorname = vorname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
    public void setAlter(Integer alter) { this.age = alter; }  // ← 'Integer' Parameter
    public void setEmail(String email) { this.email = email; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}