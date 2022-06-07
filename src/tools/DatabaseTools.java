package tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.mysql.cj.jdbc.Driver;

public class DatabaseTools {

    /**
     * Creating new connection to database
     * 
     * @return the connection
     * 
     */
    public static java.sql.Connection getConnection() {
        String databaseName = "";
        String databaseUsername = "";
        String databasePassword = "";

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/config.properties"));

            databaseName = properties.getProperty("database.name");
            databaseUsername = properties.getProperty("database.username");
            databasePassword = properties.getProperty("database.password");

        } catch (FileNotFoundException exception) {
            AlertTools.AlertErrorContactSupport();
        } catch (IOException exception) {
            AlertTools.AlertErrorContactSupport();
        }

        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }

        java.sql.Connection conn = null;
        try {
            conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUsername,
                    databasePassword);
        } catch (Exception e) {
            AlertTools.AlertErrorContactSupport();
        }
        return conn;
    }

    /**
     * Closing the connection to database
     * 
     * @param conn      the connection
     * @param statement the statement
     * @param resultSet the result set
     * 
     */
    public static void closeQueryOperation(java.sql.Connection conn, Statement statement, ResultSet resultSet) {
        try {
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    /**
     * Closing the connection to database
     * 
     * @param conn      the connection
     * @param statement the statement
     * 
     */
    public static void closeQueryOperation(java.sql.Connection conn, Statement statement) {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    /**
     * Closing the connection to database with prepare statement
     * 
     * @param conn              the connection
     * @param preparedStatement the prepared statement
     * @param resultSet         the result set
     * 
     */
    public static void closeQueryOperationWithPreparedStatement(java.sql.Connection conn, PreparedStatement statement,
            ResultSet resultSet) {
        try {
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

    /**
     * Closing the connection to database with prepare statement
     * 
     * @param conn              the connection
     * @param preparedStatement the prepared statement
     * 
     */
    public static void closeQueryOperationWithPreparedStatement(java.sql.Connection conn, PreparedStatement statement) {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            AlertTools.AlertErrorContactSupport();
        }
    }

}