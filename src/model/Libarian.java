package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import tools.DatabaseTools;

public class Libarian extends User {
    private SimpleStringProperty active;

    public Libarian(int id, String username, String password, String role, String createdAt, String active) {
        super(id, username, password, role, createdAt);
        this.active = new SimpleStringProperty(active);
    }

    public String getActive() {
        return active.get();
    }

    public void setActive(String active) {
        this.active.set(active);
    }

    /**
     * delete libarian with given object
     * 
     * @return true if the libarian is deleted
     * 
     */
    public static boolean deleteLibarian(Libarian libarian) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM libarians WHERE id = ?");
            preparedStatement.setInt(1, libarian.getId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
