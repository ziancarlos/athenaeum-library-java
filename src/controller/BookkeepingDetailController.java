package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.BookManageTemp;
import model.BookkeepingBorrowing;
import model.BookkeepingPenalty;
import model.BookkeepingPurchasing;
import model.Category;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class BookkeepingDetailController {

    @FXML
    private TableColumn<?, ?> amountColBorrowing;

    @FXML
    private TableColumn<?, ?> bookColBorrowing;

    @FXML
    private TableColumn<?, ?> categoryColPurchasing;

    @FXML
    private TableColumn<?, ?> amountColPurchasing;

    @FXML
    private TableColumn<?, ?> dueDateColBorrowing;

    @FXML
    private TableColumn<?, ?> idColPurchasing;

    @FXML
    private ListView<String> lv;

    @FXML
    private TableColumn<?, ?> nameColPurchasing;

    @FXML
    private TableColumn<?, ?> statusColBorrowing;

    @FXML
    private TableView<Book> tablePurchasing;

    @FXML
    private TableView<BookManageTemp> tableBorrowing;

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    void setBookkeepingPenalty(BookkeepingPenalty bookkeepingPenalty) {
        tablePurchasing.setVisible(false);
        tableBorrowing.setVisible(false);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM penalties INNER JOIN books ON penalties.borrowed_book_book_id = books.id INNER JOIN users ON penalties.borrowed_book_borrowing_customer_id = users.id INNER JOIN borrowings ON penalties.borrowed_book_borrowing_id = borrowings.id INNER JOIN borrowed_books ON borrowed_books.borrowing_id = penalties.borrowed_book_borrowing_id   WHERE penalties.id = ? AND penalties.borrowed_book_book_id = borrowed_books.book_id");
            statement.setInt(1, bookkeepingPenalty.getPenaltyId());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lv.getItems().add("Penalty Id : " + resultSet.getInt("penalties.id"));
                lv.getItems().add("Penalty Type : " + resultSet.getString("penalties.penalty_type"));
                lv.getItems().add("Amount : $" + resultSet.getDouble("penalties.amount"));
                lv.getItems().add("Penalty Date : " + resultSet.getString("penalties.penalty_date"));
                lv.getItems().add("");
                lv.getItems().add("Customer Id :  " + resultSet.getInt("users.id"));
                lv.getItems().add("Customer Name : " + resultSet.getString("users.username"));
                lv.getItems().add("Customer Phone Number : " + resultSet.getString("users.phone_number"));
                lv.getItems().add("");
                lv.getItems().add("Book Id : " + resultSet.getInt("books.id"));
                lv.getItems().add("Book Name : " + resultSet.getString("books.name"));
                lv.getItems().add("");
                lv.getItems().addAll("Borrowed Books Status : " + resultSet.getString("borrowed_books.status"));
                lv.getItems().addAll("Borrowed Books End Date : " + resultSet.getString("borrowed_books.end_date"));
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

    void setBookkeepingBorrowing(BookkeepingBorrowing bookkeepingBorrowing) {
        bookColBorrowing.setCellValueFactory(new PropertyValueFactory<>("book"));
        amountColBorrowing.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColBorrowing.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateColBorrowing.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        tablePurchasing.setVisible(false);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM borrowed_books INNER JOIN borrowings ON borrowed_books.borrowing_id = borrowings.id INNER JOIN users ON borrowed_books.borrowing_customer_id = users.id WHERE borrowed_books.borrowing_id = ? ");
            statement.setInt(1, bookkeepingBorrowing.getBorrowingId());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lv.getItems().add("Customer Id :  " + resultSet.getInt("users.id"));
                lv.getItems().add("Customer Name : " + resultSet.getString("users.username"));
                lv.getItems().add("Customer Phone Number : " + resultSet.getString("users.phone_number"));
                lv.getItems().add(" ");
                lv.getItems().add("Borrowings Status : " + resultSet.getString("borrowings.status"));
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

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM borrowed_books INNER JOIN books ON borrowed_books.book_id = books.id  WHERE borrowed_books.borrowing_id = ? ");
            statement.setInt(1, bookkeepingBorrowing.getBorrowingId());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BookManageTemp book = new BookManageTemp(
                        new Book(resultSet.getInt("books.id"), resultSet.getString("books.name")),
                        resultSet.getString("borrowed_books.end_date"),
                        resultSet.getString("borrowed_books.status"),
                        resultSet.getDouble("borrowed_books.price"));

                tableBorrowing.getItems().add(book);
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

    void setBookkeepingPurchasing(BookkeepingPurchasing bookkeepingPurchasing) {
        idColPurchasing.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColPurchasing.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColPurchasing.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColPurchasing.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableBorrowing.setVisible(false);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM purchasings WHERE id = ?");
            preparedStatement.setInt(1, bookkeepingPurchasing.getPurchasingId());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lv.getItems().add("Purchasing Id : " + resultSet.getString("purchasings.id"));
                lv.getItems().add("Purchasing Supplier Name : " + resultSet.getString("purchasings.supplier_name"));
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT books.id, books.name, books.availability, categories.id, categories.name, purchasings_books_details.purchasing_price FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id WHERE purchasings_books_details.purchasing_id = ?");
            preparedStatement.setInt(1, bookkeepingPurchasing.getPurchasingId());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        resultSet.getString("books.availability"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getDouble("purchasings_books_details.purchasing_price"));
                tablePurchasing.getItems().add(book);
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }

    }

    @FXML
    void detailOnAction(ActionEvent event) {

    }

}
