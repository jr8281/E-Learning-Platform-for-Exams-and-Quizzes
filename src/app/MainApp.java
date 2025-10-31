package app;

import ui.LoginFrame;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Launch GUI in the Event Dispatch Thread (recommended for Swing)
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

