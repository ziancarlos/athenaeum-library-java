package model;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.AlertTools;
import tools.DatabaseTools;

public class Book {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty categoryName;
    private Category category;
    private SimpleStringProperty availability;
    private SimpleStringProperty purchaseDate;
    private SimpleDoubleProperty boughtPrice;
    private SimpleIntegerProperty purchasing_id;

    // Book Controller
    public Book(int id, String name, Category category, int availability, String purchaseDate, double boughtPrice,
            int purchasing_id) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = category;
        this.categoryName = new SimpleStringProperty(category.getName());
        this.availability = new SimpleStringProperty((availability == 0) ? "No" : "Yes");
        this.purchaseDate = new SimpleStringProperty(purchaseDate);
        this.boughtPrice = new SimpleDoubleProperty(boughtPrice);
        this.purchasing_id = new SimpleIntegerProperty(purchasing_id);
    }

    // Purchase Add Controller
    public Book(String name, Category category, double boughtPrice) {
        this.name = new SimpleStringProperty(name);
        this.category = category;
        this.categoryName = new SimpleStringProperty(category.getName());
        this.boughtPrice = new SimpleDoubleProperty(boughtPrice);
    }

    public Book(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
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

    public String getAvailability() {
        return availability.get();
    }

    public SimpleStringProperty availabilityProperty() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability.set(availability);
    }

    public String getpurchaseDate() {
        return purchaseDate.get();
    }

    public SimpleStringProperty purchaseDateProperty() {
        return purchaseDate;
    }

    public void setpurchaseDate(String purchaseDate) {
        this.purchaseDate.set(purchaseDate);
    }

    public static boolean editBook(Book book, String newName, Category newCategory) {
        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE books SET name = ?, category_id = ?  WHERE id = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, newCategory.getId());

            preparedStatement.setInt(3, book.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                book.setName(newName);
                book.setCategory(newCategory);

                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }

        return false;
    }
}
