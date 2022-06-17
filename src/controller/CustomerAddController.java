package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.MD5;
import tools.ValidationTools;

public class CustomerAddController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private TextField usernameTf;

    public void initialize() {
        setDefaultTf();
    }

    @FXML
    void addOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf, phoneNumberTf)) {
            tools.AlertTools.showAlertError("Username/Password/Phone number text field is empty",
                    "Please fill in all fields");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 30, usernameTf.getText())) {
            tools.AlertTools.showAlertError("Username is invalid", "Username must be between 3 and 30 characters");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(8, 45, passwordTf.getText())) {
            tools.AlertTools.showAlertError("Password is invalid", "Password must be between 3 and 30 characters");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(8, 16, phoneNumberTf.getText())) {
            tools.AlertTools.showAlertError("Phone number is invalid",
                    "Phone number must be between 8 to 16 characters");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isPhoneNumberValid(phoneNumberTf.getText())) {
            tools.AlertTools.showAlertError("Phone number is invalid",
                    "Phone number must begin with +628 and contain between 8 and 16 characters");

            setDefaultTf();

            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatementSelectUsername = null;
        PreparedStatement preparedStatementSelectPhoneNumber = null;
        PreparedStatement preparedStatementInsert = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            preparedStatementSelectUsername = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatementSelectUsername.setString(1, usernameTf.getText());

            resultSet = preparedStatementSelectUsername.executeQuery();

            if (!resultSet.next()) {
                preparedStatementSelectPhoneNumber = connection
                        .prepareStatement("SELECT * FROM users WHERE phone_number = ?");
                preparedStatementSelectPhoneNumber.setString(1, phoneNumberTf.getText());

                resultSet = preparedStatementSelectPhoneNumber.executeQuery();
                if (!resultSet.next()) {
                    preparedStatementInsert = connection.prepareStatement(
                            "INSERT INTO users (username, password, phone_number, role, active, created_at) VALUES (?, ?, ?, 'customer', 'active', NOW())");
                    preparedStatementInsert.setString(1, usernameTf.getText());
                    preparedStatementInsert.setString(2, MD5.getMd5(passwordTf.getText()));
                    preparedStatementInsert.setString(3, phoneNumberTf.getText());

                    affectedRows = preparedStatementInsert.executeUpdate();
                    if (affectedRows > 0) {
                        tools.AlertTools.showAlertInformation("Success", "Customer added successfully");

                        connection.commit();

                        BackBtn.backBtnActionEvent(event);
                    } else {
                        tools.AlertTools.showAlertError("Error", "Customer not added");

                        setDefaultTf();

                        connection.rollback();
                    }
                } else {
                    connection.rollback();

                    setDefaultTf();

                    tools.AlertTools.showAlertError("Phone number already exist", "Phone number already exist");
                }
            } else {
                connection.rollback();

                setDefaultTf();

                tools.AlertTools.showAlertError("Username already exists", "Username already exists");
            }

        } catch (Exception e) {
            tools.AlertTools.showAlertError("Error", "Customer not added");

            try {
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                AlertTools.showAlertError("Error", "Customer not added");
            }

            setDefaultTf();
        } finally {
            try {

                if (preparedStatementSelectUsername != null) {
                    preparedStatementSelectUsername.close();
                }
                if (preparedStatementSelectPhoneNumber != null) {
                    preparedStatementSelectPhoneNumber.close();
                }
                if (preparedStatementInsert != null) {
                    preparedStatementInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                AlertTools.showAlertError("Error", "Customer not added");
            }
        }

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    private void setDefaultTf() {
        usernameTf.setText("");
        passwordTf.setText("");
        phoneNumberTf.setText("+628");
    }

}
