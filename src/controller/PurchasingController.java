package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.PurchasingDetail;
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

public class PurchasingController {

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> purchasingDate;

    @FXML
    private TextField searchTf;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TableColumn<?, ?> supplierNameCol;

    @FXML
    private TableView<PurchasingDetail> table;

    @FXML
    private TableColumn<?, ?> totalAmountCol;

    @FXML
    private TableColumn<?, ?> totalBooksBoughtCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasingDate.setCellValueFactory(new PropertyValueFactory<>("purchasingDate"));
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalBooksBoughtCol.setCellValueFactory(new PropertyValueFactory<>("totalBooks"));

        setTable();
    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement("SELECT * FROM purchasings_details ");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PurchasingDetail purchasingDetail = new PurchasingDetail(resultSet.getInt("id"),
                        resultSet.getString("supplier_name"), resultSet.getInt("total_books"),
                        resultSet.getDouble("total_amount"), resultSet.getString("payment_date"));
                table.getItems().add(purchasingDetail);
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void newPurchasingOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/purchasing-new-page.fxml");

        BackBtn.addToBackBtnStack("../view/purchasings-page.fxml");
    }

    @FXML
    void purchasingDetailOnAction(ActionEvent event) {
        PurchasingDetail purchasingDetail = table.getSelectionModel().getSelectedItem();

        if (purchasingDetail == null) {
            AlertTools.showAlertError("Please select a purchasing!", "Select a purchasing!");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/purchasing-detail-page.fxml"));

            Parent root = loader.load();

            PurchasingDetailController purchasingDetailDetailController = loader.getController();

            purchasingDetailDetailController.setPurchasingDetail(purchasingDetail);

            BackBtn.addToBackBtnStack("../view/purchasings-page.fxml");

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

        setSearchFormEmpty();
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if ((startDatePicker.getValue() == null || endDatePicker.getValue() == null)
                && ValidationTools.isTextFieldEmptyOrNull(searchTf)) {
            AlertTools.showAlertError("Date picker and search text field is empty!", "Please fill in all fields");

            setSearchFormEmpty();

            return;
        }

        String sql = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && (startDatePicker.getValue() != null && endDatePicker.getValue() != null)) {
            sql = "SELECT * FROM purchasings_details WHERE payment_date BETWEEN '" + startDatePicker.getValue()
                    + "' AND '" + endDatePicker.getValue() + "'";
        }

        if (!ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && (startDatePicker.getValue() == null && endDatePicker.getValue() == null)) {
            sql = "SELECT * FROM purchasings_details WHERE supplier_name LIKE   '%" + searchTf.getText() + "%'";
        }

        if (!ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && (startDatePicker.getValue() != null && endDatePicker.getValue() != null)) {
            sql = "SELECT * FROM purchasings_details WHERE payment_date  supplier_name LIKE ? AND payment_date BETWEEN '"
                    + startDatePicker.getValue() + "' AND '" + endDatePicker.getValue() + "'";
        }

        if (sql == null) {
            setTable();

            setSearchFormEmpty();

            return;
        }

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            table.getItems().clear();

            while (resultSet.next()) {
                PurchasingDetail purchasingDetail = new PurchasingDetail(resultSet.getInt("id"),
                        resultSet.getString("supplier_name"), resultSet.getInt("total_books"),
                        resultSet.getDouble("total_amount"), resultSet.getString("payment_date"));
                table.getItems().add(purchasingDetail);
            }
        } catch (Exception e) {

            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }

        setSearchFormEmpty();
    }

    private void setSearchFormEmpty() {
        UiTools.setTextFieldEmpty(searchTf);
        UiTools.setDatePickerNull(startDatePicker, endDatePicker);
    }

}
