package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.BorrowingHistoryTemp;
import tools.AlertTools;
import tools.BackBtn;
import tools.CurrentUser;
import tools.DatabaseTools;

public class HistoryController {

    @FXML
    private TableColumn<?, ?> bookNameCol;

    @FXML
    private TableColumn<?, ?> endDateCol;

    @FXML
    private TableColumn<?, ?> startDateCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private TableView<BorrowingHistoryTemp> table;

    public void initialize() {
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("book"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM borrowed_books INNER JOIN books ON borrowed_books.book_id = books.id INNER JOIN bookkeepings ON borrowed_books.borrowing_id = bookkeepings.borrowing_id WHERE borrowed_books.borrowing_customer_id = ?;");
            statement.setInt(1, CurrentUser.currentUser.getId());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                table.getItems().add(
                        new BorrowingHistoryTemp(resultSet.getInt("borrowed_books.borrowing_id"),
                                new Book(resultSet.getInt("books.id"), resultSet.getString("books.name")),
                                resultSet.getString("borrowed_books.end_date"),
                                resultSet.getString("bookkeepings.payment_date"),
                                resultSet.getString("borrowed_books.status"),
                                resultSet.getInt("borrowed_books.price")));
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
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

}
