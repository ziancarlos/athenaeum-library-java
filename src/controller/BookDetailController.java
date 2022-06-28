
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.BookBorrowingDetailTemp;
import model.Customer;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class BookDetailController {

    @FXML
    private TableColumn<?, ?> borrowerNameCol;

    @FXML
    private TableColumn<?, ?> dueDateCol;

    @FXML
    private ListView<String> listView;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableView<BookBorrowingDetailTemp> table;

    private Book book;

    public void initialize() {
        borrowerNameCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setLv() {
        listView.getItems().clear();

        listView.getItems().add("Id : " + book.getId());
        listView.getItems().add("Title : " + book.getName());
        listView.getItems().add("Availability : " + book.getAvailability());
        listView.getItems().add("Category : " + book.getCategory().getName());
        listView.getItems().add("Purchase Date : " + book.getPurchaseDate());

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setBook(Book book) {
        this.book = book;

        setLv();

        setTable();
    }

    private void setTable() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM borrowed_books INNER JOIN customers ON borrowed_books.borrowing_customer_id = customers.id WHERE borrowed_books.book_id = ?");
            statement.setInt(1, book.getId());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer(resultSet.getInt("customers.id"),
                        resultSet.getString("customers.username"),
                        resultSet.getString("customers.password"),
                        resultSet.getString("customers.role"),
                        resultSet.getString("customers.created_at"),
                        resultSet.getString("customers.phone_number"),
                        resultSet.getString("customers.blacklisted"));
                BookBorrowingDetailTemp bookBorrowingDetail = new BookBorrowingDetailTemp(customer,
                        resultSet.getString("borrowed_books.end_date"), resultSet.getString("borrowed_books.status"));

                table.getItems().add(bookBorrowingDetail);
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error!", e.getMessage());
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
                AlertTools.showAlertError("Error!", e.getMessage());
            }
        }

    }

}
