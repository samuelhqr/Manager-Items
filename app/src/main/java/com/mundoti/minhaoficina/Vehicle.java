package com.mundoti.minhaoficina;

public class Vehicle {
    private String id; // Adicionado a propriedade id para armazenar o ID do veículo
    private String model;
    private String brand;
    private String year;
    private String plate;
    private String color;
    private String owner;
    private String observation;

    public Vehicle() {
        // Construtor vazio necessário para Firebase
    }

    // Construtor com o ID adicionado como parâmetro
    public Vehicle(String id, String model, String brand, String year, String plate, String color, String owner, String observation) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.plate = plate;
        this.color = color;
        this.owner = owner;
        this.observation = observation;
    }

    // Métodos getters e setters para as propriedades do veículo
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
