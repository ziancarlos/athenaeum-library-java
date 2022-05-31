package tools;

import javafx.scene.control.TextField;

public class UiTools {
    public static void setTextFieldEmpty(TextField... tf) {
        for (TextField textField : tf) {
            textField.setText("");
        }
    }
}
