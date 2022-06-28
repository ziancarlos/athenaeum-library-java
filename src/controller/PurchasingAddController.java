package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Category;
import model.PurchasingAddTemp;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.UiTools;
import tools.ValidationTools;

public class PurchasingAddController {

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> bookNameCol;

    @FXML
    private TextField bookNameTf;

    @FXML
    private ComboBox<Category> categoryCb;

    @FXML
    private TableColumn<?, ?> categoryCol;

    @FXML
    private TextField priceTf;

    @FXML
    private TextField supplierNameTf;

    @FXML
    private TableView<PurchasingAddTemp> table;

    @FXML
    private Text totalAmountText;

    private double totalAmount;

    private ArrayList<PurchasingAddTemp> purchasingAddTempList = new ArrayList<>();

    public void initialize() {
        amountCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        setCategoryCb();

        setTable();
    }

    private void setTable() {
        table.getItems().clear();
        table.getItems().addAll(purchasingAddTempList);
    }

    private void setCategoryCb() {
        categoryCb.getItems().addAll(model.Category.getAllCategories());
    }

    @FXML
    void addOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(bookNameTf, priceTf)
                || categoryCb.getSelectionModel().getSelectedItem() == null) {
            AlertTools.showAlertError("Text field is empty!", "Please fill in all fields");

            setAddFormDefault();

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 46, bookNameTf.getText())) {
            AlertTools.showAlertError("Text is invalid!", "Please check your text");

            setAddFormDefault();

            return;
        }

        if (!ValidationTools.isDouble(priceTf.getText())) {
            AlertTools.showAlertError("Price is invalid!", "Please check your price");

            setAddFormDefault();

            return;
        }

        purchasingAddTempList
                .add(new PurchasingAddTemp(bookNameTf.getText(), categoryCb.getSelectionModel().getSelectedItem(),
                        Double.parseDouble(priceTf.getText())));

        setTable();

        setAddFormDefault();

        setTotalAmount();

        AlertTools.showAlertConfirmation("Book added!", "Book added successfully");
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        PurchasingAddTemp purchasingAddTemp = table.getSelectionModel().getSelectedItem();

        if (purchasingAddTemp == null) {
            AlertTools.showAlertError("No book selected!", "Please select a book");

            return;
        }

        purchasingAddTempList.remove(purchasingAddTemp);

        setTotalAmount();

        setTable();
    }

    @FXML
    void purchaseOnAction(ActionEvent event) {
        if (purchasingAddTempList.isEmpty()) {
            AlertTools.showAlertError("No book selected!", "Please select a book");

            setAllFormDefault();

            return;
        }

        if (ValidationTools.isTextFieldEmptyOrNull(supplierNameTf)) {
            AlertTools.showAlertError("Text field is empty!", "Please fill in all fields");

            setAllFormDefault();

            return;
        }

        if (!ValidationTools.isTextIsValid(3, 46, supplierNameTf.getText())) {
            AlertTools.showAlertError("Text is invalid!", "Please check your text");

            setAllFormDefault();

            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatementPurchasing = null;
        PreparedStatement preparedStatementPurchasingBookkeeping = null;
        PreparedStatement preparedStatementBook = null;
        PreparedStatement preparedStatementBookDetail = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            preparedStatementPurchasing = connection.prepareStatement(
                    "INSERT INTO purchasings (supplier_name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatementPurchasing.setString(1, supplierNameTf.getText());
            affectedRows = preparedStatementPurchasing.executeUpdate();

            if (affectedRows > 0) {

                resultSet = preparedStatementPurchasing.getGeneratedKeys();
                if (resultSet.next()) {

                    int purchasingId = resultSet.getInt(1);

                    preparedStatementPurchasingBookkeeping = connection.prepareStatement(
                            "INSERT INTO purchasings_bookkeepings (double_entry_type, transaction_type, amount, purchasing_id, payment_date) VALUES (?, ?, ?, ?, NOW())");
                    preparedStatementPurchasingBookkeeping.setString(1, "kredit");
                    preparedStatementPurchasingBookkeeping.setString(2, "purchasing");
                    preparedStatementPurchasingBookkeeping.setDouble(3, totalAmount);
                    preparedStatementPurchasingBookkeeping.setInt(4, purchasingId);

                    affectedRows = preparedStatementPurchasingBookkeeping.executeUpdate();

                    if (affectedRows > 0) {

                        for (PurchasingAddTemp purchasingAddTemp : purchasingAddTempList) {

                            preparedStatementBook = connection.prepareStatement(
                                    "INSERT INTO books (name, category_id, availability) VALUES (?, ?, ?)",
                                    PreparedStatement.RETURN_GENERATED_KEYS);
                            preparedStatementBook.setString(1, purchasingAddTemp.getBookName());
                            preparedStatementBook.setInt(2, purchasingAddTemp.getCategory().getId());
                            preparedStatementBook.setInt(3, 1);
                            affectedRows = preparedStatementBook.executeUpdate();

                            if (affectedRows > 0) {
                                resultSet = preparedStatementBook.getGeneratedKeys();

                                if (resultSet.next()) {
                                    int bookId = resultSet.getInt(1);

                                    preparedStatementBookDetail = connection.prepareStatement(
                                            "INSERT INTO purchasings_books_details (purchasing_price, purchasing_id, book_id) VALUES (?, ?, ?)");
                                    preparedStatementBookDetail.setDouble(1, purchasingAddTemp.getPrice());
                                    preparedStatementBookDetail.setInt(2, purchasingId);
                                    preparedStatementBookDetail.setInt(3, bookId);
                                    preparedStatementBookDetail.executeUpdate();
                                }

                            } else {
                                AlertTools.showAlertError("Book is not added!", "Book is not added");

                                connection.rollback();

                                purchasingAddTempList.clear();

                                setTotalAmount();

                                setTable();

                                return;
                            }

                        }

                        connection.commit();

                        AlertTools.showAlertConfirmation("Purchasing is done!", "Purchasing is done");

                        purchasingAddTempList.clear();

                        BackBtn.backBtnActionEvent(event);

                        setTotalAmount();

                        setTable();

                        setAllFormDefault();

                    } else {
                        AlertTools.showAlertError("Error!", "Error while adding purchasing");

                        setAllFormDefault();

                        purchasingAddTempList.clear();

                        setTotalAmount();

                        setTable();

                        connection.rollback();
                    }

                } else {
                    AlertTools.showAlertError("Error!", "Error while adding purchasing");

                    setAllFormDefault();

                    purchasingAddTempList.clear();

                    setTotalAmount();

                    setTable();

                    connection.rollback();
                }

            } else {
                AlertTools.showAlertError("Error!", "Error while adding purchasing");

                setAllFormDefault();

                purchasingAddTempList.clear();

                setTotalAmount();

                setTable();

                connection.rollback();
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Error!", "Error while connecting to database");

            try {
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            purchasingAddTempList.clear();

            setTotalAmount();

            setTable();

        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (preparedStatementPurchasing != null)
                    preparedStatementPurchasing.close();
                if (preparedStatementPurchasingBookkeeping != null)
                    preparedStatementPurchasingBookkeeping.close();
                if (preparedStatementBook != null)
                    preparedStatementBook.close();
                if (preparedStatementBookDetail != null)
                    preparedStatementBookDetail.close();

            } catch (Exception e) {
                AlertTools.showAlertError("Error!", "Error while connecting to database");
            }
        }

    }

    private void setAddFormDefault() {
        UiTools.setTextFieldEmpty(bookNameTf, priceTf);
        categoryCb.getSelectionModel().clearSelection();
    }

    private void setAllFormDefault() {
        setAddFormDefault();

        UiTools.setTextFieldEmpty(supplierNameTf);
    }

    private void setTotalAmount() {
        totalAmount = 0;

        for (PurchasingAddTemp purchasingAddTemp : purchasingAddTempList) {
            totalAmount += purchasingAddTemp.getPrice();
        }

        totalAmountText.setText(String.valueOf(totalAmount));
    }
}
