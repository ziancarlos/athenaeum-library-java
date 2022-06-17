package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.Category;
import tools.BackBtn;

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
    void backBtnOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setCategory(model.Category category) {
        this.category = category;

        setLv();

        setTable();
    }

    private void setTable() {
        table.getItems().clear();

        for (Book book : category.getAllBooks()) {
            table.getItems().add(book);
        }
    }

    private void setLv() {
        lv.getItems().clear();
        lv.getItems().add("Id : " + category.getId());
        lv.getItems().add("Name : " + category.getName());
        lv.getItems().add("Connected Books : " + category.getConnectedBooks());
    }

}
