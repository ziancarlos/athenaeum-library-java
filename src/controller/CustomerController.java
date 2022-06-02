package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.ValidationTools;

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
    private TextField searchTf;

    public void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        blacklistedCol.setCellValueFactory(new PropertyValueFactory<>("blacklisted"));

        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE role = 'user'");

            while (resultSet.next()) {
                table.getItems()
                        .add(new User(resultSet.getInt("id"), resultSet.getString("username"),
                                resultSet.getString("password"), resultSet.getString("role"),
                                resultSet.getString("phone_number"), resultSet.getString("created_at")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void addCustomerBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/customer-add-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/customers-page.fxml");
    }

    @FXML
    void detailCustomerBtn(ActionEvent event) {

    }

    @FXML
    void editCustomerBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/customer-edit-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/customers-page.fxml");
    }

    @FXML
    void searchBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(this.searchTf)) {
            AlertTools.AlertError("Error!", "Search text field is empty!", "Please enter a valid input!");
            return;
        }

        String searchText = this.searchTf.getText();

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM users WHERE role = 'user' AND username LIKE ?");
            statement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();

            table.getItems().clear();

            while (resultSet.next()) {
                table.getItems()
                        .add(new User(resultSet.getInt("id"), resultSet.getString("username"),
                                resultSet.getString("password"), resultSet.getString("role"),
                                resultSet.getString("phone_number"), resultSet.getString("created_at")));
            }

            DatabaseTools.closeQueryOperationWithPreparedStatement(connection, statement, resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

}
