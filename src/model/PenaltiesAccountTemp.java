package model;

public class PenaltiesAccountTemp {
    private int id;
    private String type;
    private String date;
    private String status;
    private double amount;

    public PenaltiesAccountTemp(int id,
            String type,
            String date, String status, double amount) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.status = status;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
