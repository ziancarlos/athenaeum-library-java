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
            System.err.println("Gagal Load File Dari Url");
        } catch (IOException exception) {
            System.err.println("Gagal Load Data Dari Url");
        }

        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Driver not found");
        }

        java.sql.Connection conn = null;
        try {
            conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUsername,
                    databasePassword);
        } catch (Exception e) {
            System.err.println("Connection To Database Error");
        }
        return conn;
    }

    public static void closeQueryOperation(java.sql.Connection conn, Statement statement, ResultSet resultSet) {
        try {
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Connection Close Error");
        }
    }

    public static void closeQueryOperation(java.sql.Connection conn, Statement statement) {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Connection Close Error");
        }
    }

    public static void closeQueryOperationWithPreparedStatement(java.sql.Connection conn, PreparedStatement statement,
            ResultSet resultSet) {
        try {
            conn.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Connection Close Error");
        }
    }

    public static void closeQueryOperationWithPreparedStatement(java.sql.Connection conn, PreparedStatement statement) {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Connection Close Error");
        }
    }

}