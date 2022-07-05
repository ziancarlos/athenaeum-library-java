package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Procedure {
    public static void fineAllLateBorrowings() {
        Connection connection = null;
        PreparedStatement statementSelectBorrowing = null;
        PreparedStatement statementSelectBorrowedBooks = null;
        PreparedStatement statementCheckDateDiff = null;
        PreparedStatement statementCheckPenalties = null;
        PreparedStatement statementInsertPenalties = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        ResultSet resultSet4 = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);

            statementSelectBorrowing = connection.prepareStatement(
                    "SELECT * FROM borrowings WHERE status != 'completed' LOCK IN SHARE MODE");

            resultSet = statementSelectBorrowing.executeQuery();
            while (resultSet.next()) {
                statementSelectBorrowedBooks = connection.prepareStatement(
                        "SELECT * FROM borrowed_books WHERE borrowing_id = ? AND status = 'on-going' LOCK IN SHARE MODE");
                statementSelectBorrowedBooks.setInt(1, resultSet.getInt("id"));

                resultSet2 = statementSelectBorrowedBooks.executeQuery();
                while (resultSet2.next()) {
                    statementCheckDateDiff = connection.prepareStatement(
                            "SELECT DATEDIFF(NOW(), ?) AS dateDiff");
                    statementCheckDateDiff.setString(1, resultSet2.getString("end_date"));

                    resultSet3 = statementCheckDateDiff.executeQuery();
                    if (resultSet3.next()) {

                        int dateDiff = resultSet3.getInt("dateDiff");
                        int dateInserted = 0;
                        int temp;
                        if (dateDiff > 0) {
                            statementCheckPenalties = connection.prepareStatement(
                                    "SELECT COUNT(*)  AS count FROM penalties WHERE borrowed_book_borrowing_id = ? AND borrowed_book_book_id = ? AND borrowed_book_borrowing_customer_id = ?");
                            statementCheckPenalties.setInt(
                                    1, resultSet2.getInt("borrowing_id"));
                            statementCheckPenalties.setInt(
                                    2, resultSet2.getInt("book_id"));
                            statementCheckPenalties.setInt(
                                    3, resultSet2.getInt("borrowing_customer_id"));

                            resultSet4 = statementCheckPenalties.executeQuery();
                            if (resultSet4.next()) {

                                dateInserted = resultSet4.getInt("count");
                            }

                            if (dateDiff != dateInserted) {
                                temp = dateDiff - dateInserted;

                                for (int i = 1; i <= temp; i++) {
                                    // create old date in string format
                                    String dateBefore = resultSet2.getString("end_date");

                                    // create instance of the SimpleDateFormat that matches the given date
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                    // create instance of the Calendar class and set the date to the given date
                                    Calendar cal = Calendar.getInstance();
                                    try {
                                        cal.setTime(sdf.parse(dateBefore));
                                    } catch (ParseException e) {
                                        AlertTools.showAlertError("Error!", e.getMessage());

                                        connection.rollback();
                                    }

                                    // use add() method to add the days to the given date
                                    cal.add(Calendar.DATE, i + dateInserted);

                                    String dateAfter = sdf.format(cal.getTime());

                                    statementInsertPenalties = connection.prepareStatement(
                                            "INSERT INTO penalties(penalty_type, penalty_date, payment_status, borrowed_book_book_id, borrowed_book_borrowing_customer_id, borrowed_book_borrowing_id, amount) VALUES ('late', ?, 'unpaid', ?, ?, ?, ?)",
                                            PreparedStatement.RETURN_GENERATED_KEYS);
                                    statementInsertPenalties.setString(1, dateAfter);
                                    statementInsertPenalties.setInt(2, resultSet2.getInt("book_id"));
                                    statementInsertPenalties.setInt(3, resultSet2.getInt("borrowing_customer_id"));
                                    statementInsertPenalties.setInt(4, resultSet2.getInt("borrowing_id"));
                                    statementInsertPenalties.setDouble(5, 5);

                                    affectedRows = statementInsertPenalties.executeUpdate();

                                    if (affectedRows == 0) {
                                        connection.rollback();
                                        break;
                                    }
                                }

                                connection.commit();
                            } else {
                                break;
                            }

                        } else {
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                AlertTools.showAlertError("Error!", e.getMessage());
            }

            e.printStackTrace();
        } finally {

            try {
                if (connection != null) {
                    connection.close();
                }
                if (statementCheckDateDiff != null) {
                    statementCheckDateDiff.close();
                }
                if (statementCheckPenalties != null) {
                    statementCheckPenalties.close();
                }
                if (statementInsertPenalties != null) {
                    statementInsertPenalties.close();
                }
                if (statementSelectBorrowedBooks != null) {
                    statementSelectBorrowedBooks.close();
                }
                if (statementSelectBorrowing != null) {
                    statementSelectBorrowing.close();
                }
                if (resultSet2 != null) {
                    resultSet2.close();
                }
                if (resultSet3 != null) {
                    resultSet3.close();
                }
                if (resultSet4 != null) {
                    resultSet4.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Error", e.getMessage());
            }
        }
    }
}
