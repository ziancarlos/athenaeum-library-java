package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.ValidationTools;

public class CustomerEditController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private TextField usernameTf;

    private User user;

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void editBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf, phoneNumberTf)) {
            AlertTools.AlertError("Error!", "Text field consist of blank!", "Please fill in all text field!");

            setDefaultTf();

            return;
        }

        String password = passwordTf.getText();

        if (password.equals(user.getPassword())) {
            AlertTools.AlertError("Error!", "Password is not change!", null);

            setDefaultTf();

            return;
        }

        if (password.length() < 8) {
            AlertTools.AlertError("Error!", "Password is not valid!", "Please enter a valid password!");

            setDefaultTf();

            return;
        }

        if (user.changePassword(password)) {
            AlertTools.AlertConfirmation("Success!", "Password is changed!", null);

            BackBtnTools.backBtnActionEvent(event);
        } else {
            AlertTools.AlertError("Error!", "Password is not changed!", null);

            setDefaultTf();
        }
    }

    void setUser(User user) {
        this.user = user;

        setDefaultTf();
    }

    private void setDefaultTf() {
        usernameTf.setText(user.getUsername());
        passwordTf.setText(user.getPassword());
        phoneNumberTf.setText(user.getPhoneNumber());

        usernameTf.setDisable(true);
        phoneNumberTf.setDisable(true);
    }

}
