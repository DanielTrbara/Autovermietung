package org.example.autovermietung.Model;

import jakarta.persistence.*;

@Entity
public class AddCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String brand;
    private String model;
    private double pricePerDay;
    private int year;
    private String color;

    public AddCar() {
    }

    public AddCar(String brand, String model, double pricePerDay, int year, String color) {
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.year = year;
        this.color = color;
    }

    public int getId() {
        return id;
    }
    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public double getPricePerDay() {
        return pricePerDay;
    }
    public int getYear() {
        return year;
    }
    public String getColor() {
        return color;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
