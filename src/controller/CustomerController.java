package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.User;
import tools.BackBtnTools;

public class CustomerController {

    @FXML
    private TableColumn<User, String> blacklistedCol;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private TableColumn<User, String> phoneNumberCol;

    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    void addCustomerBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void detailCustomerBtn(ActionEvent event) {

    }

    @FXML
    void editCustomerBtn(ActionEvent event) {

    }

    @FXML
    void searchBtn(ActionEvent event) {

    }

    @FXML
    void searchTf(ActionEvent event) {

    }

}
