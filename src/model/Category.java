package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.AlertTools;
import tools.DatabaseTools;

public class Category {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty connectedBooks;

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

    public void setId(int id) {
        this.id.set(id);
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
     * Check if category name exist in the database
     * 
     * @param name  name to be checked
     * @return return true if exist
     */
    public static boolean isCategoryNameExist(String name){
        try{
            Connection connection = DatabaseTools.getConnection();

            String sql = "SELECT * FROM categories WHERE name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
    
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement, resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * check name if category exist, exclude the given id
     * 
     * @param name  name to be checked
     * @return return true if exist
     */
    public static boolean isCategoryNameExist(String name, int id){
        try{
            Connection connection = DatabaseTools.getConnection();

            String sql = "SELECT * FROM categories WHERE name = ? AND id != ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
    
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement, resultSet);
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * edit the given category with the new name
     * 
     * @param category the row of category to be edited
     * @param newName the given new name
     * @return return true if success
    */
    public static boolean editCategory(Category category, String newName) {
        try {
            Connection connection = DatabaseTools.getConnection();

            String sql = "UPDATE categories SET name = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, category.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                category.setName(newName);

                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

     /**
     * add new category
     * 
     * @param name the given name
     * @return return true if success
    */
    public static boolean addCategory(String name) {
        try {
            Connection connection = DatabaseTools.getConnection();

            String sql = "INSERT INTO categories (name) VALUES (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, name);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

     /**
     * delete the given category
     * 
     * @param category the row of category to be deleted
     * @return return true if success
    */
    public static boolean deleteCategory(Category category) {
        try {
            Connection connection = DatabaseTools.getConnection();

            String sql = "DELETE FROM categories WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, category.getId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }

            DatabaseTools.closeQueryOperation(connection, preparedStatement);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
