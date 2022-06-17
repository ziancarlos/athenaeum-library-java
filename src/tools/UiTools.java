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

    /**
     * capitalize first letter of each word
     * 
     * @param str the string to be capitalized
     * @return all first letter of each word capitalize
     * 
     */
    public static String capitalizeWord(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }

    /**
     * set the date picker to null
     * 
     * @param dp the date picker to be set null
     * 
     */
    public static void setDatePickerNull(javafx.scene.control.DatePicker... dp) {
        for (javafx.scene.control.DatePicker datePicker : dp) {
            datePicker.setValue(null);
        }
    }
}
