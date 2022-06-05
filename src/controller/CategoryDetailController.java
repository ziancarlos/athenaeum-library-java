package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Category;
import tools.BackBtnTools;

public class CategoryDetailController {

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private ListView<String> lv;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableView<?> table;

    private Category category;

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    public void setCategory(Category category){
        this.category = category;

        setListView();
    }

    private void setListView(){
        lv.getItems().clear();
        lv.getItems().add("Category ID: " + category.getId());
        lv.getItems().add("Category Name: " + category.getName());
        lv.getItems().add("Category Connected Books: " + category.getConnectedBooks());
    }

}
