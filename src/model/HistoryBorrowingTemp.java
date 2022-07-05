package model;

public class HistoryBorrowingTemp {
    private int id;
    private Book book;
    private String endDate;
    private String startDate;
    private String status;

    public HistoryBorrowingTemp(int id, Book book, String endDate, String startDate, String status) {
        this.id = id;
        this.book = book;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
