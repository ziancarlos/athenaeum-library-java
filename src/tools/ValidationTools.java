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


    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
