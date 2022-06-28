package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.Category;
import tools.AlertTools;
import tools.BackBtn;
import tools.CurrentUser;
import tools.DatabaseTools;
import tools.UiTools;
import tools.ValidationTools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class BookController {

    @FXML
    private TableColumn<?, ?> availCol;

    @FXML
    private ComboBox<Category> categoryCb;

    @FXML
    private TableColumn<?, ?> categoryCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> purchaseDateCol;

    @FXML
    private TextField searchTf;

    @FXML
    private Button editBtn;

    @FXML
    private Button detailsBtn;

    @FXML
    private TableView<Book> table;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

        setTable();

        setCategoryCb();

        setPermission();
    }

    private void setPermission() {
        if (CurrentUser.currentUser.getRole().equals("customer")) {
            editBtn.setVisible(false);

            detailsBtn.setLayoutX(22);
            detailsBtn.setLayoutY(336);
        }
    }

    private void setCategoryCb() {
        categoryCb.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM categories");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                this.categoryCb.getItems().add(
                        new Category(
                                resultSet.getInt("id"),
                                resultSet.getString("name")));
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }

    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT books.id, books.name,books.availability,categories.id, categories.name, bookkeepings.payment_date AS purchase_date FROM  books INNER JOIN categories ON categories.id = books.category_id INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id  INNER JOIN purchasings ON purchasings_books_details.purchasing_id = purchasings.id LEFT JOIN bookkeepings ON purchasings.id = bookkeepings.purchasing_id;");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Book(
                        resultSet.getInt("books.id"),
                        resultSet.getString("books.name"),
                        resultSet.getString("books.availability"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getString("purchase_date")));
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
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
    void detailsOnAction(ActionEvent event) {
        Book book = table.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlertTools.showAlertError("Error", "Please select a book");

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/book-details-page.fxml"));

            Parent root = loader.load();

            BookDetailController controller = loader.getController();

            controller.setBook(book);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtn.addToBackBtnStack("../view/books-page.fxml");

        } catch (IOException e) {
            AlertTools.showAlertError("Switch scene problem!", "Contact support!");
        }
    }

    @FXML
    void editOnAction(ActionEvent event) {
        Book book = table.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlertTools.showAlertError("Error", "Please select a book");

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/book-edit-page.fxml"));

            Parent root = loader.load();

            BookEditController controller = loader.getController();

            controller.setBook(book);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtn.addToBackBtnStack("../view/books-page.fxml");

        } catch (IOException e) {
            AlertTools.showAlertError("Switch scene problem!", "Contact support!");
        }
    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        setTable();

        setCategoryCb();
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() == null) {
            AlertTools.showAlertError("Error", "Please select a book or fill the search field");

            setTable();

            setDefaultSearchTf();

            return;
        }

        table.getItems().clear();

        if (!ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() == null) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                connection = DatabaseTools.getConnection();
                preparedStatement = connection.prepareStatement(
                        "SELECT books.id, books.name, books.availability, categories.id, categories.name, bookkeepings.payment_date AS purchase_date FROM books INNER JOIN  categories ON books.category_id = categories.id INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id INNER JOIN bookkeepings ON purchasings_books_details.purchasing_id = bookkeepings.id where books.name LIKE ?");
                preparedStatement.setString(1, "%" + searchTf.getText() + "%");
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    table.getItems().add(new Book(
                            resultSet.getInt("books.id"),
                            resultSet.getString("books.name"),
                            resultSet.getString("books.availability"),
                            new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                            resultSet.getString("purchase_date")));
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    AlertTools.showAlertError("Error", e.getMessage());
                }
            }

            setDefaultSearchTf();

            return;
        }

        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() != null) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                connection = DatabaseTools.getConnection();
                preparedStatement = connection.prepareStatement(
                        "SELECT books.id, books.name, books.availability, categories.id, categories.name, bookkeepings.payment_date AS purchase_date FROM books INNER JOIN  categories ON books.category_id = categories.id INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id INNER JOIN bookkeepings ON purchasings_books_details.purchasing_id = bookkeepings.id WHERE categories.id = ?");
                preparedStatement.setInt(1, categoryCb.getSelectionModel().getSelectedItem().getId());
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    table.getItems().add(new Book(
                            resultSet.getInt("books.id"),
                            resultSet.getString("books.name"),
                            resultSet.getString("books.availability"),
                            new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                            resultSet.getString("purchase_date")));
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    AlertTools.showAlertError("Error", e.getMessage());
                }
            }

            setDefaultSearchTf();

            return;
        }

        if (!ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() != null) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                connection = DatabaseTools.getConnection();
                preparedStatement = connection.prepareStatement(
                        "SELECT books.id, books.name, books.availability, categories.id, categories.name, bookkeepings.payment_date AS purchase_date FROM books INNER JOIN  categories ON books.category_id = categories.id INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id INNER JOIN bookkeepings ON purchasings_books_details.purchasing_id = bookkeepings.id WHERE categories.id = ? AND books.name LIKE ? ");
                preparedStatement.setInt(1, categoryCb.getSelectionModel().getSelectedItem().getId());
                preparedStatement.setString(2, "%" + searchTf.getText() + "%");

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    table.getItems().add(new Book(
                            resultSet.getInt("books.id"),
                            resultSet.getString("books.name"),
                            resultSet.getString("books.availability"),
                            new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                            resultSet.getString("purchase_date")));
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    AlertTools.showAlertError("Error", e.getMessage());
                }
            }

            setDefaultSearchTf();

            return;
        }

        setTable();

        setDefaultSearchTf();
    }

    private void setDefaultSearchTf() {
        UiTools.setTextFieldEmpty(searchTf);

        categoryCb.getSelectionModel().clearSelection();
    }

}
