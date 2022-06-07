package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.UiTools;
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
    void addBtn(ActionEvent event) {

        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf, phoneNumberTf)) {
            AlertTools.AlertError("Error!", "Text field consist of blank!", "Please fill in all text field!");

            setDefaultTf();

            return;
        }

        String username = usernameTf.getText();
        String password = passwordTf.getText();
        String phoneNumber = phoneNumberTf.getText();

        if (!ValidationTools.isPhoneNumberValid(phoneNumber)) {
            AlertTools.AlertError("Error!", "Invalid phone number!", "Please enter a valid phone number!");

            setDefaultTf();

            return;
        }

        if (password.length() < 8) {
            AlertTools.AlertError("Error!", "Password is not valid!", "Please enter a valid password!");

            setDefaultTf();

            return;
        }

        if (User.isValueExist("phone_number", phoneNumber)) {
            AlertTools.AlertError("Error!", "Phone number is already exist!", "Please enter a valid phone number!");

            setDefaultTf();

            return;
        }

        if (User.isValueExist("username", username)) {
            AlertTools.AlertError("Error!", "Username is already exist!", "Please enter a valid username!");

            setDefaultTf();

            return;
        }

        if (User.addCustomer(username, password, phoneNumber)) {
            AlertTools.AlertInformation("Success!", "User has been added!", "User has been added!");
            BackBtnTools.backBtnActionEvent(event);
        } else {
            AlertTools.AlertInformation("Error!", "User has not been added!", "User has not been added!");

            setDefaultTf();
        }
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    private void setDefaultTf() {
        UiTools.setTextFieldEmpty(usernameTf, passwordTf);
        phoneNumberTf.setText("+628");
    }

}
