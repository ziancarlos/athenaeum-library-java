package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.Category;
import model.PurchasingDetail;
import tools.AlertTools;
import tools.BackBtn;
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

    private PurchasingDetail purchasingDetail;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setPurchasingDetail(PurchasingDetail purchasingDetail) {
        this.purchasingDetail = purchasingDetail;

        setTable();

        setLv();
    }

    private void setLv() {
        lv.getItems().clear();

        lv.getItems().add("Id : " + purchasingDetail.getId());
        lv.getItems().add("Supplier Name : " + purchasingDetail.getSupplierName());
        lv.getItems().add("Purchasing Date : " + purchasingDetail.getPurchasingDate());
        lv.getItems().add("Total Amount : " + purchasingDetail.getTotalAmount());
        lv.getItems().add("Total Books : " + purchasingDetail.getTotalBooks());
    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT books.id, books.name, books.availability, categories.id, categories.name, purchasings_books_details.purchasing_price FROM books INNER JOIN categories ON books.category_id = categories.id INNER JOIN purchasings_books_details ON books.id = purchasings_books_details.book_id WHERE purchasings_books_details.purchasing_id = ?");
            preparedStatement.setInt(1, purchasingDetail.getId());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("books.id"), resultSet.getString("books.name"),
                        resultSet.getString("books.availability"),
                        new Category(resultSet.getInt("categories.id"), resultSet.getString("categories.name")),
                        resultSet.getDouble("purchasings_books_details.purchasing_price"));
                table.getItems().add(book);
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }
}
