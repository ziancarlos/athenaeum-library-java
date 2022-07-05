package model;

public class BorrowingHistoryTemp {
    private int id;
    private Book book;
    private String startDate;
    private String endDate;
    private String status;
    private double price;

    public BorrowingHistoryTemp(int id, Book book, String endDate, String startDate, String status, double price) {
        this.id = id;
        this.book = book;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
        this.price = price;
    }

    public double getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
