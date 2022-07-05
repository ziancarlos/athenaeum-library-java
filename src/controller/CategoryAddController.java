package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import tools.AlertTools;
import tools.BackBtn;
import tools.UiTools;
import tools.ValidationTools;

public class CategoryAddController {

    @FXML
    private TextField categoryNameTf;

    @FXML
    void addOnAction(ActionEvent event) {

        if (AlertTools
                .showAlertConfirmationWithOptional("Confirmation!", "Are you sure you want to add this category?")
                .get() == ButtonType.CANCEL) {
            return;
        }

        if (ValidationTools.isTextFieldEmptyOrNull(categoryNameTf)) {
            AlertTools.showAlertError("Text field is empty!", "Please fill in all fields");

            UiTools.setTextFieldEmpty(categoryNameTf);

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 46, categoryNameTf.getText())) {
            AlertTools.showAlertError("Text is invalid!", "Please check your text");

            UiTools.setTextFieldEmpty(categoryNameTf);

            return;
        }

        if (model.Category.addNewCategory(categoryNameTf.getText())) {
            AlertTools.showAlertInformation("Category added!", "Category added successfully");

            BackBtn.backBtnActionEvent(event);
        } else {
            AlertTools.showAlertError("Category already exists!", "Please check your category name");

            UiTools.setTextFieldEmpty(categoryNameTf);

            return;
        }

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

}
