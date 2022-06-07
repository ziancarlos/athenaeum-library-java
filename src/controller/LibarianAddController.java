package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.UiTools;
import tools.ValidationTools;

public class LibarianAddController {

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
            AlertTools.AlertError("Error!", "Username, password or phone number field is blank!", null);

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isPhoneNumberValid(phoneNumberTf.getText())) {
            AlertTools.AlertError("Error!", "Phone number is not valid!", null);

            setDefaultTf();

            return;
        }

        if (User.isValueExist("username", usernameTf.getText())) {
            AlertTools.AlertError("Error!", "Username is already exist!", null);

            setDefaultTf();

            return;
        }

        if (User.isValueExist("phone_number", phoneNumberTf.getText())) {
            AlertTools.AlertError("Error!", "Phone number is already exist!", null);

            setDefaultTf();

            return;
        }

        if (passwordTf.getText().length() < 8) {
            AlertTools.AlertError("Error!", "Password is not valid!", null);

            setDefaultTf();

            return;
        }

        if (User.addLibarian(usernameTf.getText(), passwordTf.getText(), phoneNumberTf.getText())) {
            AlertTools.AlertInformation("Success!", "Libarian is added successfully!", null);

            BackBtnTools.backBtnActionEvent(event);

            setDefaultTf();
        } else {
            AlertTools.AlertError("Error!", "Libarian is not added!", null);

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
