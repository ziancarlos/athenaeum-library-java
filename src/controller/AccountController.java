package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import model.Admin;
import model.Customer;
import model.Libarian;
import tools.BackBtn;
import tools.CurrentUser;

public class AccountController {

    @FXML
    private ListView<String> lv;

    @FXML
    private TableView<?> table;

    public void initialize() {
        if (CurrentUser.currentUser.getRole().equals("admin")) {
            Admin admin = (Admin) CurrentUser.currentUser;
            lv.getItems().add("Id : " + admin.getId());
            lv.getItems().add("Username : " + admin.getUsername());
            lv.getItems().add("Role : " + admin.getRole());
        } else if (CurrentUser.currentUser.getRole().equals("libarian")) {
            Libarian libarian = (Libarian) CurrentUser.currentUser;
            lv.getItems().add("Id : " + libarian.getId());
            lv.getItems().add("Username : " + libarian.getUsername());
            lv.getItems().add("Role : " + libarian.getRole());
            lv.getItems().add("Active : " + libarian.getActive());
        } else if (CurrentUser.currentUser.getRole().equals("customer")) {
            Customer customer = (Customer) CurrentUser.currentUser;
            lv.getItems().add("Id : " + customer.getId());
            lv.getItems().add("Username : " + customer.getUsername());
            lv.getItems().add("Role : " + customer.getRole());
            lv.getItems().add("Phone Number : " + customer.getPhoneNumber());
            lv.getItems().add("Blacklisted : " + customer.getBlacklisted());
        }
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

}
