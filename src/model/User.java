package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.AlertTools;
import tools.DatabaseTools;

public class User {
    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty role;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty date;
    private SimpleStringProperty blacklisted;
    private SimpleIntegerProperty active;

    public User(int id, String username, String password, String role, String phoneNumber, String date) {

        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.date = new SimpleStringProperty(date);
        this.blacklisted = new SimpleStringProperty("");
        this.active = new SimpleIntegerProperty(0);
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
                AlertTools.AlertErrorContactSupport();
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

    public int getActive() {
        return active.get();
    }

    public void setActive(int active) {
        this.active.set(active);
    }

    /**
     * change password for the given id
     * 
     * @param newPassword the new password to be set
     * @return return true if successfully change password, false otherwise
     */
    public boolean changePassword(String newPassword) {
        Connection connection = DatabaseTools.getConnection();

        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, this.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                this.setPassword(newPassword);

                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }

        return false;
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

    /**
     * check if the given value is exist in given field
     * 
     * @param field the field to be checked
     * @param value the value to be checked
     * @return return true if the value is exist in the field
     */
    public static boolean isValueExist(String field, String value) {

        Connection connection = DatabaseTools.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE " + field + " = ?");
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement, resultSet);
                return true;
            }

        } catch (SQLException e) {
            AlertTools.AlertErrorContactSupport();
        }

        return false;
    }

    public static boolean addCustomer(String username, String password, String phoneNumber) {
        String sql = "INSERT INTO users (username, password, role, phone_number, created_at) VALUES (?, ?, 'user', ?, NOW())";

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phoneNumber);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement);
                return true;
            } else {
                DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement);
            }

        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }

        return false;
    }

    public static boolean addLibarian(String username, String password, String phoneNumber) {
        String sql = "INSERT INTO users (username, password, role, phone_number, active ,created_at) VALUES (?, ?, 'libarian', ?, 1,NOW())";

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phoneNumber);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement);
                return true;
            } else {
                DatabaseTools.closeQueryOperationWithPreparedStatement(connection, preparedStatement);
            }

        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }

        return false;
    }

}
