package model;

public class Admin extends User {
    Admin(int id, String username, String password, String role, String createdAt) {
        super(id, username, password, role, createdAt);
    }
}
