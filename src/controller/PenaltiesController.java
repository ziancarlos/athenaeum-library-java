package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Penalties;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class PenaltiesController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private ComboBox<Customer> customerCb;

    @FXML
    private TableColumn<?, ?> customerCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableView<Penalties> table;

    @FXML
    private TableColumn<?, ?> transactionTypeCol1;

    @FXML
    private ComboBox<String> typeCb;

    @FXML
    private TableColumn<?, ?> typeCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("penaltyType"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        setTable();

        setTypeCb();

        setCustomerCb();
    }

    private void setCustomerCb() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM customers");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customerCb.getItems().add(new Customer(
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

    private void setTypeCb() {
        typeCb.getItems().add("lost");
        typeCb.getItems().add("late");
        typeCb.getItems().add("broken");
    }

    private void setTable() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            table.getItems().clear();

            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM penalties INNER JOIN customers ON penalties.borrowed_book_borrowing_customer_id = customers.id ");

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                table.getItems().add(new Penalties(
                        resultSet.getInt("penalties.id"),
                        resultSet.getString("penalties.penalty_date"),
                        resultSet.getString("penalties.payment_status"),
                        resultSet.getDouble("penalties.amount"),
                        resultSet.getInt("penalties.borrowed_book_book_id"),
                        new Customer(resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("role"),
                                resultSet.getString("created_at"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("blacklisted")),
                        resultSet.getInt("penalties.borrowed_book_borrowing_id"),
                        resultSet.getString("penalties.penalty_type")));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error!", e.getMessage());
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
            }
        }
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if (customerCb.getSelectionModel().getSelectedItem() == null
                && typeCb.getSelectionModel().getSelectedItem() == null) {
            AlertTools.showAlertError("Error!", "Both combobox is empty!");

            setTable();

            setDefaultCb();

            return;
        }

        String sql = "SELECT * FROM penalties INNER JOIN customers ON penalties.borrowed_book_borrowing_customer_id = customers.id  ";

        if (customerCb.getSelectionModel().getSelectedItem() != null
                && typeCb.getSelectionModel().getSelectedItem() == null) {
            sql += "WHERE customers.id = " + customerCb.getSelectionModel().getSelectedItem().getId();
        }

        if (customerCb.getSelectionModel().getSelectedItem() == null
                && typeCb.getSelectionModel().getSelectedItem() != null) {
            sql += "WHERE penalties.penalty_type = '" + typeCb.getSelectionModel().getSelectedItem() + "'";
        }

        if (customerCb.getSelectionModel().getSelectedItem() != null
                && typeCb.getSelectionModel().getSelectedItem() != null) {
            sql += "WHERE penalties.penalty_type = '" + typeCb.getSelectionModel().getSelectedItem()
                    + "'' AND customers.id = " + customerCb.getSelectionModel().getSelectedItem().getId();
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            table.getItems().clear();

            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    sql);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                table.getItems().add(new Penalties(
                        resultSet.getInt("penalties.id"),
                        resultSet.getString("penalties.penalty_date"),
                        resultSet.getString("penalties.payment_status"),
                        resultSet.getDouble("penalties.amount"),
                        resultSet.getInt("penalties.borrowed_book_book_id"),
                        new Customer(resultSet.getInt("id"),
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("role"),
                                resultSet.getString("created_at"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("blacklisted")),
                        resultSet.getInt("penalties.borrowed_book_borrowing_id"),
                        resultSet.getString("penalties.penalty_type")));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error!", e.getMessage());
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
            }
        }

        setDefaultCb();

    }

    private void setDefaultCb() {
        customerCb.getSelectionModel().clearSelection();
        typeCb.getSelectionModel().clearSelection();
    }

    @FXML
    void paidOnAction(ActionEvent event) {
        Penalties penalties = table.getSelectionModel().getSelectedItem();

        if (penalties == null) {
            AlertTools.showAlertError("Error!", "No penalties selected!");

            setTable();

            setDefaultCb();

            return;
        }

        if (AlertTools.showAlertConfirmationWithOptional("Are you sure ?",
                "You want to claimed this penalties as paid ?").get() == ButtonType.CANCEL) {
            setTable();

            setDefaultCb();

            return;
        }

        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            selectStatement = connection.prepareStatement(
                    "SELECT * FROM penalties WHERE id = ? FOR UPDATE");
            selectStatement.setInt(1, penalties.getId());

            resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {

                if (resultSet.getString("payment_status").equals("unpaid")) {

                    updateStatement = connection.prepareStatement(
                            "UPDATE penalties SET payment_status = 'paid' WHERE id = ?");
                    updateStatement.setInt(1, penalties.getId());

                    affectedRows = updateStatement.executeUpdate();

                    if (affectedRows > 0) {
                        insertStatement = connection.prepareStatement(
                                "INSERT INTO bookkeepings(double_entry_type, transaction_type, amount, payment_date, penalty_id) VALUES('debit', 'fine', ?, NOW(), ?)");
                        insertStatement.setDouble(1, resultSet.getDouble("amount"));
                        insertStatement.setInt(2, penalties.getId());

                        affectedRows = insertStatement.executeUpdate();
                        if (affectedRows > 0) {
                            AlertTools.showAlertInformation("Success!", "This fine has been stated paid!");

                            connection.commit();

                        } else {
                            AlertTools.showAlertError("Error!", "Please try again!");

                            connection.rollback();
                        }
                    }

                } else {
                    AlertTools.showAlertError("Error!", "This penalties has been paid!");

                    connection.rollback();
                }
            } else {
                AlertTools.showAlertError("Error!", "Please try again!");

                connection.rollback();
            }

            setTable();

            setDefaultCb();
        } catch (Exception e) {
            AlertTools.showAlertError("Error!", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (selectStatement != null)
                    selectStatement.close();
                if (insertStatement != null)
                    insertStatement.close();
                if (updateStatement != null)
                    updateStatement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());
            }
        }
    }

}
