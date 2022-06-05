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
import model.Category;
import tools.DatabaseTools;
import tools.ValidationTools;
import tools.AlertTools;

public class CategoryController {

    @FXML
    private TableColumn<Category, Integer> connectedBooks;

    @FXML
    private TableColumn<Category, Integer> idCol;

    @FXML
    private TableColumn<Category, String> nameCol;

    @FXML
    private TextField searchTf;

    @FXML
    private TableView<Category> table;

    public void initialize(){

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        connectedBooks.setCellValueFactory(new PropertyValueFactory<>("connectedBooks"));

        try{
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT categories.id, categories.name, COUNT(books.id) AS connectedBooks FROM categories LEFT JOIN books ON categories.id = books.category_id GROUP BY categories.id;");
            
            while(resultSet.next()){
                Category category = new Category(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("connectedBooks"));
                table.getItems().add(category);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void addBtn(ActionEvent event) {

    }

    @FXML
    void backBtn(ActionEvent event) {

    }

    @FXML
    void deleteBtn(ActionEvent event) {

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
            AlertTools.AlertError("Error!", "Please Enter A Search Term!", null);
            return;
        }


        table.getItems().clear();

        try{
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT categories.id, categories.name, COUNT(books.id) AS connectedBooks FROM categories LEFT JOIN books ON categories.id = books.category_id WHERE categories.name LIKE ? GROUP BY categories.id;");
            statement.setString(1, "%" +searchTf.getText() + "%");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Category category = new Category(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("connectedBooks"));
                table.getItems().add(category);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
