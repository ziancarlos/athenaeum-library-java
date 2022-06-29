package model;

public class Penalties {
    private int id;
    private String penaltyDate;
    private String penaltyType;
    private String paymentStatus;
    private double amount;
    private int bookId;
    private Customer customer;
    private int borrowingId;

    public Penalties(int id, String penaltyDate, String paymentStatus, double amount, int bookId, Customer customer,
            int borrowingId, String penaltyType) {
        this.id = id;
        this.penaltyDate = penaltyDate;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.bookId = bookId;
        this.customer = customer;
        this.borrowingId = borrowingId;
        this.penaltyType = penaltyType;
    }

    public int getId() {
        return id;
    }

    public String getPenaltyDate() {
        return penaltyDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public int getBookId() {
        return bookId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPenaltyDate(String penalyDate) {
        this.penaltyDate = penalyDate;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public String getPenaltyType() {
        return penaltyType;
    }
}
