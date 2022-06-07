package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Book;
import model.Category;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.ValidationTools;

public class BookEditController {

    @FXML
    private TextField bookNameTf;

    @FXML
    private ComboBox<Category> categoryCb;

    private Book book;

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void editBookBtn(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(bookNameTf)) {
            AlertTools.AlertError("Error!", "Book Name Text Field Is Empty!", "Please Fill Book Name Text Field!");

            setDefaultTf();

            return;
        }

        if (categoryCb.getSelectionModel().getSelectedItem() == null) {
            AlertTools.AlertError("Error!", "Category Combo Box Is Empty!", "Please Select Category!");

            setDefaultTf();

            return;
        }

        if (bookNameTf.getText().equals(book.getName())
                && categoryCb.getSelectionModel().getSelectedItem().getId() == book.getCategory().getId()) {
            AlertTools.AlertError("Error!", "No change has been made!", null);

            setDefaultTf();

            return;
        }

        if (Book.editBook(book, bookNameTf.getText(), categoryCb.getSelectionModel().getSelectedItem())) {
            AlertTools.AlertInformation("Success!", "Changes Have been made!", null);

            BackBtnTools.backBtnActionEvent(event);

            setDefaultTf();

            return;
        } else {
            AlertTools.AlertError("Error!", null, null);

            setDefaultTf();

            return;
        }

    }

    public void setBook(Book book) {
        this.book = book;

        setCategoryCb();

        setDefaultTf();
    }

    private void setDefaultTf() {
        bookNameTf.setText(book.getName());
        categoryCb.setValue(book.getCategory());
    }

    private void setCategoryCb() {
        try {
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM categories");

            while (resultSet.next()) {
                categoryCb.getItems().add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

}
