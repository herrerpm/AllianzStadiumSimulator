package Managers;

import Agents.FanAgent;
import Agents.PlayerAgent;
import Agents.TicketSellerAgent;
import Handlers.FanHandler;
import Handlers.PlayerHandler;
import Handlers.SellingHandler;

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

    // Draws all agents by iterating over them
    private void drawGraphics(Graphics g) {
        // Draw FanAgents
        g.setColor(Color.BLUE);
        for (FanAgent fan : FanHandler.getInstance().getAgents()) {
            fan.draw(g);
        }

        // Draw PlayerAgents
        g.setColor(Color.RED);
        for (PlayerAgent player : PlayerHandler.getInstance().getAgents()) {
            player.draw(g);
        }

        // Draw TicketSellerAgents
        g.setColor(Color.GREEN);
        for (TicketSellerAgent seller : SellingHandler.getInstance().getAgents()) {
            seller.draw(g);
        }
    }


    // Method to trigger a repaint of the drawing panel
    public void triggerRepaint() {
        SwingUtilities.invokeLater(() -> drawPanel.repaint());
    }
}
