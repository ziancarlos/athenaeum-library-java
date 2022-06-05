package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchasing {
    private SimpleIntegerProperty id;
    private SimpleStringProperty purchasingDate;
    private SimpleStringProperty supplierName;
    private SimpleIntegerProperty totalBooksBought;
    private SimpleDoubleProperty totalAmount;

    public Purchasing(int id, String purchasingDate, String supplierName, int totalBooksBought, double totalAmount) {
        this.id = new SimpleIntegerProperty(id);
        this.purchasingDate = new SimpleStringProperty(purchasingDate);
        this.supplierName = new SimpleStringProperty(supplierName);
        this.totalBooksBought = new SimpleIntegerProperty(totalBooksBought);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
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

    public String getPurchasingDate() {
        return purchasingDate.get();
    }

    public SimpleStringProperty purchasingDateProperty() {
        return purchasingDate;
    }

    public void setPurchasingDate(String purchasingDate) {
        this.purchasingDate.set(purchasingDate);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public int getTotalBooksBought() {
        return totalBooksBought.get();
    }

    public SimpleIntegerProperty totalBooksBoughtProperty() {
        return totalBooksBought;
    }

    public void setTotalBooksBought(int totalBooksBought) {
        this.totalBooksBought.set(totalBooksBought);
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }
}
