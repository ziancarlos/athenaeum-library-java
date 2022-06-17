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

    /**
     * check text field is empty or null
     * 
     * @param phoneNumber phone number we wanna check the format
     * @return Return true if the phone number is valid
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        String[] splittedPhoneNumber = phoneNumber.split("");
        String countryCode = phoneNumber.substring(0, 4);

        if (!countryCode.equals("+628")) {
            return false;
        }

        phoneNumber = "";
        for (int i = 4; i < splittedPhoneNumber.length; i++) {
            phoneNumber += splittedPhoneNumber[i];
        }

        if (phoneNumber.matches("[0-9]+") && phoneNumber.length() > 8 && phoneNumber.length() < 12) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check if the given text is valid with the given minimum and maximum length
     * 
     * @param min minimum length of the text
     * @param max maximum length of the text
     * @return true if the text is valid
     * 
     */
    public static boolean isTextIsValid(int min, int max, String text) {
        if (text.length() < min || text.length() > max) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * check if the given string is double
     * 
     * @return true if the given string is double
     * 
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * check if the double is negative or below 0
     * 
     * @return true if the double is negative or below 0
     * 
     */
    public static boolean isDoubleIsNegative(double value) {
        if (value < 0) {
            return true;
        } else {
            return false;
        }
    }

}
