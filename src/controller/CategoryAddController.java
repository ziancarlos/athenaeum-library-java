package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Category;
import tools.BackBtnTools;
import tools.ValidationTools;
import tools.AlertTools;

public class CategoryAddController {

    @FXML
    private TextField categoryNameTf;


    @FXML
    void addBtn(ActionEvent event) {
        if(ValidationTools.isTextFieldEmptyOrNull(categoryNameTf)){
            AlertTools.AlertError("Error!", "Category Name Text Field Is Empty!", "Please Fill Category Name Text Field!");

            defaultTf();

            return;
        }
        
        if(Category.isCategoryNameExist(categoryNameTf.getText())){
            AlertTools.AlertError("Error!", "Category Name Is Already Exist!", "Please Enter Another Category Name!");

            defaultTf();

            return;
        }

        if(Category.addCategory(categoryNameTf.getText())){
            AlertTools.AlertInformation("Success!", "Category Added Successfully!", "Category Name: " + categoryNameTf.getText());

            BackBtnTools.backBtnActionEvent(event);
        }else{
            AlertTools.AlertError("Error!", "Category Not Added!", "Please Try Again!");

            defaultTf();
        }

    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }


    void defaultTf(){
        categoryNameTf.setText("");
    }

}
