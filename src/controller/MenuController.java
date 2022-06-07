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
        if (AlertTools.AlertConfirmation("Logout confirmation", "Are you sure? ", null).get() == ButtonType.OK) {
            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

            CurrentUser.currentUser = null;
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

    @FXML
    void booksBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/books-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void purchasingBtn(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("user")) {
            AlertTools.AlertError("Error!", "You dont have permission to access this page!", null);
            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/purchasings-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void categoryBtn(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("user")) {
            AlertTools.AlertError("Error!", "You dont have permission to access this page!", null);
            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/categories-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void libarianBtn(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("user")) {
            AlertTools.AlertError("Error!", "You dont have permission to access this page!", null);
            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/libarians-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/menu-page.fxml");
    }

}
