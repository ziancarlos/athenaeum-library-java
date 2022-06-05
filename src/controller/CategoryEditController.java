package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Category;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.ValidationTools;

public class CategoryEditController {

    @FXML
    private TextField categoryNameTf;

    private Category category;

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void editBtn(ActionEvent event) {
        if(ValidationTools.isTextFieldEmptyOrNull(categoryNameTf)){
            AlertTools.AlertError("Error!", "Category Name Text Field Is Empty!", "Please Fill Category Name Text Field!");

            defaultTf();

            return;
        }

        if(category.getName().equals(categoryNameTf.getText())){
            AlertTools.AlertError("Error!", "Category Name Is Not Changed", null);

            defaultTf();

            return;
        }
        
        if(Category.isCategoryNameExist(categoryNameTf.getText(), category.getId())){
            AlertTools.AlertError("Error!", "Category Name Is Already Exist!", "Please Enter Another Category Name!");

            defaultTf();

            return;
        }

        if(Category.editCategory(category, categoryNameTf.getText())){
            AlertTools.AlertInformation("Success!", "Category Edited Successfully!", "Category Name: " + categoryNameTf.getText());

            BackBtnTools.backBtnActionEvent(event);
        }else{
            AlertTools.AlertError("Error!", "Category Not Edited!", "Please Try Again!");

            defaultTf();
        }

    }

    public void setCategory(Category category){
        this.category = category;

        defaultTf();
    }

    public void defaultTf(){
        categoryNameTf.setText(category.getName());
    }

}
