package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.User;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.ValidationTools;
import javafx.scene.Node;

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

    @FXML
    void addCustomerBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/customer-add-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/customers-page.fxml");
    }

    @FXML
    void detailCustomerBtn(ActionEvent event) {
        User user = table.getSelectionModel().getSelectedItem();

        if (user != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer-details-page.fxml"));

                Parent root = loader.load();

                CustomerDetailController controller = loader.getController();

                controller.setUser(user);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

                BackBtnTools.addToBackBtnStack("../view/customers-page.fxml");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertTools.AlertConfirmation("Error", "No User Selected!", "Please select valid user!");
        }
    }

    @FXML
    void editCustomerBtn(ActionEvent event) {

        User user = table.getSelectionModel().getSelectedItem();

        if (user != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer-edit-page.fxml"));

                Parent root = loader.load();

                CustomerEditController controller = loader.getController();

                controller.setUser(user);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

                BackBtnTools.addToBackBtnStack("../view/customers-page.fxml");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            AlertTools.AlertConfirmation("Error", "No User Selected!", "Please select valid user!");
        }
    }

}
