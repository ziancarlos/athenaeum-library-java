package tools;

import javafx.scene.control.TextField;

public class UiTools {

    /**
     * set all text field empty
     * 
     * @param tf the text field to be set empty
     * 
     */
    public static void setTextFieldEmpty(TextField... tf) {
        for (TextField textField : tf) {
            textField.setText("");
        }
    }
}
