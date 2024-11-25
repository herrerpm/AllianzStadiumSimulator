import Managers.GraphicsManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicsManager.getInstance().initialize(); // Initialize the GUI
            new SimulationWindow();
            new InputWindow(); // Show the input window
            new TableStates();
        });
    }
}
