package controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.User;
import tools.AlertTools;
import tools.CurrentUser;
import tools.SwitchSceneTools;
import tools.UiTools;
import tools.ValidationTools;

public class LoginController {

    @FXML
    private TextField passwordTf;

    @FXML
    private TextField usernameTf;

    @FXML
    void loginBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(usernameTf, passwordTf)) {
            AlertTools.AlertError("Error!", "Username or password field is blank!", null);

            UiTools.setTextFieldEmpty(usernameTf, passwordTf);

            return;
        }

        User user = null;

        try {
            user = User.validateUser(usernameTf.getText(), passwordTf.getText());
        } catch (SQLException e) {
            AlertTools.AlertError("Error!", "Contact Support!", null);
            e.printStackTrace();
            return;
        }

        if (user != null) {
            CurrentUser.currentUser = user;

            SwitchSceneTools.changeSceneActionEvent(event, "../view/menu-page.fxml");

            return;
        }

        AlertTools.AlertError("Error!", "Username and password dont match!", null);
    }

}
