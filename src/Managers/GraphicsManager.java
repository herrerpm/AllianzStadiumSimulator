package Managers;

import Agents.FanAgent;

import javax.swing.*;
import java.awt.*;

public class GraphicsManager {
    private static volatile GraphicsManager instance;
    private JFrame frame;
    private JPanel drawPanel;

    // Private constructor to enforce singleton
    private GraphicsManager() {}

    // Public method to get the singleton instance with double-checked locking for thread safety
    public static GraphicsManager getInstance() {
        if (instance == null) {
            synchronized (GraphicsManager.class) {
                if (instance == null) {
                    instance = new GraphicsManager();
                }
            }
        }
        return instance;
    }

    // Initialize the GUI components
    public void initialize() {
        // Create and configure the JFrame
        frame = new JFrame("Visualización de Estados de Threads");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create and configure the drawing panel
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraphics(g);
            }
        };
        drawPanel.setBackground(Color.WHITE); // Optional: Set background color
        frame.add(drawPanel);

        // Make the JFrame visible
        frame.setVisible(true);
    }

    // Example method for drawing custom graphics
    private void drawGraphics(Graphics g) {
        FanAgent wujiBiFan = new FanAgent("dany", 100);
        g.setColor(Color.BLUE);
        wujiBiFan.draw();
    }

    /**
     * Exposes the Graphics object for external manipulation.
     * @return the Graphics object of the drawing panel.
     */
    public Graphics getGraphics() {
        return drawPanel.getGraphics();
    }
}
