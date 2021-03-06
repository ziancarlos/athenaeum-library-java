package controller;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.BackBtn;
import tools.CurrentUser;
import tools.SwitchSceneTools;
import tools.ValidationTools;

public class LoginController {

    @FXML
    private PasswordField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void loginOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf)) {
            tools.AlertTools.showAlertError("Username/Password text field is empty", "Please fill in all fields");

            settAllTfDefault();

            return;
        }

        User user = null;

        try {
            user = User.getUsernameAndPasswordValid(usernameTf.getText(), passwordTf.getText());
        } catch (SQLException e) {
            tools.AlertTools.showAlertError("Error", "Error while connecting to database");

            settAllTfDefault();

            return;
        }

        if (user == null) {
            tools.AlertTools.showAlertError("Username/Password is invalid", "Please check your username/password");

            settAllTfDefault();

            return;
        }

        CurrentUser.currentUser = user;

        AlertTools.showAlertInformation("Sucessfull!", "You have succesfully login!");

        SwitchSceneTools.changeSceneActionEvent(event, "../view/menu-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");

    }

    private void settAllTfDefault() {
        tools.UiTools.setTextFieldEmpty(usernameTf, passwordTf);
    }

}
