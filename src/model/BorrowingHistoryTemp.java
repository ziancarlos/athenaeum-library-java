package model;

public class BorrowingHistoryTemp {
    private int id;
    private Book book;
    private String startDate;
    private String endDate;
    private String status;

    public BorrowingHistoryTemp(int id, Book book, String endDate, String startDate, String status) {
        this.id = id;
        this.book = book;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
