package tools;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertTools {
    /**
     * AlertError
     * 
     * @param title   the title of the alert
     * @param header  the header of the alert
     * @param content the content of the alert
     */
    public static void AlertError(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Alert Information
     * 
     * @param title   the title of the alert
     * @param header  the header of the alert
     * @param content the content of the alert
     */
    public static void AlertInformation(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Alert Confirmation
     * 
     * @param title   the title of the alert
     * @param header  the header of the alert
     * @param content the content of the alert
     * @return the result of the alert
     */
    public static Optional<ButtonType> AlertConfirmation(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        return alert.showAndWait();
    }

    public static void AlertErrorContactSupport() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText("Contact Support!");
        alert.setContentText("Please contact support!");

        alert.showAndWait();
    }

}
