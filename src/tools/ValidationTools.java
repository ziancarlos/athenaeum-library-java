package tools;

import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static boolean isDateOlderThanOneDay(String purchasingDate) {
        String[] splittedPurchasingDate = purchasingDate.split("-");
        String year = splittedPurchasingDate[0];
        String month = splittedPurchasingDate[1];
        String day = splittedPurchasingDate[2];

        String[] splittedToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");

        String todayYear = splittedToday[0];
        String todayMonth = splittedToday[1];
        String todayDay = splittedToday[2];

        if (Integer.parseInt(year) < Integer.parseInt(todayYear)) {
            return true;
        } else if (Integer.parseInt(year) == Integer.parseInt(todayYear)) {
            if (Integer.parseInt(month) < Integer.parseInt(todayMonth)) {
                return true;
            } else if (Integer.parseInt(month) == Integer.parseInt(todayMonth)) {
                if (Integer.parseInt(day) < Integer.parseInt(todayDay)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}
