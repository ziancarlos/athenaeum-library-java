package model;

public class BookManageTemp {
    private Book book;
    private String dueDate;
    private String status;
    private double price;

    public BookManageTemp(Book book, String dueDate, String status, double price) {
        this.book = book;
        this.dueDate = dueDate;
        this.status = status;
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
