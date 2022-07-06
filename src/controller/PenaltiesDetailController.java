package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Penalties;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;

public class PenaltiesDetailController {

    @FXML
    private ListView<String> lv;

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    public void setPenalties(Penalties penalties) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM penalties  INNER JOIN users ON penalties.borrowed_book_borrowing_customer_id = users.id INNER JOIN books ON borrowed_book_book_id = books.id  INNER JOIN borrowings ON penalties.borrowed_book_borrowing_id = borrowings.id WHERE penalties.id = ?");
            statement.setInt(1, penalties.getId());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lv.getItems().add("Penalties Id : " + penalties.getId());
                lv.getItems().add("Penalty Date : " + resultSet.getString("penalties.penalty_date"));
                lv.getItems().add("Penalty Type : " + resultSet.getString("penalties.penalty_type"));
                lv.getItems().add("Penalty Payment Status : " + resultSet.getString("penalties.payment_status"));
                lv.getItems().add("Penalty Amount : $" + resultSet.getDouble("penalties.amount"));
                lv.getItems().add(" ");
                lv.getItems().add("Customer Username : " + resultSet.getString("users.username"));
                lv.getItems().add("Customer Phone Number : " + resultSet.getString("users.phone_number"));
                lv.getItems().add(" ");
                lv.getItems().add("Book Id : " + resultSet.getString("books.id"));
                lv.getItems().add("Book Name : " + resultSet.getString("books.name"));
                lv.getItems().add(" ");
                lv.getItems().add("Borrowing Id : " + resultSet.getString("borrowings.id"));
                lv.getItems().add("Borrowing Status : " + resultSet.getString("borrowings.status"));
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
}
