package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import tools.UiTools;
import tools.ValidationTools;

public class LibarianController {

    @FXML
    private TableColumn<User, String> activeCol;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    @FXML
    private TextField searchTf;

    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<?, String> usernameCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));

        refreshTable();
    }

    @FXML
    void addBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/libarian-add-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/libarians-page.fxml");
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void deactivateBtn(ActionEvent event) {
        User user = table.getSelectionModel().getSelectedItem();

        if (user == null) {
            AlertTools.AlertError("Error!", "No user is selected!", null);
            return;
        }

        if (user.getActive() == 0) {
            AlertTools.AlertError("Error!", "This user is already deactivate!", null);
            return;
        }

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE users SET active = 0 WHERE id = ?");
            preparedStatement.setInt(1, user.getId());
            int affecedRows = preparedStatement.executeUpdate();

            if (affecedRows > 0) {
                user.setActive(0);

                refreshTable();

                AlertTools.AlertInformation("Success!", "This user is deactivate already!", null);
            } else {
                AlertTools.AlertError("Error!", "This user is not active!", null);
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    @FXML
    void activateBtn(ActionEvent event) {
        User user = table.getSelectionModel().getSelectedItem();

        if (user == null) {
            AlertTools.AlertError("Error!", "No user is selected!", null);
            return;
        }

        if (user.getActive() == 1) {
            AlertTools.AlertError("Error!", "This user is already active!", null);
            return;
        }

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE users SET active = 1 WHERE id = ?");
            preparedStatement.setInt(1, user.getId());
            int affecedRows = preparedStatement.executeUpdate();

            if (affecedRows > 0) {
                user.setActive(1);

                refreshTable();

                AlertTools.AlertInformation("Success!", "This user is activated already!", null);
            } else {
                AlertTools.AlertError("Error!", "This user is not deactive!", null);
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    @FXML
    void searchBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)) {
            AlertTools.AlertError("Error", "Search field is empty", "Error!");

            return;
        }

        table.getItems().clear();

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM users WHERE role = 'libarian' AND username LIKE ?");
            statement.setString(1, "%" + searchTf.getText() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("password"), resultSet.getString("role"),
                        resultSet.getString("phone_number"), resultSet.getString("created_at"));
                user.setActive(resultSet.getInt("active"));

                table.getItems().add(user);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        }

        UiTools.setTextFieldEmpty(searchTf);
    }

    @FXML
    void editBtn(ActionEvent event) {
        User user = table.getSelectionModel().getSelectedItem();

        if (user == null) {
            AlertTools.AlertError("Error!", "No user is selected!", null);
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/libarian-edit-page.fxml"));

            Parent root = loader.load();

            LibarianEditController libarianEditController = loader.getController();

            libarianEditController.setUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtnTools.addToBackBtnStack("../view/libarians-page.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void refreshTable() {
        table.getItems().clear();

        UiTools.setTextFieldEmpty(searchTf);
        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM users WHERE role = 'libarian'");
            while (resultSet.next()) {
                User user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("password"), resultSet.getString("role"),
                        resultSet.getString("phone_number"), resultSet.getString("created_at"));
                user.setActive(resultSet.getInt("active"));

                table.getItems().add(user);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshBtn(ActionEvent event) {
        refreshTable();
    }

}
