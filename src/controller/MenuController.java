package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.CurrentUser;
import tools.SwitchSceneTools;

public class MenuController {

    @FXML
    void logoutBtn(ActionEvent event) {
        CurrentUser.currentUser = null;

        if (AlertTools.AlertConfirmation("Logout onfirmation", "Are you sure? ", null).get() == ButtonType.OK) {
            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");
        }

    }

    @FXML
    void customerBtn(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("user")) {
            AlertTools.AlertError("Error!", "You dont have permission to access this page!", null);
            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/customers-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/menu-page.fxml");

    }

}
