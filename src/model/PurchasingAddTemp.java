package model;

public class PurchasingAddTemp {
    private String bookName;
    private Category category;
    private double price;

    public PurchasingAddTemp(String bookName, Category category, double price) {
        this.bookName = bookName;
        this.category = category;
        this.price = price;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
