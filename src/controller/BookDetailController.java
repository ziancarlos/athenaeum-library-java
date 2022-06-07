package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Book;
import tools.BackBtnTools;

public class BookDetailController {

    @FXML
    private ListView<String> listView;

    private Book book;

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    public void setBook(Book book) {
        this.book = book;

        setListView();
    }

    private void setListView() {
        listView.getItems().add("Book ID: " + book.getId());
        listView.getItems().add("Book Name: " + book.getName());
        listView.getItems().add("Category: " + book.getCategory().getName());
        listView.getItems().add("Purchase Date: " + book.getpurchaseDate());
        listView.getItems().add("Availability: " + book.getAvailability());
    }

}
