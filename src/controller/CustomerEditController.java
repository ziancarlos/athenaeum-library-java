package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Customer;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.MD5;
import tools.ValidationTools;

public class CustomerEditController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private TextField usernameTf;

    private Customer customer;

    public void initialize() {
        usernameTf.setDisable(true);
        phoneNumberTf.setDisable(true);
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void editOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(passwordTf)) {
            AlertTools.showAlertError("Password and Phone number text field is empty!", "Please fill in all fields");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(8, 45, passwordTf.getText())) {
            tools.AlertTools.showAlertError("Password is invalid", "Password must be between 8 and 45 characters");

            setDefaultTf();

            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatementSelect = null;
        PreparedStatement preparedStatementUpdate = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            preparedStatementSelect = connection.prepareStatement("SELECT * FROM users WHERE id = ? FOR UPDATE");
            preparedStatementSelect.setInt(1, customer.getId());

            resultSet = preparedStatementSelect.executeQuery();
            if (resultSet.next()) {

                if (!resultSet.getString("password").equals(MD5.getMd5(passwordTf.getText()))) {

                    preparedStatementUpdate = connection
                            .prepareStatement("UPDATE users SET password = ? WHERE id = ?");
                    preparedStatementUpdate.setString(1, MD5.getMd5(passwordTf.getText()));
                    preparedStatementUpdate.setInt(2, customer.getId());

                    affectedRows = preparedStatementUpdate.executeUpdate();

                    if (affectedRows > 0) {
                        AlertTools.showAlertInformation("Success", "Password has been updated");

                        BackBtn.backBtnActionEvent(event);

                        connection.commit();
                    } else {
                        AlertTools.showAlertError("Error", "Password has not been updated");

                        setDefaultTf();

                        connection.rollback();
                    }
                } else {
                    AlertTools.showAlertError("Error!", "Password has not been updated");

                    setDefaultTf();

                    connection.rollback();
                }

            } else {
                AlertTools.showAlertError("Error!", "Contact Support!");

                setDefaultTf();

                connection.rollback();
            }
        } catch (SQLException e) {
            AlertTools.showAlertError("Error!", "Contact Support!");

            setDefaultTf();

            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                AlertTools.showAlertError("Error!", "Contact Support!");

            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatementSelect != null) {
                    preparedStatementSelect.close();
                }
                if (preparedStatementUpdate != null) {
                    preparedStatementUpdate.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                AlertTools.showAlertError("Error!", "Contact Support!");
            }
        }

    }

    public void setCustomer(Customer customer) {
        this.customer = customer;

        setDefaultTf();
    }

    private void setDefaultTf() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM customers WHERE id = ?");
            preparedStatement.setInt(1, customer.getId());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usernameTf.setText(resultSet.getString("username"));
                passwordTf.setText("");
                phoneNumberTf.setText(resultSet.getString("phone_number"));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());
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
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }

    }

}
