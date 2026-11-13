import javax.swing.*;
import Views.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
