package model;

public class Borrowing {
    private int id;
    private Customer customer;
    private int borrowedBooks;
    private String borrowingDate;
    private String borrowingStatus;

    public Borrowing(int id, Customer customer, int borrowedBooks, String borrowingDate, String borrowingStatus) {
        this.id = id;
        this.customer = customer;
        this.borrowedBooks = borrowedBooks;
        this.borrowingDate = borrowingDate;
        this.borrowingStatus = borrowingStatus;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public String getBorrowingDate() {
        return borrowingDate;
    }

    public String getBorrowingStatus() {
        return borrowingStatus;
    }

    public void setBorrowingStatus(String borrowingStatus) {
        this.borrowingStatus = borrowingStatus;
    }

    public void setBorrowingDate(String borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public void setBorrowedBooks(int borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setId(int id) {
        this.id = id;
    }

}
