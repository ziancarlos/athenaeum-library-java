package tools;

import javafx.scene.control.TextField;

public class ValidationTools {
    /**
     * check text field is empty or null
     * 
     * @param tf which tf where we wanna check
     * @return Return true if the value of the textfield consist of null or blank
     * @return Return false if the value is not null or not blank
     */
    public static boolean isTextFieldEmptyOrNull(TextField... tf) {
        for (TextField textField : tf) {
            if (textField.getText() == null || textField.getText().isBlank()) {
                return true;
            }
        }
        return false;
    }
}
