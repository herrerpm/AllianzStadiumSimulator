import Agents.FanAgent;
import Managers.GraphicsManager;
import Managers.ThreadManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphicsManager.getInstance().initialize(); // Initialize the GUI
        });
    }
}
