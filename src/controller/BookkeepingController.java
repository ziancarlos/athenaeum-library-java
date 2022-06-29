package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bookkeeping;
import model.BookkeepingBorrowing;
import model.BookkeepingPenalty;
import model.BookkeepingPurchasing;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class BookkeepingController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private DatePicker endDate;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private DatePicker startDate;

    @FXML
    private TableView<Bookkeeping> table;

    @FXML
    private ComboBox<String> typeCb;

    @FXML
    private TableColumn<?, ?> transactionTypeCol;

    @FXML
    private TableColumn<?, ?> doubEntryTypeCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        doubEntryTypeCol.setCellValueFactory(new PropertyValueFactory<>("doubleEntryType"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        setTypeCb();

        setTable();
    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM bookkeepings");

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String doubleEntryType = resultSet.getString("double_entry_type");
                String transactionType = resultSet.getString("transaction_type");
                float amount = resultSet.getFloat("amount");
                String paymentDate = resultSet.getString("payment_date");

                switch (resultSet.getString("transaction_type")) {
                    case "purchasing":
                        table.getItems().add(new BookkeepingPurchasing(id, doubleEntryType, transactionType, amount,
                                paymentDate, resultSet.getInt("purchasing_id")));
                        break;
                    case "fine":
                        table.getItems().add(new BookkeepingPenalty(id, doubleEntryType, transactionType, amount,
                                paymentDate, resultSet.getInt("penalty_id")));
                        break;
                    case "borrowing":
                        table.getItems().add(new BookkeepingBorrowing(id, doubleEntryType, transactionType, amount,
                                paymentDate, resultSet.getInt("borrowing_id")));
                        break;
                }
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

    @FXML
    void detailOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        String startDate = null;
        String endDate = null;
        boolean dateEmpty = false;
        String sql = "SELECT * FROM bookkeepings";

        try {
            startDate = this.startDate.getValue().toString();
            endDate = this.endDate.getValue().toString();
        } catch (Exception e) {

        }

        if (startDate == null && endDate == null) {
            dateEmpty = true;
        }

        table.getItems().clear();

        switch (typeCb.getSelectionModel().getSelectedItem()) {
            case "All":
                if (dateEmpty) {
                    setTable();

                    return;
                }

                sql += " WHERE payment_date between " + startDate + " AND " + endDate;

                break;
            case "Fine":
                if (dateEmpty) {
                    sql += " WHERE transaction_type = 'fine'";

                    break;
                }

                sql += " WHERE transaction_type = 'fine' AND  payment_date between " + startDate + " AND " + endDate;

                break;
            case "Purchasing":
                if (dateEmpty) {
                    sql += " WHERE transaction_type = 'purchasing' ";

                    break;
                }

                sql += " WHERE transaction_type = 'purchasing' AND  payment_date between " + startDate + " AND "
                        + endDate;

                break;
            case "Borrowing":
                if (dateEmpty) {
                    sql += " WHERE transaction_type = 'borrowing' ";

                    break;
                }

                sql += " WHERE transaction_type = 'borrowing' AND  payment_date between " + startDate + " AND "
                        + endDate;

                break;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String doubleEntryType = resultSet.getString("double_entry_type");
                String transactionType = resultSet.getString("transaction_type");
                float amount = resultSet.getFloat("amount");
                String paymentDate = resultSet.getString("payment_date");

                switch (resultSet.getString("transaction_type")) {
                    case "purchasing":
                        table.getItems().add(new BookkeepingPurchasing(id, doubleEntryType, transactionType, amount,
                                paymentDate, resultSet.getInt("purchasing_id")));
                        break;
                    case "fine":
                        table.getItems().add(new BookkeepingPenalty(id, doubleEntryType, transactionType, amount,
                                paymentDate, resultSet.getInt("penalty_id")));
                        break;
                    case "borrowing":
                        table.getItems().add(new BookkeepingBorrowing(id, doubleEntryType, transactionType, amount,
                                paymentDate, resultSet.getInt("borrowing_id")));
                        break;
                }
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

    private void setTypeCb() {
        typeCb.getItems().add("All");
        typeCb.getItems().add("Fine");
        typeCb.getItems().add("Purchasing");
        typeCb.getItems().add("Borrowing");

        typeCb.setValue("All");
    }

}
