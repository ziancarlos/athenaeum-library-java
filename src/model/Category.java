package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.DatabaseTools;
import tools.UiTools;

public class Category {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty connectedBooks;
    private ArrayList<Book> books = null;

    public Category(int id, String name, int connectedBooks) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.connectedBooks = new SimpleIntegerProperty(connectedBooks);
    }

    public Category(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getConnectedBooks() {
        return connectedBooks.get();
    }

    public void setConnectedBooks(int connectedBooks) {
        this.connectedBooks.set(connectedBooks);
    }

    /**
     * all books in the database related to the category
     * 
     * @return all books in the database related to the category
     * 
     */
    public ArrayList<Book> getAllBooks() {
        books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM books WHERE category_id = ?");
            statement.setInt(1, id.get());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt("id"), resultSet.getString("name")));
            }

            return books;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                return null;
            }
        }

    }

    /**
     * delete category with the given object
     * 
     * @return true if the transaction success, false otherwise
     * 
     */
    public static boolean deleteCategory(Category category) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "DELETE FROM categories WHERE id = ?");
            statement.setInt(1, category.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                return false;
            }

        }
    }

    /**
     * add new category
     * 
     * @param name new category name
     * 
     * @return all books in the database related to the category
     * 
     */
    public static boolean addNewCategory(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "INSERT INTO categories (name) VALUES (?)");
            statement.setString(1, UiTools.capitalizeWord(name));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                return false;
            }

        }
    }

    /**
     * check if the category name is already in the database but not the same
     * 
     * @param category row to not check
     * @param name     name to check
     * 
     * @return true if the category name is already in the database.
     * 
     */
    public static boolean isExistWithout(Category category, String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.prepareStatement(
                    "SELECT * FROM categories WHERE name = ? AND id != ?");
            statement.setString(1, name);
            statement.setInt(2, category.getId());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                return false;
            }

        }
    }

    /**
     * get all categories id and name
     * 
     * @return as arraylist of categoriess id and name
     * 
     */
    public static ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM categories");

            while (resultSet.next()) {
                categories.add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
            }

            return categories;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                return null;
            }
        }
    }

    @Override
    public String toString() {
        return name.get();
    }

}
