package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Category;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.ValidationTools;
import tools.AlertTools;
import tools.BackBtnTools;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

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
        SwitchSceneTools.changeSceneActionEvent(event, "../view/category-add-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/categories-page.fxml");
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void deleteBtn(ActionEvent event) {
        Category category = table.getSelectionModel().getSelectedItem();

        if(category == null){
            AlertTools.AlertInformation("Error!", "No Category Selected!", "Please select a category to delete.");
            return;
        }

        if(category.getConnectedBooks() > 0){
            AlertTools.AlertError("Error!", "This Category Is Connected To Books!", null);
            
            return;
        }

        if(AlertTools.AlertConfirmation("Confirmation!", "Are you sure wanna delete " + category.getName() + "Category?", null).get() == ButtonType.OK){

            if(Category.deleteCategory(category)){
                AlertTools.AlertInformation("Success!", "Category Succesfully Deleted!", "The category has been deleted.");
            }else{
                AlertTools.AlertError("Error!", "Category Deletion Failed!", "The category could not be deleted.");
            }

            table.getItems().remove(category);
        }   
       
    }

    @FXML
    void detailsBtn(ActionEvent event) {
        Category category = table.getSelectionModel().getSelectedItem();

        if(category == null){
            AlertTools.AlertInformation("Error!", "No Category Selected!", "Please select a category to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/category-details-page.fxml"));

            Parent root = loader.load();

            CategoryDetailController categoryDetailController = loader.getController();

            categoryDetailController.setCategory(category);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtnTools.addToBackBtnStack("../view/categories-page.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void editBtn(ActionEvent event) {
        Category category = table.getSelectionModel().getSelectedItem();

        if(category == null){
            AlertTools.AlertInformation("Error!", "No Category Selected!", "Please select a category to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/category-edit-page.fxml"));

            Parent root = loader.load();

            CategoryEditController categoryEditController = loader.getController();

            categoryEditController.setCategory(category);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtnTools.addToBackBtnStack("../view/categories-page.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
