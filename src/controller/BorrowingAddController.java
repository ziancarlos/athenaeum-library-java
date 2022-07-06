package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Book;

import model.BookAddTemp;
import model.Category;
import model.Customer;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class BorrowingAddController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private ComboBox<Book> bookCb;

    @FXML
    private TableColumn<?, ?> bookNameCol;

    @FXML
    private ComboBox<String> borrowingPeriodCb;

    @FXML
    private ComboBox<Customer> customerCb;

    @FXML
    private TableColumn<?, ?> dueDateCol;

    @FXML
    private TableColumn<?, ?> periodCol;

    @FXML
    private TableView<BookAddTemp> table;

    @FXML
    private Text totalText;

    private ArrayList<BookAddTemp> bookAddTemps = new ArrayList<>();

    private ArrayList<Book> books = new ArrayList<>();

    private double total;

    public void initialize() {
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("book"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        periodCol.setCellValueFactory(new PropertyValueFactory<>("period"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        setCustomerCb();

        setBookNameCb();

        setBorrowingPeriodCb();
    }

    @FXML
    void addBtn(ActionEvent event) {
        if (bookCb.getSelectionModel().getSelectedItem() == null
                || borrowingPeriodCb.getSelectionModel().getSelectedItem() == null) {
            AlertTools.showAlertError("Please fill all the fields", "Error!");

            setDefaultAdd();

            return;
        }

        Book book = null;
        String dueDate = null;
        Integer period = null;
        Double amount = null;

        try {
            book = bookCb.getSelectionModel().getSelectedItem();
            period = Integer.parseInt(borrowingPeriodCb.getSelectionModel().getSelectedItem());
            amount = (double) period * 5;

            // create old date in string format
            String dateBefore = new Date(System.currentTimeMillis()).toString();

            // create instance of the SimpleDateFormat that matches the given date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // create instance of the Calendar class and set the date to the given date
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(dateBefore));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // use add() method to add the days to the given date
            cal.add(Calendar.DATE, period);
            String dateAfter = sdf.format(cal.getTime());

            dueDate = dateAfter;

        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());

            return;
        }

        BookAddTemp bookAddTemp = new BookAddTemp(book, dueDate, period, amount);

        this.bookAddTemps.add(bookAddTemp);

        books.remove(book);

        bookCb.getItems().clear();

        bookCb.getItems().addAll(books);

        setTable();

        setDefaultAdd();

        setTotal();

        AlertTools.showAlertInformation("Success", "Book added successfully!");

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        BookAddTemp bookAddTemp = table.getSelectionModel().getSelectedItem();

        if (bookAddTemp == null) {
            AlertTools.showAlertError("Please select a book to delete", "Error!");

            return;
        }

        bookAddTemps.remove(bookAddTemp);

        table.getItems().addAll(bookAddTemps);

        books.add(bookAddTemp.getBook());

        bookCb.getItems().clear();

        bookCb.getItems().addAll(books);

        setTable();

        setDefaultAdd();

        setTotal();

        AlertTools.showAlertInformation("Success", "Book deleted successfully!");
    }

    @FXML
    void paidOnAction(ActionEvent event) {
        if (customerCb.getSelectionModel().getSelectedItem() == null) {
            AlertTools.showAlertError("Error!", "customer is not stated!");

            return;
        }

        if (bookAddTemps.isEmpty()) {
            AlertTools.showAlertError("Error!", "No book added!");

            return;
        }

        if (AlertTools.showAlertConfirmationWithOptional("Are you sure ?",
                "You want to borrow this books to " + customerCb.getSelectionModel().getSelectedItem().getUsername())
                .get() == ButtonType.CANCEL) {

            return;
        }

        Connection connection = null;
        PreparedStatement statementCheckBooksAvail = null;
        PreparedStatement statementCheckCustomerBorrowings = null;
        PreparedStatement statementBorrowings = null;
        PreparedStatement statementInsertBorrowedBooks = null;
        PreparedStatement statementInsertBookkeepings = null;
        PreparedStatement statementUpdateBook = null;
        PreparedStatement statementCheckBlacklisted = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);
            boolean booksAvail = false;

            for (BookAddTemp bookAddTemp : bookAddTemps) {
                statementCheckBooksAvail = connection.prepareStatement(
                        "SELECT * FROM books WHERE id = ? FOR UPDATE");
                statementCheckBooksAvail.setInt(1, bookAddTemp.getBook().getId());

                resultSet = statementCheckBooksAvail.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getString("books.availability").equals("available")) {

                        booksAvail = true;

                    } else {
                        connection.rollback();

                        booksAvail = false;

                        allDefault();

                        AlertTools.showAlertError("One of books you want to borrow not availabe!",
                                "Please choose another one!");

                        break;
                    }
                } else {
                    connection.rollback();

                    booksAvail = false;

                    allDefault();

                    AlertTools.showAlertError("Error!",
                            "Books not found!");

                    break;
                }
            }

            if (booksAvail) {
                statementCheckBlacklisted = connection.prepareStatement(
                        "SELECT users.id, users.username, users.password, users.role, users.phone_number, users.created_at,  IF(SUM(IF(penalties.payment_status = 'unpaid', 1, 0)) = 0, 'not-blacklisted', 'blacklisted' ) AS blacklisted FROM users  LEFT JOIN  penalties ON penalties.borrowed_book_borrowing_customer_id = users.id GROUP BY users.id HAVING users.role = 'customer' AND blacklisted = 'not-blacklisted' AND users.id = ?");
                statementCheckBlacklisted.setInt(1, customerCb.getSelectionModel().getSelectedItem().getId());

                resultSet = statementCheckBlacklisted.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getString("blacklisted").equals("not-blacklisted")) {

                        statementCheckCustomerBorrowings = connection.prepareStatement(
                                "SELECT * FROM borrowings WHERE customer_id = ? AND status = 'on-going'");
                        statementCheckCustomerBorrowings.setInt(1,
                                customerCb.getSelectionModel().getSelectedItem().getId());

                        resultSet = statementCheckCustomerBorrowings.executeQuery();
                        if (!resultSet.next()) {
                            statementBorrowings = connection
                                    .prepareStatement("INSERT INTO borrowings(customer_id) VALUES(?) ",
                                            PreparedStatement.RETURN_GENERATED_KEYS);
                            statementBorrowings.setInt(1, customerCb.getSelectionModel().getSelectedItem().getId());

                            affectedRows = statementBorrowings.executeUpdate();
                            if (affectedRows > 0) {
                                resultSet = statementBorrowings.getGeneratedKeys();
                                resultSet.next();

                                int borrowingId = resultSet.getInt(1);

                                statementInsertBookkeepings = connection.prepareStatement(
                                        "INSERT INTO borrowings_bookkeepings(double_entry_type, transaction_type, amount, payment_date, borrowing_id) VALUES('debit', 'borrowing', ?, NOW(), ?)");
                                statementInsertBookkeepings.setDouble(1, total);
                                statementInsertBookkeepings.setInt(2, borrowingId);

                                affectedRows = statementInsertBookkeepings.executeUpdate();
                                if (affectedRows > 0) {
                                    for (BookAddTemp bookAddTemp : bookAddTemps) {
                                        statementInsertBorrowedBooks = connection.prepareStatement(
                                                "INSERT INTO borrowed_books (borrowing_id, borrowing_customer_id, book_id, price, status, end_date) VALUES(?, ?, ?, ?, ?, ?);");
                                        statementInsertBorrowedBooks.setInt(1, borrowingId);
                                        statementInsertBorrowedBooks.setInt(2,
                                                customerCb.getSelectionModel().getSelectedItem().getId());
                                        statementInsertBorrowedBooks.setInt(3, bookAddTemp.getBook().getId());
                                        statementInsertBorrowedBooks.setDouble(4, bookAddTemp.getAmount());
                                        statementInsertBorrowedBooks.setString(5, "on-going");
                                        statementInsertBorrowedBooks.setString(6, bookAddTemp.getDueDate());

                                        statementInsertBorrowedBooks.executeUpdate();
                                    }

                                    for (BookAddTemp bookAddTemp : bookAddTemps) {
                                        statementUpdateBook = connection
                                                .prepareStatement(
                                                        "UPDATE books SET availability = 'unavailable' WHERE id = ?");
                                        statementUpdateBook.setInt(1, bookAddTemp.getBook().getId());
                                        statementUpdateBook.executeUpdate();
                                    }

                                    connection.commit();

                                    BackBtn.backBtnActionEvent(event);

                                } else {
                                    connection.rollback();

                                    allDefault();

                                    AlertTools.showAlertError("Insert bookkeepings failed!",
                                            "Try again/Contact support!");
                                }
                            } else {
                                connection.rollback();

                                allDefault();

                                AlertTools.showAlertError("Insert borrowings failed!",
                                        "Try again!");
                            }

                        } else {
                            connection.rollback();

                            allDefault();

                            AlertTools.showAlertError("Customer you selected has a on-going borrowing transaction!",
                                    "Please try again!");

                        }
                    } else {
                        connection.rollback();

                        allDefault();

                        AlertTools.showAlertError("Customer you selected is blacklisted!",
                                "Please try again!");

                    }

                } else {
                    connection.rollback();

                    allDefault();

                    AlertTools.showAlertError("Error!",
                            "Please try again!");
                }

            } else {
                connection.rollback();

                allDefault();

                AlertTools.showAlertError("One of books you want to borrow not availabe/not found!",
                        "Please choose another one!");
            }

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Error", e1.getMessage());
            }
            AlertTools.showAlertError("Error", e.getMessage());

            e.printStackTrace();

            allDefault();

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statementBorrowings != null) {
                    statementBorrowings.close();
                }
                if (statementCheckBooksAvail != null) {
                    statementCheckBooksAvail.close();
                }
                if (statementCheckCustomerBorrowings != null) {
                    statementCheckCustomerBorrowings.close();
                }
                if (statementInsertBookkeepings != null) {
                    statementInsertBookkeepings.close();
                }
                if (statementInsertBorrowedBooks != null) {
                    statementInsertBorrowedBooks.close();
                }
                if (statementUpdateBook != null) {
                    statementUpdateBook.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }
    }

    private void allDefault() {
        customerCb.getSelectionModel().clearSelection();
        setDefaultAdd();

        setBookNameCb();

        setCustomerCb();

        bookAddTemps.clear();

        setTable();
    }

    private void setTable() {
        table.getItems().clear();
        table.getItems().addAll(bookAddTemps);
    }

    private void setDefaultAdd() {
        bookCb.getSelectionModel().clearSelection();
        borrowingPeriodCb.getSelectionModel().clearSelection();
    }

    private void setBorrowingPeriodCb() {
        borrowingPeriodCb.getItems().addAll("1", "2", "3", "4", "5");
    }

    private void setBookNameCb() {
        bookCb.getItems().clear();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT books.id, books.name, categories.id, categories.name FROM books INNER JOIN categories ON books.category_id = categories.id WHERE availability = 'available'";

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name"))));
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

            bookCb.getItems().addAll(books);

        }
    }

    private void setTotal() {
        total = 0;

        for (BookAddTemp bookAddTemp : bookAddTemps) {
            total += bookAddTemp.getAmount();
        }

        totalText.setText("$ " + total);
    }

    private void setCustomerCb() {
        customerCb.getItems().clear();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT users.id, users.username, users.password, users.role, users.phone_number, users.created_at, IF(count(penalties.payment_status = 'unpaid') > 0, 'blacklisted', 'not-blacklisted') AS blacklisted FROM users  LEFT JOIN  penalties ON penalties.borrowed_book_borrowing_customer_id = users.id GROUP BY users.id HAVING users.role = 'customer' AND blacklisted = 'not-blacklisted'");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                customerCb.getItems().add(new Customer(
                        resultSet.getInt("users.id"),
                        resultSet.getString("users.username"),
                        resultSet.getString("users.password"),
                        resultSet.getString("users.role"),
                        resultSet.getString("users.created_at"),
                        resultSet.getString("users.phone_number"),
                        resultSet.getString("blacklisted")));
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

}
