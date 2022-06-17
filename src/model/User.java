package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.DatabaseTools;
import tools.MD5;

public class User {
    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty role;
    private SimpleStringProperty createdAt;

    public User(int id, String username, String password, String role, String createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    /**
     * check if username exist
     * 
     * @return true if username exist
     * 
     */
    public static boolean isUsernameExist(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static User getUsernameAndPasswordValid(String username, String password) throws SQLException {
        Connection connection;
        PreparedStatement preparedStatement;
        PreparedStatement preparedStatementGetUser;
        ResultSet resultSet;

        connection = DatabaseTools.getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");

        preparedStatement.setString(1, username);
        preparedStatement.setString(2, MD5.getMd5(password));

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            switch (resultSet.getString("role")) {
                case "admin":
                    preparedStatementGetUser = connection.prepareStatement("SELECT * FROM admin WHERE id = ?");
                    preparedStatementGetUser.setInt(1, resultSet.getInt("id"));

                    resultSet = preparedStatementGetUser.executeQuery();

                    if (resultSet.next()) {
                        Admin admin = new Admin(resultSet.getInt("id"), resultSet.getString("username"),
                                resultSet.getString("password"), resultSet.getString("role"),
                                resultSet.getString("created_at"));

                        return admin;
                    }
                    break;
                case "libarian":
                    preparedStatementGetUser = connection.prepareStatement("SELECT * FROM libarians WHERE id = ?");
                    preparedStatementGetUser.setInt(1, resultSet.getInt("id"));

                    resultSet = preparedStatementGetUser.executeQuery();

                    if (resultSet.next()) {
                        Libarian libarian = new Libarian(resultSet.getInt("id"), resultSet.getString("username"),
                                resultSet.getString("password"), resultSet.getString("role"),
                                resultSet.getString("created_at"), resultSet.getString("active"));
                        return libarian;
                    }
                    break;

                case "customer":
                    preparedStatementGetUser = connection.prepareStatement("SELECT * FROM customers WHERE id = ?");
                    preparedStatementGetUser.setInt(1, resultSet.getInt("id"));

                    resultSet = preparedStatementGetUser.executeQuery();

                    if (resultSet.next()) {
                        Customer customer = new Customer(resultSet.getInt("id"), resultSet.getString("username"),
                                resultSet.getString("password"), resultSet.getString("role"),
                                resultSet.getString("created_at"), resultSet.getString("phone_number"),
                                resultSet.getString("blacklisted"));

                        return customer;
                    }
                    break;
                default:
                    return null;

            }
        }

        return null;
    }

}
