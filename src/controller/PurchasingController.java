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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Purchasing;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.UiTools;
import tools.ValidationTools;

public class PurchasingController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableColumn<Purchasing, Integer> idCol;

    @FXML
    private TableColumn<Purchasing, String> purchasingDate;

    @FXML
    private TableColumn<Purchasing, String> supplierNameCol;

    @FXML
    private TableView<Purchasing> table;

    @FXML
    private TableColumn<Purchasing, Double> totalAmountCol;

    @FXML
    private TableColumn<Purchasing, String> totalBooksBoughtCol;

    @FXML
    private TextField searchTf;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasingDate.setCellValueFactory(new PropertyValueFactory<>("purchasingDate"));
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalBooksBoughtCol.setCellValueFactory(new PropertyValueFactory<>("totalBooksBought"));

        refreshTable();
    }

    @FXML
    void refreshBtn(ActionEvent event) {
        refreshTable();

        setSearchForm();
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void newPurchasingBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/purchasing-new-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/purchasings-page.fxml");
    }

    @FXML
    void purchasingDetailBtn(ActionEvent event) {
        Purchasing purchasing = table.getSelectionModel().getSelectedItem();

        if (purchasing == null) {
            AlertTools.AlertError("Error!", "Please select a purchasing!", "Error");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/purchasing-detail-page.fxml"));

            Parent root = loader.load();

            PurchasingDetailController controller = loader.getController();

            controller.setPurchasing(purchasing);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtnTools.addToBackBtnStack("../view/purchasings-page.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void searchBtn(ActionEvent event) {
        String dateStart = null;
        String dateEnd = null;

        try {
            dateStart = this.startDatePicker.getValue().toString();
            dateEnd = this.endDatePicker.getValue().toString();
        } catch (Exception e) {

        }

        if (ValidationTools.isTextFieldEmptyOrNull(searchTf) && (dateStart == null && dateEnd == null)) {
            AlertTools.AlertError("Error!", "Search text field and date picker is empty!", "Please enter a value!");

            return;
        }

        table.getItems().clear();

        if (ValidationTools.isTextFieldEmptyOrNull(searchTf) && (dateStart != null && dateEnd != null)) {

            try {
                Connection connection = DatabaseTools.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id HAVING purchasings.date BETWEEN ? AND ? ");
                statement.setString(1, dateStart);
                statement.setString(2, dateEnd);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"),
                            resultSet.getString("purchasings.date"),
                            resultSet.getString("purchasings.supplier_name"), resultSet.getInt("totalBooksBought"),
                            resultSet.getDouble("purchasings.amount"));
                    table.getItems().add(purchasing);
                }

                DatabaseTools.closeQueryOperation(connection, statement, resultSet);
            } catch (Exception e) {
                AlertTools.AlertErrorContactSupport();
            }

        } else if (!ValidationTools.isTextFieldEmptyOrNull(searchTf) && (dateStart == null && dateEnd == null)) {
            try {
                Connection connection = DatabaseTools.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id HAVING purchasings.supplier_name LIKE ? ");
                statement.setString(1, "%" + searchTf.getText() + "%");

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"),
                            resultSet.getString("purchasings.date"),
                            resultSet.getString("purchasings.supplier_name"), resultSet.getInt("totalBooksBought"),
                            resultSet.getDouble("purchasings.amount"));
                    table.getItems().add(purchasing);
                }

                DatabaseTools.closeQueryOperation(connection, statement, resultSet);
            } catch (Exception e) {
                AlertTools.AlertErrorContactSupport();
            }
        } else {
            try {
                Connection connection = DatabaseTools.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id HAVING purchasings.supplier_name LIKE ? AND purchasings.date BETWEEN ? AND ? ");
                statement.setString(1, "%" + searchTf.getText() + "%");
                statement.setString(2, dateStart);
                statement.setString(3, dateEnd);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"),
                            resultSet.getString("purchasings.date"),
                            resultSet.getString("purchasings.supplier_name"), resultSet.getInt("totalBooksBought"),
                            resultSet.getDouble("purchasings.amount"));
                    table.getItems().add(purchasing);
                }

                DatabaseTools.closeQueryOperation(connection, statement, resultSet);
            } catch (Exception e) {
                AlertTools.AlertErrorContactSupport();
            }
        }

        setSearchForm();

    }

    private void refreshTable() {
        table.getItems().clear();
        setSearchForm();

        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id");

            while (resultSet.next()) {
                Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"),
                        resultSet.getString("purchasings.date"),
                        resultSet.getString("purchasings.supplier_name"), resultSet.getInt("totalBooksBought"),
                        resultSet.getDouble("purchasings.amount"));
                table.getItems().add(purchasing);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    private void setSearchForm() {
        UiTools.setTextFieldEmpty(searchTf);
        this.startDatePicker.setValue(null);
        this.endDatePicker.setValue(null);
    }

}
