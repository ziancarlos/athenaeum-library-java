package controller;

import java.sql.CallableStatement;
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
import model.BookManageTemp;
import model.Borrowing;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class BorrowingManageController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> bookCol;

    @FXML
    private TableColumn<?, ?> dueDateCol;

    @FXML
    private ListView<String> lv;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableView<BookManageTemp> table;

    private Borrowing borrowing;

    public void initialize() {
        bookCol.setCellValueFactory(new PropertyValueFactory<>("book"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void brokenOnAction(ActionEvent event) {
        BookManageTemp book = table.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlertTools.showAlertError("Error", "No Book Selected!");

            return;
        }

        Connection connection = null;
        PreparedStatement statementSelect = null;
        PreparedStatement statementUpdate = null;
        PreparedStatement statementUpdateBookAvail = null;
        PreparedStatement statementInsertPenalties = null;
        PreparedStatement statementSelectBook = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            statementSelectBook = connection.prepareStatement(
                    "SELECT purchasings_books_details.purchasing_price FROM books INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id WHERE books.id = ?");
            statementSelectBook.setInt(1, book.getBook().getId());

            resultSet = statementSelectBook.executeQuery();

            if (resultSet.next()) {
                double bookPrice = resultSet.getDouble("purchasings_books_details.purchasing_price");

                statementSelect = connection.prepareStatement(
                        "SELECT * FROM borrowed_books WHERE borrowing_id = ? AND book_id = ? FOR UPDATE");

                statementSelect.setInt(1, borrowing.getId());
                statementSelect.setInt(2, book.getBook().getId());

                resultSet = statementSelect.executeQuery();
                if (resultSet.next()) {

                    if (resultSet.getString("status").equals("on-going")) {

                        statementUpdate = connection.prepareStatement(
                                "UPDATE borrowed_books SET status = 'broken' WHERE borrowing_id = ? AND book_id = ? ");
                        statementUpdate.setInt(1, borrowing.getId());
                        statementUpdate.setInt(2, book.getBook().getId());

                        affectedRows = statementUpdate.executeUpdate();
                        if (affectedRows > 0) {
                            statementInsertPenalties = connection.prepareStatement(
                                    "INSERT INTO penalties(penalty_type, penalty_date, payment_status, borrowed_book_book_id, borrowed_book_borrowing_customer_id, borrowed_book_borrowing_id, amount) VALUES ('broken', NOW(), 'unpaid', ?, ?, ?, ?)");

                            statementInsertPenalties.setInt(1, book.getBook().getId());
                            statementInsertPenalties.setInt(2, resultSet.getInt("borrowing_customer_id"));
                            statementInsertPenalties.setInt(3, resultSet.getInt("borrowing_id"));
                            statementInsertPenalties.setDouble(4, bookPrice);

                            affectedRows = statementInsertPenalties.executeUpdate();

                            if (affectedRows > 0) {
                                String SQL = "{call checkIfSpecifiedBorrowingIsCompleted (?)}";
                                callableStatement = connection.prepareCall(SQL);
                                callableStatement.setInt(1, borrowing.getId());

                                callableStatement.executeUpdate();

                                connection.commit();

                                AlertTools.showAlertInformation("Success!", "Succeed Claimed Broken Books!");

                                BackBtn.backBtnActionEvent(event);
                            } else {
                                connection.rollback();

                                AlertTools.showAlertError("Error!", "Contact Support!!");
                            }
                        } else {
                            connection.rollback();

                            AlertTools.showAlertError("Error!", "Contact Support!!");
                        }
                    } else {
                        connection.rollback();

                        AlertTools.showAlertError("Error!", "This books have been claimed!");
                    }
                }
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Error!", e1.getMessage());
            }

            e.printStackTrace();

            AlertTools.showAlertError("Error!", e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statementSelect != null) {
                    statementSelect.close();
                }
                if (statementUpdate != null) {
                    statementUpdate.close();
                }
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (statementUpdateBookAvail != null) {
                    statementUpdateBookAvail.close();
                }
                if (statementSelectBook != null) {
                    statementSelectBook.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());
            }

        }
    }

    @FXML
    void lostOnAction(ActionEvent event) {
        BookManageTemp book = table.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlertTools.showAlertError("Error", "No Book Selected!");

            return;
        }

        Connection connection = null;
        PreparedStatement statementSelect = null;
        PreparedStatement statementUpdate = null;
        PreparedStatement statementUpdateBookAvail = null;
        PreparedStatement statementInsertPenalties = null;
        PreparedStatement statementSelectBook = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            statementSelectBook = connection.prepareStatement(
                    "SELECT purchasings_books_details.purchasing_price FROM books INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id WHERE books.id = ?");
            statementSelectBook.setInt(1, book.getBook().getId());

            resultSet = statementSelectBook.executeQuery();

            if (resultSet.next()) {
                double bookPrice = resultSet.getDouble("purchasings_books_details.purchasing_price");

                statementSelect = connection.prepareStatement(
                        "SELECT * FROM borrowed_books WHERE borrowing_id = ? AND book_id = ? FOR UPDATE");

                statementSelect.setInt(1, borrowing.getId());
                statementSelect.setInt(2, book.getBook().getId());

                resultSet = statementSelect.executeQuery();
                if (resultSet.next()) {

                    if (resultSet.getString("status").equals("on-going")) {

                        statementUpdate = connection.prepareStatement(
                                "UPDATE borrowed_books SET status = 'lost' WHERE borrowing_id = ? AND book_id = ? ");
                        statementUpdate.setInt(1, borrowing.getId());
                        statementUpdate.setInt(2, book.getBook().getId());

                        affectedRows = statementUpdate.executeUpdate();
                        if (affectedRows > 0) {
                            statementInsertPenalties = connection.prepareStatement(
                                    "INSERT INTO penalties(penalty_type, penalty_date, payment_status, borrowed_book_book_id, borrowed_book_borrowing_customer_id, borrowed_book_borrowing_id, amount) VALUES ('lost', NOW(), 'unpaid', ?, ?, ?, ?)");

                            statementInsertPenalties.setInt(1, book.getBook().getId());
                            statementInsertPenalties.setInt(2, resultSet.getInt("borrowing_customer_id"));
                            statementInsertPenalties.setInt(3, resultSet.getInt("borrowing_id"));
                            statementInsertPenalties.setDouble(4, bookPrice);

                            affectedRows = statementInsertPenalties.executeUpdate();

                            if (affectedRows > 0) {
                                String SQL = "{call checkIfSpecifiedBorrowingIsCompleted (?)}";
                                callableStatement = connection.prepareCall(SQL);
                                callableStatement.setInt(1, borrowing.getId());

                                callableStatement.executeUpdate();

                                connection.commit();

                                AlertTools.showAlertInformation("Success!", "Succeed Claimed Lost Books!");

                                BackBtn.backBtnActionEvent(event);
                            } else {
                                connection.rollback();

                                AlertTools.showAlertError("Error!", "Contact Support!!");
                            }
                        } else {
                            connection.rollback();

                            AlertTools.showAlertError("Error!", "Contact Support!!");
                        }
                    } else {
                        connection.rollback();

                        AlertTools.showAlertError("Error!", "This books have been claimed!");
                    }
                }
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Error!", e1.getMessage());
            }

            e.printStackTrace();

            AlertTools.showAlertError("Error!", e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statementSelect != null) {
                    statementSelect.close();
                }
                if (statementUpdate != null) {
                    statementUpdate.close();
                }
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (statementUpdateBookAvail != null) {
                    statementUpdateBookAvail.close();
                }
                if (statementSelectBook != null) {
                    statementSelectBook.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());
            }

        }
    }

    @FXML
    void returnedOnAction(ActionEvent event) {
        BookManageTemp book = table.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlertTools.showAlertError("Error", "No Book Selected!");

            return;
        }

        Connection connection = null;
        PreparedStatement statementSelect = null;
        PreparedStatement statementUpdate = null;
        PreparedStatement statementUpdateBookAvail = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);
            statementSelect = connection.prepareStatement(
                    "SELECT * FROM borrowed_books WHERE borrowing_id = ? AND book_id = ? FOR UPDATE");

            statementSelect.setInt(1, borrowing.getId());
            statementSelect.setInt(2, book.getBook().getId());

            resultSet = statementSelect.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("status").equals("on-going")) {
                    statementUpdate = connection.prepareStatement(
                            "UPDATE borrowed_books SET status = 'returned' , returned_date = NOW() WHERE borrowing_id = ? AND book_id = ? ");
                    statementUpdate.setInt(1, borrowing.getId());
                    statementUpdate.setInt(2, book.getBook().getId());

                    affectedRows = statementUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        statementUpdateBookAvail = connection.prepareStatement(
                                "UPDATE books SET availability = 'available' WHERE id = ?");
                        statementUpdateBookAvail.setInt(1, book.getBook().getId());

                        affectedRows = statementUpdateBookAvail.executeUpdate();

                        if (affectedRows > 0) {
                            String SQL = "{call checkIfSpecifiedBorrowingIsCompleted (?)}";
                            callableStatement = connection.prepareCall(SQL);
                            callableStatement.setInt(1, borrowing.getId());

                            callableStatement.executeUpdate();

                            connection.commit();

                            AlertTools.showAlertInformation("Success!", "Succeed Claimed Books!");

                            BackBtn.backBtnActionEvent(event);

                        } else {
                            connection.rollback();

                            AlertTools.showAlertError("Error!", "Update book availability failed!");
                        }

                    } else {
                        connection.rollback();

                        AlertTools.showAlertError("Error!", "Contact Support!!");
                    }
                } else {
                    connection.rollback();

                    AlertTools.showAlertError("Error!", "This books have been claimed!");
                }
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Error!", e1.getMessage());
            }

            e.printStackTrace();

            AlertTools.showAlertError("Error!", e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statementSelect != null) {
                    statementSelect.close();
                }
                if (statementUpdate != null) {
                    statementUpdate.close();
                }
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (statementUpdateBookAvail != null) {
                    statementUpdateBookAvail.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());
            }

        }

    }

    public void setBorrowing(Borrowing borrowing) {
        this.borrowing = borrowing;

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
                    "SELECT * FROM borrowed_books INNER JOIN books ON borrowed_books.book_id = books.id  WHERE borrowed_books.borrowing_id = ? AND borrowed_books.borrowing_customer_id = ?");
            statement.setInt(1, borrowing.getId());
            statement.setInt(2, borrowing.getCustomer().getId());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BookManageTemp book = new BookManageTemp(
                        new Book(resultSet.getInt("books.id"), resultSet.getString("books.name")),
                        resultSet.getString("borrowed_books.end_date"),
                        resultSet.getString("borrowed_books.status"),
                        resultSet.getDouble("borrowed_books.price"));

                table.getItems().add(book);
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

    private void setLv() {
        lv.getItems().add("Id : " + borrowing.getId());
        lv.getItems()
                .add("Customer : " + borrowing.getCustomer().getId() + ". " + borrowing.getCustomer().getUsername());
        lv.getItems().addAll("Borrowed Books : " + borrowing.getBorrowedBooks());
        lv.getItems().addAll("Borrowing Start Date : " + borrowing.getBorrowingDate());
        lv.getItems().add("Borrowing Status : " + borrowing.getBorrowingStatus());
    }

}
