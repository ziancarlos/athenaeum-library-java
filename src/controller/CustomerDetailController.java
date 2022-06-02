package controller;

import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.User;
import tools.BackBtnTools;

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
    private TableColumn<?, ?> returnedCol;

    @FXML
    private TableView<String> table;

    private LinkedList<String> list = new LinkedList<String>();

    private User user;

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    void setUser(User user) {
        this.user = user;

        setTableView();
    }

    private void setTableView() {
        listView.getItems().clear();
        list.clear();

        list.add("Username : " + user.getUsername());
        list.add("Password : " + user.getPassword());
        list.add("Phone Number : " + user.getPhoneNumber());
        list.add("Role : " + user.getRole());
        list.add("Blacklisted : " + user.getBlacklisted());
        list.add("Created At : " + user.getDate());

        listView.getItems().addAll(list);
    }

}
