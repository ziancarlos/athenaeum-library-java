package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;

public class LibarianEditController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private TextField usernameTf;

    private User user;

    @FXML
    void addBtn(ActionEvent event) {
        // check if the password is the same
        if (passwordTf.getText().equals(user.getPassword())) {
            AlertTools.AlertError("Error!", "Password is not change!", null);

            setDefaultTf();

            return;
        }

        // check if the password is valid
        if (passwordTf.getText().length() < 8) {
            AlertTools.AlertError("Error!", "Password is not valid!", "Please enter a valid password!");

            setDefaultTf();

            return;
        }

        // change the password
        if (user.changePassword(passwordTf.getText())) {
            AlertTools.AlertConfirmation("Success!", "Password is changed!", null);

            BackBtnTools.backBtnActionEvent(event);
        } else {
            AlertTools.AlertError("Error!", "Password is not changed!", null);

            setDefaultTf();
        }

    }

    private void setDefaultTf() {
        passwordTf.setText(user.getPassword());
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    public void setUser(User user) {
        this.user = user;

        usernameTf.setDisable(true);
        phoneNumberTf.setDisable(true);

        usernameTf.setText(user.getUsername());
        passwordTf.setText(user.getPassword());
        phoneNumberTf.setText(user.getPhoneNumber());
    }

}
