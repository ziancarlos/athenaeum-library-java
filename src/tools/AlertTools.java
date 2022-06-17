package tools;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertTools {

    /**
     * Show Information Alert
     * 
     * @param header  the header of the alert
     * @param content the content of the alert
     * 
     */
    public static void showAlertInformation(String header, String content) {

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show Warning Alert
     * 
     * @param header  the header of the alert
     * @param content the content of the alert
     * 
     */
    public static void showAlertError(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show Warning Alert
     * 
     * @param header  the header of the alert
     * @param content the content of the alert
     * 
     */
    public static void showAlertWarning(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show Confirmation Alert
     * 
     * @param header  the header of the alert
     * @param content the content of the alert
     * 
     */
    public static void showAlertConfirmation(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showAlertConfirmationWithOptional(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);

        return alert.showAndWait();
    }

}
