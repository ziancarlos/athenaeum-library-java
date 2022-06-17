package model;

public class PurchasingDetail {
    private int id;
    private String supplierName;
    private int totalBooks;
    private double totalAmount;
    private String purchasingDate;

    public PurchasingDetail(int id, String supplierName, int totalBooks, double totalAmount, String purchasingDate) {
        this.id = id;
        this.supplierName = supplierName;
        this.totalBooks = totalBooks;
        this.totalAmount = totalAmount;
        this.purchasingDate = purchasingDate;
    }

    public int getId() {
        return id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPurchasingDate() {
        return purchasingDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPurchasingDate(String purchasingDate) {
        this.purchasingDate = purchasingDate;
    }

}
