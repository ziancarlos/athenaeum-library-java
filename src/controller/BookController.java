package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Book;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.ValidationTools;

public class BookController {

    @FXML
    private TableColumn<Book, String> availCol;

    @FXML
    private AnchorPane backBtn;

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
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT books.id, books.name, books.availability, categories.name, purchasings.date FROM books  INNER JOIN categories ON books.category_id = categories.id  INNER JOIN purchased_books ON books.id = purchased_books.book_id INNER JOIN purchasings ON purchasings.id = purchased_books.purchasing_id;");

            while (resultSet.next()) {
               table.getItems().add(new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"), resultSet.getString("categories.name"), resultSet.getString("purchasings.date"), resultSet.getString("books.availability")));
            }

            DatabaseTools.closeQueryOperation(connection, statement,resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deactivateBtn(ActionEvent event) {

    }

    @FXML
    void detailsBtn(ActionEvent event) {

    }

    @FXML
    void editBtn(ActionEvent event) {

    }

    @FXML
    void searchBtn(ActionEvent event) {
        if(ValidationTools.isTextFieldEmptyOrNull(searchTf)){
            AlertTools.AlertError("Error!", "Search Text field consist of blank!", "Please fill in all text field!");
            return;
        }


        table.getItems().clear();
        
        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT books.id, books.name, books.availability, categories.name, purchasings.date FROM books  INNER JOIN categories ON books.category_id = categories.id  INNER JOIN purchased_books ON books.id = purchased_books.book_id INNER JOIN purchasings ON purchasings.id = purchased_books.purchasing_id WHERE books.name = ?;");
            statement.setString(1, searchTf.getText());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
               table.getItems().add(new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"), resultSet.getString("categories.name"), resultSet.getString("purchasings.date"), resultSet.getString("books.availability")));
            }

            DatabaseTools.closeQueryOperation(connection, statement,resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

}
