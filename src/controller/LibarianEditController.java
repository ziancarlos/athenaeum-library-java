package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Libarian;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.MD5;
import tools.ValidationTools;

public class LibarianEditController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    private Libarian libarian;

    public void initialize() {
        usernameTf.setDisable(true);
    }

    @FXML
    void addBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(passwordTf)) {
            AlertTools.showAlertError("Password text field is empty", "Please fill in all fields");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(8, 45, passwordTf.getText())) {
            AlertTools.showAlertError("Password is invalid", "Password must be between 8 and 45 characters");

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
            preparedStatementSelect = connection.prepareStatement("SELECT * FROM libarians WHERE id = ? FOR UPDATE");
            preparedStatementSelect.setInt(1, libarian.getId());

            resultSet = preparedStatementSelect.executeQuery();

            if (resultSet.next()) {
                if (!resultSet.getString("password").equals(MD5.getMd5(passwordTf.getText()))) {
                    preparedStatementUpdate = connection
                            .prepareStatement("UPDATE libarians SET password = ? WHERE id = ?");
                    preparedStatementUpdate.setString(1, MD5.getMd5(passwordTf.getText()));
                    preparedStatementUpdate.setInt(2, libarian.getId());

                    affectedRows = preparedStatementUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        AlertTools.showAlertInformation("Password changed!", "Password has been changed");

                        BackBtn.backBtnActionEvent(event);

                        libarian.setPassword(MD5.getMd5(passwordTf.getText()));

                        connection.commit();
                    } else {
                        AlertTools.showAlertError("Password change failed", "Password change failed");

                        setDefaultTf();

                        connection.rollback();
                    }

                } else {
                    AlertTools.showAlertError("Password unchanged!", "Password is unchanged");

                    setDefaultTf();

                    connection.rollback();
                }

            } else {
                AlertTools.showAlertError("Password change failed", "Password change failed");

                setDefaultTf();

                connection.rollback();
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity error!", "Error");

            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Database connectivity error!", "Error");
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
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity error!", "Error");
            }
        }

    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setLibarian(Libarian libarian) {
        this.libarian = libarian;

        setDefaultTf();
    }

    private void setDefaultTf() {
        usernameTf.setText(libarian.getUsername());
        passwordTf.setText("");
    }

}
