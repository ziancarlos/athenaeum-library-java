package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Category;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.ValidationTools;

public class CategoryEditController {

    @FXML
    private TextField categoryNameTf;

    private Category category;

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void editOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(categoryNameTf)) {
            AlertTools.showAlertError("Text field is empty!", "Please fill in all fields");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 46, categoryNameTf.getText())) {
            AlertTools.showAlertError("Text is invalid!", "Please check your text");

            setDefaultTf();

            return;
        }

        if (category.getName().equals(categoryNameTf.getText())) {
            AlertTools.showAlertError("Category not changed!", "Please check your category name");

            setDefaultTf();

            return;
        }

        if (Category.isExistWithout(category, categoryNameTf.getText())) {
            AlertTools.showAlertError("Category already exists!", "Please check your category name");

            setDefaultTf();

            return;
        }

        Connection connection = null;
        PreparedStatement statementSelect = null;
        PreparedStatement statementUpdate = null;
        ResultSet resultSet = null;
        int affectedRows = 0;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            statementSelect = connection.prepareStatement("SELECT * FROM categories WHERE id != ? AND name = ?");
            statementSelect.setInt(1, category.getId());
            statementSelect.setString(2, categoryNameTf.getText());

            resultSet = statementSelect.executeQuery();
            if (!resultSet.next()) {
                statementUpdate = connection.prepareStatement("UPDATE categories SET name = ? WHERE id = ?");
                statementUpdate.setString(1, categoryNameTf.getText());
                statementUpdate.setInt(2, category.getId());

                affectedRows = statementUpdate.executeUpdate();
                if (affectedRows > 0) {
                    connection.commit();

                    AlertTools.showAlertInformation("Category updated!", "Category updated successfully");

                    BackBtn.backBtnActionEvent(event);
                } else {
                    connection.rollback();

                    AlertTools.showAlertError("Category not updated!", "Category not updated");
                }

            } else {
                AlertTools.showAlertError("Category already exists!", "Please check your category name");

                setDefaultTf();

                connection.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    AlertTools.showAlertError("Error!", "Error while updating category");
                }
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statementSelect != null) {
                    statementSelect.close();
                }
                if (statementUpdate != null) {
                    statementUpdate.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", "Error while updating category");
            }
        }
    }

    public void setCategory(Category category) {
        this.category = category;

        setDefaultTf();
    }

    private void setDefaultTf() {
        categoryNameTf.setText(category.getName());
    }

}
