package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.BookkeepingBorrowing;
import model.BookkeepingPenalty;
import model.BookkeepingPurchasing;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class BookkeepingDetailController {

    @FXML
    private TableColumn<?, ?> amountColBorrowing;

    @FXML
    private TableColumn<?, ?> amountColPurchasing;

    @FXML
    private TableColumn<?, ?> bookColBorrowing;

    @FXML
    private TableColumn<?, ?> bookNameColPurchasing;

    @FXML
    private TableView<?> tableBorrowing;

    @FXML
    private TableColumn<?, ?> categoryColPurchasing;

    @FXML
    private TableColumn<?, ?> dueDateColBorrowing;

    @FXML
    private TableColumn<?, ?> statusColBorrowing;

    @FXML
    private TableView<?> tablePurchasing;

    @FXML
    private ListView<String> lv;

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    void setBookkeepingPenalty(BookkeepingPenalty bookkeepingPenalty) {
        tablePurchasing.setVisible(false);
        tableBorrowing.setVisible(false);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM penalties INNER JOIN books ON penalties.borrowed_book_book_id = books.id INNER JOIN users ON penalties.borrowed_book_borrowing_customer_id = users.id INNER JOIN borrowings ON penalties.borrowed_book_borrowing_id = borrowings.id WHERE penalties.id = ? ");
            statement.setInt(1, bookkeepingPenalty.getPenaltyId());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lv.getItems().add("Penalties Id : ");
                lv.getItems().add("Transaction Type : ");
                lv.getItems().add("Amount : ");
                lv.getItems().add("Penalty Date : ");
                lv.getItems().add("");
                lv.getItems().add("Customer Id : ");
                lv.getItems().add("Customer Name : ");
                lv.getItems().add("Customer Phone Number : ");
                lv.getItems().add("");
                lv.getItems().add("Book Id : ");
                lv.getItems().add("Book Name : ");
                lv.getItems().add("Borrowing Status : ");
                lv.getItems().add("End Borrowing Date : ");
                lv.getItems().add("Returned Date : ");
            } else {

            }
        } catch (Exception e) {
            AlertTools.showAlertError("Error!", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception e) {
                AlertTools.showAlertError("Error!", e.getMessage());

                e.printStackTrace();
            }
        }
    }

    void setBookkeepingBorrowing(BookkeepingBorrowing bookkeepingBorrowing) {
        tablePurchasing.setVisible(false);
    }

    void setBookkeepingPurchasing(BookkeepingPurchasing bookkeepingPurchasing) {
        tableBorrowing.setVisible(false);

    }

}
