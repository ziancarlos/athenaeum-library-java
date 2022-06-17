package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Libarian;
import tools.AlertTools;
import tools.BackBtn;
import tools.DatabaseTools;
import tools.SwitchSceneTools;
import tools.UiTools;
import tools.ValidationTools;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

public class LibarianController {

    @FXML
    private TableColumn<?, ?> activeCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TextField searchTf;

    @FXML
    private TableView<Libarian> table;

    @FXML
    private TableColumn<?, ?> usernameCol;

    @FXML
    private TableColumn<?, ?> createdAtCol;

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));

        setTable();
    }

    @FXML
    void addOnAction(ActionEvent event) {
        SwitchSceneTools.changeSceneActionEvent(event, "../view/libarian-add-page.fxml");

        BackBtn.addToBackBtnStack("../view/libarians-page.fxml");
    }

    @FXML
    void backOnAction(ActionEvent event) {
        BackBtn.backBtnActionEvent(event);
    }

    @FXML
    void deactivateOnAction(ActionEvent event) {
        Libarian libarian = table.getSelectionModel().getSelectedItem();

        if (libarian == null) {
            AlertTools.showAlertError("Please select a libarian to deactivate", "Error");

            setTable();

            return;
        }

        Connection connection = null;
        PreparedStatement statementSelect = null;
        PreparedStatement statementUpdate = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);
            statementSelect = connection.prepareStatement("SELECT * FROM libarians WHERE id = ? FOR UPDATE");
            statementSelect.setInt(1, libarian.getId());

            resultSet = statementSelect.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("active").equals("active")) {
                    statementUpdate = connection
                            .prepareStatement("UPDATE libarians SET active = 'unactive' WHERE id = ?");
                    statementUpdate.setInt(1, libarian.getId());

                    affectedRows = statementUpdate.executeUpdate();

                    if (affectedRows > 0) {
                        connection.commit();

                        AlertTools.showAlertConfirmationWithOptional("Libarian deactivated successfully",
                                "Libarian deactivated successfully");

                        setTable();
                    } else {
                        connection.rollback();
                        AlertTools.showAlertError("Error deactivating libarian", "Error deactivating libarian");
                    }

                } else {
                    connection.rollback();

                    AlertTools.showAlertError("Libarian is already deactivated", "Libarian is already deactivated");
                }

            } else {
                connection.rollback();

                AlertTools.showAlertError("Error deactivating libarian", "Error deactivating libarian");
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");

            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statementSelect != null) {
                    statementSelect.close();
                }
                if (statementUpdate != null) {
                    statementUpdate.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }

    }

    @FXML
    void activateOnAction(ActionEvent event) {
        Libarian libarian = table.getSelectionModel().getSelectedItem();

        if (libarian == null) {
            AlertTools.showAlertError("Please select a libarian to deactivate", "Error");

            setTable();

            return;
        }

        Connection connection = null;
        PreparedStatement statementSelect = null;
        PreparedStatement statementUpdate = null;
        ResultSet resultSet = null;
        int affectedRows;

        try {
            connection = DatabaseTools.getConnection();
            connection.setAutoCommit(false);
            statementSelect = connection.prepareStatement("SELECT * FROM libarians WHERE id = ? FOR UPDATE");
            statementSelect.setInt(1, libarian.getId());

            resultSet = statementSelect.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("active").equals("unactive")) {
                    statementUpdate = connection
                            .prepareStatement("UPDATE libarians SET active = 'active' WHERE id = ?");
                    statementUpdate.setInt(1, libarian.getId());

                    affectedRows = statementUpdate.executeUpdate();

                    if (affectedRows > 0) {
                        connection.commit();

                        AlertTools.showAlertConfirmationWithOptional("Libarian activated successfully",
                                "Libarian activated successfully");

                        setTable();
                    } else {
                        connection.rollback();

                        AlertTools.showAlertError("Error activating libarian", "Error activating libarian");
                    }

                } else {
                    connection.rollback();

                    AlertTools.showAlertError("Libarian is already activated", "Libarian is already activated");
                }

            } else {
                connection.rollback();

                AlertTools.showAlertError("Error activating libarian", "Error activating libarian");
            }

        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");

            try {
                connection.rollback();
            } catch (Exception e1) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statementSelect != null) {
                    statementSelect.close();
                }
                if (statementUpdate != null) {
                    statementUpdate.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        Libarian libarian = table.getSelectionModel().getSelectedItem();

        if (libarian == null) {
            AlertTools.showAlertError("Please select a libarian to delete", "Error");
            return;
        }

        if (AlertTools.showAlertConfirmationWithOptional("Are you sure you want to delete this libarian ? ", null)
                .get() == ButtonType.OK) {

            if (Libarian.deleteLibarian(libarian)) {
                AlertTools.showAlertInformation("Libarian deleted successfully", "Success");
            } else {
                AlertTools.showAlertError("Error deleting libarian", "Error");
            }

        }

        setTable();
    }

    @FXML
    void editOnAction(ActionEvent event) {
        Libarian libarian = table.getSelectionModel().getSelectedItem();

        if (libarian == null) {
            AlertTools.showAlertError("Please select a libarian to edit", "Error");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/libarian-edit-page.fxml"));

            Parent root = loader.load();

            LibarianEditController controller = loader.getController();

            controller.setLibarian(libarian);

            BackBtn.addToBackBtnStack("../view/libarians-page.fxml");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void refreshOnAction(ActionEvent event) {
        UiTools.setTextFieldEmpty(searchTf);

        setTable();
    }

    private void setTable() {
        table.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM libarians");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Libarian(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("created_at"),
                        resultSet.getString("active")));
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        if (ValidationTools.isTextFieldEmptyOrNull(searchTf)) {
            AlertTools.showAlertError("Search field is empty!", "Please enter a search term!");
            return;
        }

        table.getItems().clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseTools.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM libarians WHERE username LIKE ?");
            preparedStatement.setString(1, "%" + searchTf.getText() + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                table.getItems().add(new Libarian(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("created_at"),
                        resultSet.getString("active")));
            }
        } catch (Exception e) {
            AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                AlertTools.showAlertError("Database connectivity problem!", "Contact support!");
            }
        }
    }

}
