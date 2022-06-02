package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.ValidationTools;

public class CustomerAddController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private TextField usernameTf;

    public void initialize() {
        defaultTf();
    }

    @FXML
    void addBtn(ActionEvent event) {

        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf, phoneNumberTf)) {
            AlertTools.AlertError("Error!", "Text field consist of blank!", "Please fill in all text field!");

            defaultTf();

            return;
        }

        String username = usernameTf.getText();
        String password = passwordTf.getText();
        String phoneNumber = phoneNumberTf.getText();

        if (!ValidationTools.isPhoneNumberValid(phoneNumber)) {
            AlertTools.AlertError("Error!", "Invalid phone number!", "Please enter a valid phone number!");

            defaultTf();

            return;
        }

        if (password.length() < 8) {
            AlertTools.AlertError("Error!", "Password is not valid!", "Please enter a valid password!");

            defaultTf();

            return;
        }

        if (User.isValueExist("phone_number", phoneNumber)) {
            AlertTools.AlertError("Error!", "Phone number is already exist!", "Please enter a valid phone number!");

            defaultTf();

            return;
        }

        if (User.isValueExist("username", username)) {
            AlertTools.AlertError("Error!", "Username is already exist!", "Please enter a valid username!");

            defaultTf();

            return;
        }

        String sql = "INSERT INTO users (username, password, role, phone_number, created_at) VALUES (?, ?, 'user', ?, NOW())";

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, phoneNumber);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                AlertTools.AlertInformation("Success!", "User has been added!", "User has been added!");
                BackBtnTools.backBtnActionEvent(event);
            } else {
                AlertTools.AlertInformation("Error!", "User has not been added!", "User has not been added!");

                defaultTf();
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    private void defaultTf() {
        usernameTf.setText("");
        passwordTf.setText("");
        phoneNumberTf.setText("+628");
    }

}
