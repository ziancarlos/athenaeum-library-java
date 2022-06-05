package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Purchasing;
import tools.AlertTools;
import tools.BackBtnTools;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.ValidationTools;

public class PurchasingController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableColumn<Purchasing, Integer> idCol;

    @FXML
    private TableColumn<Purchasing, String> purchasingDate;

    @FXML
    private TableColumn<Purchasing, String> supplierNameCol;

    @FXML
    private TableView<Purchasing> table;

    @FXML
    private TableColumn<Purchasing, Double> totalAmountCol;

    @FXML
    private TableColumn<Purchasing, String> totalBooksBoughtCol;

    @FXML
    private TextField searchTf;

    public void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasingDate.setCellValueFactory(new PropertyValueFactory<>("purchasingDate"));
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalBooksBoughtCol.setCellValueFactory(new PropertyValueFactory<>("totalBooksBought"));

        try{
            Connection connection = DatabaseTools.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id");

            while(resultSet.next()){
                Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"), resultSet.getString("purchasings.date"), 
                resultSet.getString("purchasings.supplier_name"),resultSet.getInt("totalBooksBought") , resultSet.getDouble("purchasings.amount") );
                table.getItems().add(purchasing);
            }

            DatabaseTools.closeQueryOperation(connection, statement,resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void backBtn(ActionEvent event) {
        BackBtnTools.backBtnActionEvent(event);
    }

    @FXML
    void editPurchasingBtn(ActionEvent event) {
        
    }
    
    @FXML
    void startDate(ActionEvent event) {
        if(endDatePicker.getValue() == null){
            return;
        }
        
        try{
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id HAVING purchasings.date BETWEEN ? AND ?");
            statement.setString(1, startDatePicker.getValue().toString());
            statement.setString(2, endDatePicker.getValue().toString());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"), resultSet.getString("purchasings.date"), 
                resultSet.getString("purchasings.supplier_name"),resultSet.getInt("totalBooksBought") , resultSet.getDouble("purchasings.amount") );
                table.getItems().add(purchasing);
            }

            DatabaseTools.closeQueryOperation(connection, statement,resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }
   
    }

    @FXML
    void endDate(ActionEvent event) {
        if(startDatePicker.getValue() == null){
            return;
        }

        try{
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id HAVING purchasings.date BETWEEN ? AND ?");
            statement.setString(1, startDatePicker.getValue().toString());
            statement.setString(2, endDatePicker.getValue().toString());
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"), resultSet.getString("purchasings.date"), 
                resultSet.getString("purchasings.supplier_name"),resultSet.getInt("totalBooksBought") , resultSet.getDouble("purchasings.amount") );
                table.getItems().add(purchasing);
            }

            DatabaseTools.closeQueryOperation(connection, statement,resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void newPurchasingBtn(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/purchasing-new-page.fxml");

        BackBtnTools.addToBackBtnStack("../view/purchasings-page.fxml");
    }

    @FXML
    void purchasingDetailBtn(ActionEvent event) {
        
    }

    @FXML
    void searchBtn(ActionEvent event) {
        if(ValidationTools.isTextFieldEmptyOrNull(searchTf)){
            AlertTools.AlertError("Error!", "Search Text Field Is Empty!", null);

            return;
        }

        table.getItems().clear();

        try{
            Connection connection = DatabaseTools.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT purchasings.id, purchasings.supplier_name, purchasings.date, purchasings.amount, COUNT(books.purchasing_id) AS totalBooksBought FROM purchasings INNER JOIN books ON purchasings.id = books.purchasing_id  GROUP BY purchasings.id HAVING purchasings.id LIKE ?");
            statement.setString(1, "%" + searchTf.getText() + "%");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Purchasing purchasing = new Purchasing(resultSet.getInt("purchasings.id"), resultSet.getString("purchasings.date"), 
                resultSet.getString("purchasings.supplier_name"),resultSet.getInt("totalBooksBought") , resultSet.getDouble("purchasings.amount") );
                table.getItems().add(purchasing);
            }

            DatabaseTools.closeQueryOperation(connection, statement,resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

  

}
