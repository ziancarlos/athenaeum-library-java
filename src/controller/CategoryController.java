package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Category;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.UiTools;
import tools.ValidationTools;

public class CategoryController {

    @FXML
    private TableColumn<?, ?> connectedBooksCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TextField searchTf;

    @FXML
    private TableView<Category> table;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        connectedBooksCol.setCellValueFactory(new PropertyValueFactory<>("connectedBooks"));

        refreshTableAndTf();
    }

    private void refreshTableAndTf() {
        table.getItems().clear();

        UiTools.setTextFieldEmpty(searchTf);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT categories.id, categories.name, COUNT(books.id) AS connected_books FROM categories LEFT JOIN books ON  categories.id = books.category_id GROUP BY categories.id ");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt("categories.id"),
                        resultSet.getString("categories.name"), resultSet.getInt("connected_books"));

                table.getItems().add(category);
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }

    @FXML
    void addOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/category-add-page.fxml");

        BackBtn.addToBackBtnStack("../view/categories-page.fxml");
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        Category category = table.getSelectionModel().getSelectedItem();

        if (category == null) {
            AlertTools.showAlertError("No category selected!", "Select a category!");
            return;
        }

        if (category.getConnectedBooks() > 0) {
            AlertTools.showAlertError("Cannot delete category!", "Category is connected to books!");
            return;
        }

        if (AlertTools.showAlertConfirmationWithOptional("Are you sure to delete this category?", "Delete category?")
                .get() == ButtonType.CANCEL) {
            return;
        }

        if (Category.deleteCategory(category)) {
            refreshTableAndTf();

            AlertTools.showAlertConfirmation("Delete category success!", null);
        } else {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        }
    }

    @FXML
    void detailsOnAction(ActionEvent event) {
        Category category = table.getSelectionModel().getSelectedItem();

        if (category == null) {
            AlertTools.showAlertError("No category selected!", "Select a category!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/category-details-page.fxml"));

            Parent root = loader.load();

            CategoryDetailController controller = loader.getController();

            controller.setCategory(category);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtn.addToBackBtnStack("../view/categories-page.fxml");

        } catch (IOException e) {
            AlertTools.showAlertError("Switch scene problem!", "Contact support!");
        }
    }

    @FXML
    void editOnAction(ActionEvent event) {
        Category category = table.getSelectionModel().getSelectedItem();

        if (category == null) {
            AlertTools.showAlertError("No category selected!", "Select a category!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/category-edit-page.fxml"));

            Parent root = loader.load();

            CategoryEditController controller = loader.getController();

            controller.setCategory(category);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

            BackBtn.addToBackBtnStack("../view/categories-page.fxml");

        } catch (IOException e) {
            AlertTools.showAlertError("Switch scene problem!", "Contact support!");
        }

    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        refreshTableAndTf();
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)) {
            AlertTools.showAlertError("Search text field is empty!", "Please enter a search text!");

            refreshTableAndTf();

            return;
        }

        table.getItems().clear();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT categories.id, categories.name, COUNT(books.id) AS connected_books FROM categories LEFT JOIN books ON  categories.id = books.category_id GROUP BY categories.id HAVING categories.name LIKE ? ");
            statement.setString(1, "%" + searchTf.getText() + "%");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt("categories.id"),
                        resultSet.getString("categories.name"), resultSet.getInt("connected_books"));

                table.getItems().add(category);
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }

            UiTools.setTextFieldEmpty(searchTf);
        }
    }

}
