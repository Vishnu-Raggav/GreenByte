package Controllers;

import javax.swing.*;

/** Small helpers shared by the controllers. */
public class Validation {

    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("^[+]?[0-9 \\-]{7,15}$");
    }

    public static void error(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public static void info(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Please confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
