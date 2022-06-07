package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.Category;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.ValidationTools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

public class BookController {

    @FXML
    private TableColumn<Book, String> availCol;

    @FXML
    private ComboBox<Category> categoryCb;

    @FXML
    private TableColumn<Book, String> categoryCol;

    @FXML
    private TableColumn<Book, Integer> idCol;

    @FXML
    private TableColumn<Book, String> nameCol;

    @FXML
    private TableColumn<Book, String> purchaseDateCol;

    @FXML
    private TextField searchTf;

    @FXML
    private TableView<Book> table;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

        setTable();

        setCategoryCb();
    }

    @FXML
    void searchBtn(ActionEvent event) {

        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() == null) {

            setTable();

        } else if (ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() != null) {

            queryAllWithCategory();

        } else if (!ValidationTools.isTextFieldEmptyOrNull(searchTf)
                && categoryCb.getSelectionModel().getSelectedItem() == null) {

            queryAllWithBookName();

        } else {

            queryAllWithBookNameAndCategory();

        }

        setDefaultSearchForm();
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void deactivateBtn(ActionEvent event) {

    }

    @FXML
    void detailsBtn(ActionEvent event) {
        Book book = table.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlertTools.AlertError("Error!", "No Book Selected!", "Please select a book to view details.");

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

            BackBtnTools.addToBackBtnStack("../view/books-page.fxml");

        } catch (IOException e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    @FXML
    void editBtn(ActionEvent event) {
        Book book = table.getSelectionModel().getSelectedItem();

        if (book != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/book-edit-page.fxml"));

                Parent root = loader.load();

                BookEditController bookEditController = loader.getController();

                bookEditController.setBook(book);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();

                BackBtnTools.addToBackBtnStack("../view/books-page.fxml");

            } catch (IOException e) {
                AlertTools.AlertErrorContactSupport();
            }
        } else {
            AlertTools.AlertError("Error!", "No Book Selected!", "Please select a book from the table.");
        }
    }

    @FXML
    void refreshBtn(ActionEvent event) {
        setTable();

        setCategoryCb();

        setDefaultSearchForm();
    }

    private void setDefaultSearchForm() {
        searchTf.setText("");
        categoryCb.getSelectionModel().clearSelection();
    }

    private void setCategoryCb() {
        categoryCb.getItems().clear();
        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM categories");

            while (resultSet.next()) {
                categoryCb.getItems().add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    private void setTable() {
        table.getItems().clear();
        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings ON purchasings.id = books.purchasing_id;");

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getInt("books.availability"), resultSet.getString("purchasings.date"),
                        resultSet.getDouble("books.bought_price"), resultSet.getInt("books.id"));
                table.getItems().add(book);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    private void queryAllWithCategory() {
        try {
            table.getItems().clear();

            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings ON purchasings.id = books.purchasing_id WHERE  categories.id = ?;");
            statement.setInt(1, categoryCb.getSelectionModel().getSelectedItem().getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getInt("books.availability"), resultSet.getString("purchasings.date"),
                        resultSet.getDouble("books.bought_price"), resultSet.getInt("books.id"));

                table.getItems().add(book);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    private void queryAllWithBookName() {
        try {
            table.getItems().clear();

            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings ON purchasings.id = books.purchasing_id WHERE books.name LIKE ?;");
            statement.setString(1, "%" + searchTf.getText() + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getInt("books.availability"), resultSet.getString("purchasings.date"),
                        resultSet.getDouble("books.bought_price"), resultSet.getInt("books.id"));

                table.getItems().add(book);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    private void queryAllWithBookNameAndCategory() {
        try {
            table.getItems().clear();

            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings ON purchasings.id = books.purchasing_id WHERE books.name LIKE ? AND categories.id = ?;");
            statement.setString(1, "%" + searchTf.getText() + "%");
            statement.setInt(2, categoryCb.getSelectionModel().getSelectedItem().getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getInt("books.availability"), resultSet.getString("purchasings.date"),
                        resultSet.getDouble("books.bought_price"), resultSet.getInt("books.id"));

                table.getItems().add(book);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

}
