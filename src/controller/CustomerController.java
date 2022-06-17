package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.UiTools;
import tools.ValidationTools;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

public class CustomerController {

    @FXML
    private TableColumn<?, ?> createdAtCol;

    @FXML
    private TableColumn<?, ?> blacklistedCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> phoneNumberCol;

    @FXML
    private TextField searchTf;

    @FXML
    private TableView<Customer> table;

    @FXML
    private TableColumn<?, ?> usernameCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        blacklistedCol.setCellValueFactory(new PropertyValueFactory<>("blacklisted"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        setTable();

        System.out.println("halo");
    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM customers");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("created_at"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("blacklisted")));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }

    @FXML
    void addOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/customer-add-page.fxml");

        BackBtn.addToBackBtnStack("../view/customers-page.fxml");
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void detailOnAction(ActionEvent event) {
        Customer customer = table.getSelectionModel().getSelectedItem();

        if (customer == null) {
            AlertTools.showAlertError("Error", "Please select a customer!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer-details-page.fxml"));

            Parent root = loader.load();

            BackBtn.addToBackBtnStack("../view/customers-page.fxml");

            CustomerDetailController controller = loader.getController();

            controller.setCustomer(customer);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void editOnAction(ActionEvent event) {
        Customer customer = table.getSelectionModel().getSelectedItem();

        if (customer == null) {
            AlertTools.showAlertError("Error", "Please select a customer!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer-edit-page.fxml"));

            Parent root = loader.load();

            BackBtn.addToBackBtnStack("../view/customers-page.fxml");

            CustomerEditController customerEditController = loader.getController();

            customerEditController.setCustomer(customer);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        setTable();
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)) {
            AlertTools.showAlertError("Error", "Search field is empty");

            setTable();

            return;
        }

        table.getItems().clear();

        Connection connection = DatabaseTools.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM customers WHERE username LIKE ? ");
            preparedStatement.setString(1, "%" + searchTf.getText() + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("created_at"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("blacklisted")));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }

        UiTools.setTextFieldEmpty(searchTf);
    }

}
