package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Book;
import model.Category;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.ValidationTools;

public class PurchaseAddController {

    @FXML
    private TableColumn<Book, Double> amountCol;

    @FXML
    private TableColumn<Book, String> bookNameCol;

    @FXML
    private TextField bookNameTf;

    @FXML
    private ComboBox<Category> categoryCb;

    @FXML
    private TableColumn<Book, String> categoryCol;

    @FXML
    private TextField priceTf;

    @FXML
    private TextField supplierNameTf;

    @FXML
    private TableView<Book> table;

    @FXML
    private Text totalAmountText;

    private LinkedList<Book> books = new LinkedList<>();

    private double totalAmount = 0;

    public void initialize(){
        setCategoryCb();

        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("boughtPrice"));
    }

    @FXML
    void addBtn(ActionEvent event) {
        if(ValidationTools.isTextFieldEmptyOrNull(bookNameTf, priceTf)){
            AlertTools.AlertError("Error!", "Text Field Is Empty!", "Please Dont Leave A Blank!");

            defaultAddBookTf();

            return;
        }

        if(!ValidationTools.isDouble(priceTf.getText())){
            AlertTools.AlertError("Error!", "Price Is Not A Number!", "Please Enter A Valid Number!");

            defaultAddBookTf();

            return;
        }


        if(categoryCb.getSelectionModel().getSelectedItem() == null){
            AlertTools.AlertError("Error!", "Category Is Not Selected!", "Please Select A Category!");

            defaultAddBookTf();
            
            return;
        }

        books.add(new Book(bookNameTf.getText(), categoryCb.getSelectionModel().getSelectedItem(), Double.parseDouble(priceTf.getText())));

        totalAmountRefresh(Double.parseDouble(priceTf.getText()));

        refreshTable();

        defaultAddBookTf();
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void deleteBtn(ActionEvent event) {
        if(table.getSelectionModel().getSelectedItem() == null){
            AlertTools.AlertError("Error!", "Book Is Not Selected!", "Please Select A Book!");

            return;
        }

        books.remove(table.getSelectionModel().getSelectedItem());

        refreshTable();
    }

    @FXML
    void purchaseBtn(ActionEvent event) {
        if(books.size() == 0){
            AlertTools.AlertError("Error!", "No Book Is Added!", "Please Add A Book!");
    
            return;
        }

        if(ValidationTools.isTextFieldEmptyOrNull(supplierNameTf)){
            AlertTools.AlertError("Error!", "Supplier Name Text Field Is Empty!", "Please Fill Supplier Name Text Field!");

            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatementPurchasing = null;
        PreparedStatement preparedStatementBooks = null;
        ResultSet resultSet = null;

        try{
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            preparedStatementPurchasing = connection.prepareStatement("INSERT INTO purchasings(supplier_name, date, amount) VALUES(?, NOW(), ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatementPurchasing.setString(1, supplierNameTf.getText());
            preparedStatementPurchasing.setDouble(2, totalAmount);

            int affectedRows = preparedStatementPurchasing.executeUpdate();

            if(affectedRows > 0){
                resultSet = preparedStatementPurchasing.getGeneratedKeys();

                if(resultSet.next()){
                    int purchasingId = resultSet.getInt(1);

                    preparedStatementBooks = connection.prepareStatement("INSERT INTO books(name, availability, category_id, bought_price, purchasing_id) VALUES(?, ?, ?, ?, ?)");
                    
                    for (Book book : books) {
                        preparedStatementBooks.setString(1, book.getName());
                        preparedStatementBooks.setInt(2, 1);
                        preparedStatementBooks.setInt(3, book.getCategory().getId());
                        preparedStatementBooks.setDouble(4, book.getBoughtPrice());
                        preparedStatementBooks.setInt(5, purchasingId);
    
                        preparedStatementBooks.executeUpdate();
                    }
                    
                    connection.commit();

                    AlertTools.AlertInformation("Success!", "Purchase Is Successfully Added!", "Purchase Is Successfully Added!");

                    everythingDefault();
                }
            }else{
                AlertTools.AlertError("Error!", "Purchasing Is Not Added!", "Please Try Again!");

                connection.rollback();

                everythingDefault();

                return;
            }
        }catch(Exception e){
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            everythingDefault();
        }finally {
            try {
                if(resultSet != null)  resultSet.close();
                if(preparedStatementBooks != null) preparedStatementBooks.close();
                if(preparedStatementPurchasing != null) preparedStatementPurchasing.close();
                if(connection != null) connection.close();
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }

    private void setCategoryCb() {
        try{
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM categories");

            while(resultSet.next()){
                categoryCb.getItems().add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
            }

            DatabaseTools.closeQueryOperation(connection, statement, resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshTable(){
        table.getItems().clear();
        table.getItems().addAll(books);
    }

    private void defaultAddBookTf(){
        bookNameTf.setText("");
        priceTf.setText("");
        categoryCb.getSelectionModel().clearSelection();
    }

    private void totalAmountRefresh(double totalAmount){
        this.totalAmount += totalAmount;

        totalAmountText.setText("$ " + String.valueOf(this.totalAmount));
    }

    private void everythingDefault(){
        this.totalAmount = 0;
        totalAmountText.setText("$ " + String.valueOf(totalAmount));

        books.clear();
        refreshTable();

        defaultAddBookTf();

        supplierNameTf.setText("");
    }

}
