package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Customer;
import tools.BackBtn;

public class CustomerDetailController {

    @FXML
    private TableColumn<?, ?> bookNameCol;

    @FXML
    private TableColumn<?, ?> dueDateCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private ListView<String> listView;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableView<?> table;

    private Customer customer;

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;

        setLv();
    }

    private void setLv() {
        listView.getItems().clear();

        listView.getItems().add("Username : " + customer.getUsername());
        listView.getItems().add("Phone number : " + customer.getPhoneNumber());
        listView.getItems().add("Role : " + customer.getRole());
        listView.getItems().add("Created at : " + customer.getCreatedAt());
        listView.getItems().add("Blacklisted : " + customer.getBlacklisted());

    }

}
