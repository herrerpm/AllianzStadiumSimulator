package Managers;

import Agents.FanAgent;
import Agents.PlayerAgent;
import Agents.TicketSellerAgent;
import Handlers.FanHandler;
import Handlers.PlayerHandler;
import Handlers.SellingHandler;
import Handlers.SystemHandler;

import javax.swing.*;
import java.awt.*;

public class GraphicsManager {
    private static final int UPDATE_INTERVAL = SystemHandler.getInstance().getInputVariable("UpdateInterval"); // Intervalo de actualización en milisegundos

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

        // Configura un Timer para actualizar posiciones y repintar
        Timer timer = new Timer(UPDATE_INTERVAL, e -> {
            updateAgentPositions();
            triggerRepaint();
        });
        timer.start();

        // Make the JFrame visible
        frame.setVisible(true);
    }

    private void updateAgentPositions() {
        // Actualiza las posiciones de los FanAgents
        for (FanAgent fan : FanHandler.getInstance().getAgents()) {
            fan.updatePosition();
        }
        // Actualiza las posiciones de los PlayerAgents
        for (PlayerAgent player : PlayerHandler.getInstance().getAgents()) {
            player.updatePosition();
        }
        // Actualiza las posiciones de los TicketSellerAgents
        for (TicketSellerAgent seller : SellingHandler.getInstance().getAgents()) {
            seller.updatePosition();
        }
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