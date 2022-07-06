package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.cj.xdevapi.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Borrowing;
import model.Customer;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.UiTools;
import tools.ValidationTools;

public class BorrowingController {

    @FXML
    private TableColumn<?, ?> booksBorrowedCol;

    @FXML
    private TableColumn<?, ?> borrowingDateCol;

    @FXML
    private TableColumn<?, ?> borrowingStatusCol;

    @FXML
    private TableColumn<?, ?> customerCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TextField searchTf;

    @FXML
    private ComboBox<String> statusCb;

    @FXML
    private TableView<Borrowing> table;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        borrowingDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowingDate"));
        borrowingStatusCol.setCellValueFactory(new PropertyValueFactory<>("borrowingStatus"));
        booksBorrowedCol.setCellValueFactory(new PropertyValueFactory<>("borrowedBooks"));

        setTable();

        setStatusCb();
    }

    private void setStatusCb() {
        statusCb.getItems().addAll("All", "On-going", "Completed");
        statusCb.setValue("All");
    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT borrowings.id, customers.id,  customers.username, customers.password, customers.role, customers.created_at, customers.phone_number,  customers.blacklisted,  COUNT(borrowed_books.borrowing_id) AS borrowed_books, bookkeepings.payment_date, borrowings.status FROM  borrowings LEFT JOIN customers ON customers.id = borrowings.customer_id LEFT JOIN bookkeepings ON bookkeepings.borrowing_id = borrowings.id LEFT JOIN borrowed_books ON borrowed_books.borrowing_id = borrowings.id GROUP BY borrowings.id, customers.id;");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Borrowing(resultSet.getInt("borrowings.id"),
                        new Customer(resultSet.getInt("customers.id"), resultSet.getString("customers.username"),
                                resultSet.getString("customers.password"), resultSet.getString("customers.role"),
                                resultSet.getString("customers.created_at"),
                                resultSet.getString("customers.phone_number"),
                                resultSet.getString("customers.blacklisted")),
                        resultSet.getInt("borrowed_books"), resultSet.getString("bookkeepings.payment_date"),
                        resultSet.getString("borrowings.status")));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void manageBorrowingOnAction(ActionEvent event) {
        Borrowing borrowing = table.getSelectionModel().getSelectedItem();

        if (borrowing == null) {
            AlertTools.showAlertError("No borrowing is being selected!", "Please select a borrowing to manage!");

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/borrowing-manage-page.fxml"));

            Parent root = loader.load();

            BorrowingManageController borrowingManageController = loader.getController();

            borrowingManageController.setBorrowing(borrowing);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        BackBtn.addToBackBtnStack("../view/borrowings-page.fxml");
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        Borrowing borrowing = table.getSelectionModel().getSelectedItem();

        if (borrowing == null) {
            AlertTools.showAlertError("No borrowing is being selected!", "Please select a borrowing to manage!");

            return;
        }

        if (AlertTools.showAlertConfirmationWithOptional("Confirmation!",
                "Make sure all this borrowings books, is already with you!").get() == ButtonType.CANCEL) {
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statementUpdate = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(
                    "SELECT IF( TIMEDIFF(NOW(), ?) < '00:05:00', 'not-late',  'late'  ) AS lateCheck;");

            statement.setString(1, borrowing.getBorrowingDate());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("lateCheck").equals("not-late")) {

                    statement = connection.prepareStatement(
                            "SELECT IF(SUM( IF(borrowed_books.status != 'on-going', 1, 0) ) = 0, 'valid', 'invalid') AS validCheck FROM borrowed_books WHERE borrowing_id = ?");
                    statement.setInt(1, borrowing.getId());
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        if (resultSet.getString("validCheck").equals("valid")) {
                            statement = connection.prepareStatement(
                                    "SELECT * FROM borrowed_books INNER JOIN books ON borrowed_books.book_id = books.id WHERE borrowed_books.borrowing_id = ?");
                            statement.setInt(1, borrowing.getId());

                            resultSet = statement.executeQuery();
                            while (resultSet.next()) {
                                statementUpdate = connection.prepareStatement(
                                        "UPDATE books SET availability = 'available' WHERE id = ?");
                                statementUpdate.setInt(1, resultSet.getInt("books.id"));
                                statementUpdate.executeUpdate();
                            }

                            statementUpdate = connection.prepareStatement(
                                    "DELETE FROM borrowed_books WHERE borrowing_id = ?");
                            statementUpdate.setInt(1, borrowing.getId());

                            affectedRows = statementUpdate.executeUpdate();
                            if (affectedRows > 0) {

                                statementUpdate = connection
                                        .prepareStatement("DELETE FROM bookkeepings WHERE borrowing_id = ?");
                                statementUpdate.setInt(1, borrowing.getId());

                                affectedRows = statementUpdate.executeUpdate();
                                if (affectedRows > 0) {
                                    statementUpdate = connection
                                            .prepareStatement("DELETE FROM borrowings WHERE id = ?");
                                    statementUpdate.setInt(1, borrowing.getId());

                                    affectedRows = statementUpdate.executeUpdate();
                                    if (affectedRows > 0) {
                                        connection.commit();

                                        setTable();

                                        AlertTools.showAlertInformation("Succeed!", "Success Delete this borrowings!");
                                    } else {
                                        connection.rollback();

                                        setTable();

                                        AlertTools.showAlertError("Error!", "Try Again!");

                                    }
                                } else {
                                    connection.rollback();

                                    setTable();

                                    AlertTools.showAlertError("Error!", "Try Again!");
                                }

                            } else {
                                connection.rollback();

                                setTable();

                                AlertTools.showAlertError("Error!", "Try Again!");
                            }

                        } else {
                            connection.rollback();

                            setTable();

                            AlertTools.showAlertError("Error!",
                                    "This borrowings books some/all has been claimed!");

                        }

                    } else {
                        connection.rollback();

                        setTable();

                        AlertTools.showAlertError("Error!",
                                "Try again!");
                    }
                } else {

                    setTable();

                    AlertTools.showAlertError("Error!",
                            "This borrowings is already longer than 5 minute, cant be return!");

                }
            } else {
                connection.rollback();

                setTable();

                AlertTools.showAlertError("Error!",
                        "This borrowings is already longer than 5 minute, cant be return!");
            }

        } catch (Exception e) {
            AlertTools.showAlertInformation("Error!", e.getMessage());

            e.printStackTrace();

            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertInformation("Error!", e1.getMessage());
            }
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (statementUpdate != null)
                    statementUpdate.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception e) {
                AlertTools.showAlertInformation("Error!", e.getMessage());

            }
        }
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        setTable();
    }

    @FXML
    void newBorrowingOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/borrowing-add-page.fxml");

        BackBtn.addToBackBtnStack("../view/borrowings-page.fxml");
    }

    @FXML
    void searchBtn(ActionEvent event) {
        String search = null;
        String sql = "SELECT borrowings.id, customers.id,  customers.username, customers.password, customers.role, customers.created_at, customers.phone_number,  customers.blacklisted,  COUNT(borrowed_books.borrowing_id) AS borrowed_books, bookkeepings.payment_date, borrowings.status FROM  borrowings LEFT JOIN customers ON customers.id = borrowings.customer_id LEFT JOIN bookkeepings ON bookkeepings.borrowing_id = borrowings.id LEFT JOIN borrowed_books ON borrowed_books.borrowing_id = borrowings.id GROUP BY borrowings.id, customers.id";

        if (!ValidationTools.isTextFieldEmptyOrNull(searchTf)) {
            search = searchTf.getText();
        }

        if (statusCb.getSelectionModel().getSelectedItem().equals("On-going")) {
            if (search != null) {
                sql += " HAVING customers.username LIKE '%" + search + "%' AND borrowings.status = 'on-going'";
            } else {
                sql += " HAVING borrowings.status = 'on-going'";
            }
        }
        if (statusCb.getSelectionModel().getSelectedItem().equals("Returned")) {
            if (search != null) {
                sql += " HAVING customers.username LIKE '%" + search + "%' AND borrowings.status = 'returned'";
            } else {
                sql += " HAVING borrowings.status = 'returned'";
            }
        }

        if (statusCb.getSelectionModel().getSelectedItem().equals("Completed")) {
            if (search != null) {
                sql += " HAVING customers.username LIKE '%" + search + "%' AND borrowings.status = 'completed'";
            } else {
                sql += " HAVING borrowings.status = 'completed'";
            }
        }

        if (search != null && statusCb.getSelectionModel().getSelectedItem().equals("All")) {
            sql += " HAVING customers.username LIKE '%" + search + "%'";
        }

        table.getItems().clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Borrowing(resultSet.getInt("borrowings.id"),
                        new Customer(resultSet.getInt("customers.id"), resultSet.getString("customers.username"),
                                resultSet.getString("customers.password"), resultSet.getString("customers.role"),
                                resultSet.getString("customers.created_at"),
                                resultSet.getString("customers.phone_number"),
                                resultSet.getString("customers.blacklisted")),
                        resultSet.getInt("borrowed_books"), resultSet.getString("bookkeepings.payment_date"),
                        resultSet.getString("borrowings.status")));
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }

        statusCb.setValue("All");

        UiTools.setTextFieldEmpty(searchTf);
    }

}
