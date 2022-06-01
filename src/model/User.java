package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.DatabaseTools;

public class User {
    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty role;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty date;
    private SimpleStringProperty blacklisted;

    public User(int id, String username, String password, String role, String phoneNumber, String date) {

        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.date = new SimpleStringProperty(date);
        this.blacklisted = new SimpleStringProperty("");
        // check role
        if (this.getRole().equals("user")) {
            Connection connection = DatabaseTools.getConnection();

            String sql = "CALL checkIfThereIsPenaltiesUnpaid(?,?);";

            try {
                CallableStatement callableStatement = connection.prepareCall(sql);
                callableStatement.setInt(1, this.getId());
                callableStatement.registerOutParameter(2, java.sql.Types.INTEGER);

                callableStatement.execute();

                Integer result = (Integer) callableStatement.getObject(2, Integer.class);

                if (result > 0) {
                    this.blacklisted.setValue("Yes");
                } else {
                    this.blacklisted.setValue("No");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            this.setBlacklisted("unknown");
        }

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

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getBlacklisted() {
        return blacklisted.get();
    }

    public void setBlacklisted(String blacklisted) {
        this.blacklisted.set(blacklisted);
    }

    /**
     * check if the given username and password match with the database
     * 
     * @param username the username to be checked
     * @param password the password to be checked
     * @return return user model if its valid or null if its invalid
     * @throws SQLException
     */
    public static User validateUser(String username, String password) throws SQLException {
        Connection connection = DatabaseTools.getConnection();
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            User user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("role"), resultSet.getString("phone_number"),
                    resultSet.getString("created_at"));

            DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement, resultSet);

            return user;
        }

        DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement, resultSet);

        return null;
    }

}
