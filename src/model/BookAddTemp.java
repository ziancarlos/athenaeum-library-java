package model;

public class BookAddTemp {
    private Book book;
    private String dueDate;
    private int period;
    private double amount;

    public BookAddTemp(Book book, String dueDate, int period, double amount) {
        this.book = book;
        this.dueDate = dueDate;
        this.period = period;
        this.amount = amount;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
