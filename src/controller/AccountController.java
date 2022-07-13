package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Admin;
import model.Customer;
import model.Libarian;
import model.PenaltiesAccountTemp;
import tools.AlertTools;
import tools.BackBtn;
import tools.CurrentUser;
import tools.DatabaseTools;

public class AccountController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private ListView<String> lv;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableView<PenaltiesAccountTemp> table;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private ComboBox<String> typeCb;

    @FXML
    private Button searchBtn;

    public void initialize() {
        if (CurrentUser.currentUser.getRole().equals("admin")) {
            Admin admin = (Admin) CurrentUser.currentUser;
            lv.getItems().add("Id : " + admin.getId());
            lv.getItems().add("Username : " + admin.getUsername());
            lv.getItems().add("Role : " + admin.getRole());

            table.setVisible(false);

            searchBtn.setVisible(false);

            typeCb.setVisible(false);

        } else if (CurrentUser.currentUser.getRole().equals("libarian")) {
            Libarian libarian = (Libarian) CurrentUser.currentUser;
            lv.getItems().add("Id : " + libarian.getId());
            lv.getItems().add("Username : " + libarian.getUsername());
            lv.getItems().add("Role : " + libarian.getRole());
            lv.getItems().add("Active : " + libarian.getActive());

            table.setVisible(false);

            searchBtn.setVisible(false);

            typeCb.setVisible(false);

        } else if (CurrentUser.currentUser.getRole().equals("customer")) {
            Customer customer = (Customer) CurrentUser.currentUser;
            lv.getItems().add("Id : " + customer.getId());
            lv.getItems().add("Username : " + customer.getUsername());
            lv.getItems().add("Role : " + customer.getRole());
            lv.getItems().add("Phone Number : " + customer.getPhoneNumber());
            lv.getItems().add("Blacklisted : " + customer.getBlacklisted());

            setTypeCb();

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                connection = DatabaseTools.getConnection();
                statement = connection.prepareStatement(
                        "SELECT * FROM penalties WHERE borrowed_book_borrowing_customer_id = ?;");
                statement.setInt(1, CurrentUser.currentUser.getId());

                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    PenaltiesAccountTemp penaltiesAccountTemp = new PenaltiesAccountTemp(resultSet.getInt("id"),
                            resultSet.getString("penalty_type"),
                            resultSet.getString("penalty_date"), resultSet.getString("payment_status"),
                            resultSet.getDouble("amount"));

                    table.getItems().add(penaltiesAccountTemp);

                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());

                e.printStackTrace();
            } finally {
                try {
                    if (connection != null)
                        connection.close();
                    if (statement != null)
                        statement.close();
                    if (resultSet != null)
                        resultSet.close();

                } catch (Exception e) {
                    AlertTools.showAlertError("Error!", e.getMessage());

                    e.printStackTrace();
                }
            }
        }

    }

    private void setTypeCb() {
        typeCb.getItems().addAll("paid", "unpaid");
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if (typeCb.getSelectionModel().getSelectedItem() == null) {
            typeCb.getSelectionModel().clearSelection();
            return;
        }
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM penalties WHERE borrowed_book_borrowing_customer_id = ? AND payment_status = ?;");
            statement.setInt(1, CurrentUser.currentUser.getId());
            statement.setString(2, typeCb.getSelectionModel().getSelectedItem());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PenaltiesAccountTemp penaltiesAccountTemp = new PenaltiesAccountTemp(resultSet.getInt("id"),
                        resultSet.getString("penalty_type"),
                        resultSet.getString("penalty_date"), resultSet.getString("payment_status"),
                        resultSet.getDouble("amount"));

                table.getItems().add(penaltiesAccountTemp);

            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error!", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();

            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());

                e.printStackTrace();
            }

            typeCb.getSelectionModel().clearSelection();
        }

    }

}
