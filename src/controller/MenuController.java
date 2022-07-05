package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.Customer;
import tools.AlertTools;
import tools.BackBtn;
import tools.CurrentUser;
import tools.Procedure;
import tools.SwitchSceneTools;

public class MenuController {

    @FXML
    private Button book;

    @FXML
    private Button bookkeeping;

    @FXML
    private Button category;
    @FXML
    private Button borrowing;

    @FXML
    private Button customer;

    @FXML
    private Button history;

    @FXML
    private Button libarian;

    @FXML
    private Button penalties;

    @FXML
    private Button purchasing;

    public void initialize() {
        Procedure.fineAllLateBorrowings();

        if (CurrentUser.currentUser.getRole().equals("customer")) {
            penalties.setVisible(false);
            borrowing.setVisible(false);
            purchasing.setVisible(false);
            bookkeeping.setVisible(false);
            customer.setVisible(false);
            category.setVisible(false);
            libarian.setVisible(false);

            Customer customer = (Customer) CurrentUser.currentUser;
            if (customer.getBlacklisted().equals("blacklisted")) {
                AlertTools.showAlertError("Your account is blacklisted !",
                        "Please contact libarians for more information");
            }
        }

        if (CurrentUser.currentUser.getRole().equals("libarian")) {
            history.setVisible(false);
            libarian.setVisible(false);

            borrowing.setLayoutX(29);
            borrowing.setLayoutY(98);

            purchasing.setLayoutX(29);
            purchasing.setLayoutY(138);

            bookkeeping.setLayoutX(29);
            bookkeeping.setLayoutY(178);

            customer.setLayoutX(29);
            customer.setLayoutY(218);

            category.setLayoutX(29);
            category.setLayoutY(258);

        }

        if (CurrentUser.currentUser.getRole().equals("admin")) {
            history.setVisible(false);

            penalties.setLayoutX(29);
            penalties.setLayoutY(98);

            borrowing.setLayoutX(29);
            borrowing.setLayoutY(138);

            purchasing.setLayoutX(29);
            purchasing.setLayoutY(178);

            bookkeeping.setLayoutX(29);
            bookkeeping.setLayoutY(218);

            customer.setLayoutX(29);
            customer.setLayoutY(258);

            category.setLayoutX(29);
            category.setLayoutY(298);

            libarian.setLayoutX(29);
            libarian.setLayoutY(338);
        }
    }

    @FXML
    void accountOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/account-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void bookkeepingOnAction(ActionEvent event) {

        SwitchSceneTools.changeSceneActionEvent(event, "../view/bookkeepings-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void booksOnAction(ActionEvent event) {

        SwitchSceneTools.changeSceneActionEvent(event, "../view/books-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void borrowingOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/borrowings-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void categoryOnAction(ActionEvent event) {
        if (CurrentUser.currentUser == null) {
            tools.AlertTools.showAlertError("Error", "You must be logged in to access this page");

            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

            BackBtn.clearBackBtnStack();

            return;
        }

        if (CurrentUser.currentUser.getRole().equals("customer")) {
            tools.AlertTools.showAlertError("Error", "You must be logged in as libarian to access this page");

            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

            BackBtn.clearBackBtnStack();

            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/categories-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");

    }

    @FXML
    void customerOnAction(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("customer")) {
            tools.AlertTools.showAlertError("Error", "You must be logged in as libarian to access this page");

            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

            BackBtn.clearBackBtnStack();

            return;
        }
        SwitchSceneTools.changeSceneActionEvent(event, "../view/customers-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void historyOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/history-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void libarianOnAction(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("customer")
                || CurrentUser.currentUser.getRole().equals("libarian")) {
            tools.AlertTools.showAlertError("Error", "You must be logged in as libarian to access this page");

            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

            BackBtn.clearBackBtnStack();

            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/libarians-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void penaltiesOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/penalties-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");

    }

    @FXML
    void purchasingOnAction(ActionEvent event) {
        if (CurrentUser.currentUser.getRole().equals("customer")) {
            tools.AlertTools.showAlertError("Error", "You must be logged in as libarian to access this page");

            SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

            BackBtn.clearBackBtnStack();

            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/purchasings-page.fxml");

        BackBtn.addToBackBtnStack("../view/menu-page.fxml");
    }

    @FXML
    void logoutOnAction(ActionEvent event) {
        if (AlertTools.showAlertConfirmationWithOptional("Logout Confirmation.", "Are you sure you want to logout?")
                .get() == ButtonType.CANCEL) {
            return;
        }

        SwitchSceneTools.changeSceneActionEvent(event, "../view/login-page.fxml");

        CurrentUser.currentUser = null;

        BackBtn.clearBackBtnStack();

        AlertTools.showAlertInformation("Logout!", "Succesfully logout!");
    }

}
