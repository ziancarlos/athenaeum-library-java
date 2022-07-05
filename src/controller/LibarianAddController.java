package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.MD5;
import tools.UiTools;
import tools.ValidationTools;

public class LibarianAddController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void addOnAction(ActionEvent event) {
        if (AlertTools.showAlertConfirmationWithOptional("Confirmation!",
                "Are you sure you want to add this new user?").get() == ButtonType.CANCEL) {

            setDefaultTf();

            return;
        }
        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf)) {
            tools.AlertTools.showAlertError("Username/Password text field is empty", "Please fill in all fields");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 30, usernameTf.getText())) {
            tools.AlertTools.showAlertError("Username is invalid", "Username must be between 3 and 30 characters");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(8, 45, passwordTf.getText())) {
            tools.AlertTools.showAlertError("Password is invalid", "Password must be between 8 and 45 characters");

            setDefaultTf();

            return;
        }

        if (User.isUsernameExist(usernameTf.getText())) {
            tools.AlertTools.showAlertError("Username already exist", "Username already exist");

            setDefaultTf();

            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection
                    .prepareStatement(
                            "INSERT INTO libarians (username, password, role, active, created_at) VALUES (?, ?, 'libarian', 'active', NOW())");
            preparedStatement.setString(1, usernameTf.getText());
            preparedStatement.setString(2, MD5.getMd5(passwordTf.getText()));

            affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                AlertTools.showAlertInformation("Success", "Libarian added successfully!");

                BackBtn.backBtnActionEvent(event);
            } else {
                AlertTools.showAlertError("Error", "Libarian not added");

                setDefaultTf();
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    private void setDefaultTf() {
        UiTools.setTextFieldEmpty(usernameTf, passwordTf);
    }

}
