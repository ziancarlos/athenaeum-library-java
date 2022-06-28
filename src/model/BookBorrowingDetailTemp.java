package model;

public class BookBorrowingDetailTemp {
    private Customer customer;
    private String dueDate;
    private String status;

    public BookBorrowingDetailTemp(Customer customer, String dueDate, String status) {
        this.customer = customer;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
