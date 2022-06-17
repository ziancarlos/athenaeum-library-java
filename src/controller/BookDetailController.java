package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Book;
import tools.BackBtn;

public class BookDetailController {

    @FXML
    private ListView<String> listView;

    private Book book;

    private void setLv() {
        listView.getItems().clear();

        listView.getItems().add("Id : " + book.getId());
        listView.getItems().add("Title : " + book.getName());
        listView.getItems().add("Availability : " + book.getAvailability());
        listView.getItems().add("Category : " + book.getCategory().getName());
        listView.getItems().add("Purchase Date : " + book.getPurchaseDate());

    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setBook(Book book) {
        this.book = book;

        setLv();
    }

}
