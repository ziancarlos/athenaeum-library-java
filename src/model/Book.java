package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty categoryName;
    private Category category;
    private SimpleDoubleProperty boughtPrice;
    private SimpleIntegerProperty purchasing_id;

    public Book(int id, String name,Category category, double boughtPrice, int purchasing_id) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = category;
        this.categoryName = new SimpleStringProperty(category.getName());
        this.boughtPrice = new SimpleDoubleProperty(boughtPrice);
        this.purchasing_id = new SimpleIntegerProperty(purchasing_id);
    }

    public Book(String name,Category category, double boughtPrice) {
        this.name = new SimpleStringProperty(name);
        this.category = category;
        this.categoryName = new SimpleStringProperty(category.getName());
        this.boughtPrice = new SimpleDoubleProperty(boughtPrice);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCategoryName() {
        return categoryName.get();
    }

    public SimpleStringProperty categoryNameProperty() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getBoughtPrice() {
        return boughtPrice.get();
    }

    public SimpleDoubleProperty boughtPriceProperty() {
        return boughtPrice;
    }

    public void setBoughtPrice(double boughtPrice) {
        this.boughtPrice.set(boughtPrice);
    }

    public int getPurchasing_id() {
        return purchasing_id.get();
    }

    public SimpleIntegerProperty purchasing_idProperty() {
        return purchasing_id;
    }

    public void setPurchasing_id(int purchasing_id) {
        this.purchasing_id.set(purchasing_id);
    }
    
}
