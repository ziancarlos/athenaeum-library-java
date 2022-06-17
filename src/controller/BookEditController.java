package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Book;
import model.Category;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.ValidationTools;

public class BookEditController {

    @FXML
    private TextField bookNameTf;

    @FXML
    private ComboBox<Category> categoryCb;

    private Book book;

    private int categoryId;

    private void setCategoryCb() {
        categoryCb.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM categories");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                this.categoryCb.getItems().add(
                        new Category(
                                resultSet.getInt("id"),
                                resultSet.getString("name")));
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }

    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void editBookBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(bookNameTf) || categoryCb.getSelectionModel().isEmpty()) {
            AlertTools.showAlertError("Error", "Please fill all fields!");

            setDefaultTf();

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 46, bookNameTf.getText())) {
            AlertTools.showAlertError("Text is invalid!", "Please check your text");

            setDefaultTf();

            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatementSelect = null;
        PreparedStatement preparedStatementUpdate = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            preparedStatementSelect = connection
                    .prepareStatement("SELECT * FROM books WHERE id = ? AND name = ? AND category_id = ? FOR UPDATE");
            preparedStatementSelect.setInt(1, book.getId());
            preparedStatementSelect.setString(2, bookNameTf.getText());
            preparedStatementSelect.setInt(3, categoryCb.getSelectionModel().getSelectedItem().getId());

            resultSet = preparedStatementSelect.executeQuery();

            if (!resultSet.next()) {
                preparedStatementUpdate = connection
                        .prepareStatement("UPDATE books SET name = ?, category_id = ? WHERE id = ?");
                preparedStatementUpdate.setString(1, bookNameTf.getText());
                preparedStatementUpdate.setInt(2, categoryCb.getSelectionModel().getSelectedItem().getId());
                preparedStatementUpdate.setInt(3, book.getId());

                affectedRows = preparedStatementUpdate.executeUpdate();
                if (affectedRows > 0) {
                    AlertTools.showAlertInformation("Success", "Book updated successfully!");
                    connection.commit();
                    BackBtn.backBtnActionEvent(event);
                } else {
                    AlertTools.showAlertError("Error", "Book not updated!");

                    setDefaultTf();

                    connection.rollback();
                }
            } else {
                AlertTools.showAlertError("Error", "Book is unchanged!");

                setDefaultTf();

                connection.rollback();

                return;
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());

            setDefaultTf();

            try {
                connection.rollback();
            } catch (Exception ex) {
                AlertTools.showAlertError("Error", ex.getMessage());
            }
        } finally {
            try {
                if (preparedStatementUpdate != null) {
                    preparedStatementUpdate.close();
                }
                if (connection != null) {
                    connection.close();
                }

                if (preparedStatementSelect != null) {
                    preparedStatementSelect.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }

    }

    public void setBook(Book book) {
        this.book = book;

        setDefaultTf();

        setCategoryCb();

        selectCategoryCb();
    }

    private void selectCategoryCb() {
        for (Category category : categoryCb.getItems()) {
            if (category.getId() == categoryId) {
                categoryCb.getSelectionModel().select(category);
                break;
            }
        }
    }

    private void setDefaultTf() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM books WHERE id = ?");
            preparedStatement.setInt(1, book.getId());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bookNameTf.setText(resultSet.getString("name"));
                categoryId = resultSet.getInt("category_id");

            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error", e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }
    }

}
