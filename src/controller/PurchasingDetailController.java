package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.Category;
import model.Purchasing;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;

public class PurchasingDetailController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> categoryCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private ListView<String> lv;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableView<Book> table;

    private Purchasing purchasing;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("boughtPrice"));
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    public void setPurchasing(Purchasing purchasing) {
        this.purchasing = purchasing;

        setListView();

        setTable();

    }

    private void setListView() {
        lv.getItems().clear();

        lv.getItems().add("ID: " + purchasing.getId());
        lv.getItems().add("Supplier: " + purchasing.getSupplierName());
        lv.getItems().add("Total Amount: " + purchasing.getTotalAmount());
        lv.getItems().add("Date: " + purchasing.getPurchasingDate());
        lv.getItems().add("Total Books: " + purchasing.getTotalBooksBought());
    }

    private void setTable() {
        table.getItems().clear();

        try {
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings ON purchasings.id = books.purchasing_id WHERE books.purchasing_id = ?;");
            statement.setInt(1, purchasing.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getInt("books.availability"), resultSet.getString("purchasings.date"),
                        resultSet.getDouble("books.bought_price"), resultSet.getInt("books.id"));
                table.getItems().add(book);
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

}
