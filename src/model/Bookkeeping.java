package model;

public class Bookkeeping {
    private int id;
    private String doubleEntryType;
    private String transactionType;
    private double amount;
    private String paymentDate;

    public Bookkeeping(int id, String doubleEntryType, String transactionType, double amount, String paymentDate) {
        this.id = id;
        this.doubleEntryType = doubleEntryType;
        this.transactionType = transactionType;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public String getDoubleEntryType() {
        return doubleEntryType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoubleEntryType(String doubleEntryType) {
        this.doubleEntryType = doubleEntryType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void sePayementDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
