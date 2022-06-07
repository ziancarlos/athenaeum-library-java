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
import model.Category;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;

public class CategoryDetailController {

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private ListView<String> lv;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableView<Book> table;

    private Category category;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    public void setCategory(Category category) {
        this.category = category;

        setListView();

        setTable();
    }

    private void setTable() {
        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE category_id = ?");
            statement.setInt(1, category.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("id"), resultSet.getString("name"));

                table.getItems().add(book);
            }

            DatabaseTools.closeQueryOperationWithPreparedStatement(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    private void setListView() {
        lv.getItems().clear();
        lv.getItems().add("Category ID: " + category.getId());
        lv.getItems().add("Category Name: " + category.getName());
        lv.getItems().add("Category Connected Books: " + category.getConnectedBooks());
    }

}
