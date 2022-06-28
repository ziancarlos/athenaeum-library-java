package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty availability;
    private Category category;
    private SimpleStringProperty purchaseDate;

    public Book(int id, String name, String availability, Category category) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.availability = new SimpleStringProperty(availability);
        this.category = category;
    }

    public Book(int id, String name, Category category) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = category;
    }

    public Book(int id, String name, String availability, Category category, String purchaseDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.availability = new SimpleStringProperty(availability);
        this.category = category;
        this.purchaseDate = new SimpleStringProperty(purchaseDate);
    }

    public Book(int id, String name, String availability) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.availability = new SimpleStringProperty(availability);
    }

    public Book(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAvailability() {
        return availability.get();
    }

    public void setAvailability(String availability) {
        this.availability.set(availability);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPurchaseDate() {
        return purchaseDate.get();
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate.set(purchaseDate);
    }

    @Override
    public String toString() {
        return getId() + ". " + getName();
    }

}
