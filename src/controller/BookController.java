package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.Category;
import tools.BackBtnTools;
import tools.DatabaseTools;

public class BookController {

    @FXML
    private TableColumn<Book,  String> availCol;

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

    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
   
        try{
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings ON purchasings.id = books.purchasing_id;");
        
            while(resultSet.next()){
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"), new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")), resultSet.getInt("books.availability"), resultSet.getString("purchasings.date"), resultSet.getDouble("books.bought_price" 
                ) , resultSet.getInt("books.id"));
                table.getItems().add(book);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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

    }

    @FXML
    void editBtn(ActionEvent event) {

    }

    @FXML
    void searchBtn(ActionEvent event) {

    }

}
