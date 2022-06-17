package model;

import javafx.beans.property.SimpleStringProperty;

public class Customer extends User {
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty blacklisted;

    public Customer(int id, String username, String password, String role, String createdAt, String phoneNumber,
            String blacklisted) {
        super(id, username, password, role, createdAt);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.blacklisted = new SimpleStringProperty(blacklisted);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getBlacklisted() {
        return blacklisted.get();
    }

    public void setBlacklisted(String blacklisted) {
        this.blacklisted.set(blacklisted);
    }
}
