// File: src/Windows/ModeSelectorWindow.java

package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import Handlers.SystemHandler;
import Network.Client;
import Network.ConnectionListener;
import Network.Server;
import Tables.TableStates;
import Managers.GraphicsManager;

/**
 * ModeSelectorWindow is responsible for allowing the user to select the mode of operation:
 * Server, Client, or Local. Based on the selection, it collects necessary inputs and
 * starts the appropriate components. It listens for connection events to transition
 * to the main simulation windows.
 */
public class ModeSelectorWindow implements ConnectionListener {

    // References to Server and Client instances
    private Server serverInstance;
    private Client clientInstance;

    // Main frame reference to allow disposal
    private JFrame frame;

    public ModeSelectorWindow(){
        this.createAndShowGUI();
    }

    // Method to create and display the GUI
    private void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("Mode Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null); // Center on screen

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Instruction label
        JLabel label = new JLabel("Select Mode to Start:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        // Buttons for mode selection
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton serverButton = new JButton("Server");
        JButton clientButton = new JButton("Client");
        JButton localButton = new JButton("Local");

        serverButton.setPreferredSize(new Dimension(120, 40));
        clientButton.setPreferredSize(new Dimension(120, 40));
        localButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(serverButton);
        buttonPanel.add(clientButton);
        buttonPanel.add(localButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleServerMode(frame);
            }
        });

        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClientMode(frame);
            }
        });

        localButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLocalMode(frame);
            }
        });

        // Add panel to frame with padding
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Handle Server Mode
    private void handleServerMode(JFrame parentFrame) {

        SystemHandler.getInstance().getSystemVariables().put("mode","server");
        // Prompt for Port
        String portStr = JOptionPane.showInputDialog(parentFrame, "Enter Port Number:", "Server Configuration", JOptionPane.PLAIN_MESSAGE);
        if (portStr == null) {
            // User canceled
            return;
        }

        // Validate port number
        int port;
        try {
            port = Integer.parseInt(portStr);
            if (port < 1 || port > 65535) {
                JOptionPane.showMessageDialog(parentFrame, "Please enter a valid port number (1-65535).", "Invalid Port", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Port must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generate Connection PIN
        String connectionPin = generateConnectionPin();

        // Initialize Server instance
        serverInstance = Server.getInstance();
        serverInstance.config(port);
        serverInstance.setConnectionPin(connectionPin);

        // Register this window as a listener to server connection events
        serverInstance.addConnectionListener(this);

        // Start the server in a new thread to prevent blocking the GUI
        new Thread(() -> {
            try {
                serverInstance.startServer();
                // Retrieve server IP
                String serverIp = serverInstance.getServerIp();
                SwingUtilities.invokeLater(() -> {
                    String message = "Server started successfully!\n" +
                            "IP Address: " + serverIp + "\n" +
                            "Port: " + port + "\n" +
                            "Connection PIN: " + connectionPin;
                    JOptionPane.showMessageDialog(parentFrame, message, "Server Started", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to start server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }, "Server-Startup-Thread").start();
    }

    // Handle Client Mode
    private void handleClientMode(JFrame parentFrame) {
        SystemHandler.getInstance().getSystemVariables().put("mode","client");

        // Prompt for IP Address
        String ipAddress = JOptionPane.showInputDialog(parentFrame, "Enter Server IP Address:", "Client Configuration", JOptionPane.PLAIN_MESSAGE);
        if (ipAddress == null) {
            // User canceled
            return;
        }
        ipAddress = ipAddress.trim();
        if (ipAddress.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "IP Address cannot be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prompt for Port
        String portStr = JOptionPane.showInputDialog(parentFrame, "Enter Server Port Number:", "Client Configuration", JOptionPane.PLAIN_MESSAGE);
        if (portStr == null) {
            // User canceled
            return;
        }

        // Validate port number
        int port;
        try {
            port = Integer.parseInt(portStr);
            if (port < 1 || port > 65535) {
                JOptionPane.showMessageDialog(parentFrame, "Please enter a valid port number (1-65535).", "Invalid Port", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Port must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prompt for Connection PIN
        String connectionPin = JOptionPane.showInputDialog(parentFrame, "Enter Connection PIN:", "Client Configuration", JOptionPane.PLAIN_MESSAGE);
        if (connectionPin == null) {
            // User canceled
            return;
        }
        connectionPin = connectionPin.trim();
        if (connectionPin.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Connection PIN cannot be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Initialize Client instance
        clientInstance = Client.getInstance();
        clientInstance.config(ipAddress, port, connectionPin);

        // Register this window as a listener to client connection events
        clientInstance.addConnectionListener(this);

        // Start the client in a new thread to prevent blocking the GUI
        String finalIpAddress = ipAddress;
        String finalConnectionPin = connectionPin;
        new Thread(() -> {
            try {
                clientInstance.connect();
                SwingUtilities.invokeLater(() -> {
                    String message = "Client attempting to connect...\n" +
                            "Server IP: " + finalIpAddress + "\n" +
                            "Port: " + port + "\n" +
                            "Connection PIN: " + finalConnectionPin;
                    JOptionPane.showMessageDialog(parentFrame, message, "Client Connecting", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to connect to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }, "Client-Startup-Thread").start();
    }

    // Handle Local Mode
    private void handleLocalMode(JFrame parentFrame) {
        SystemHandler.getInstance().getSystemVariables().put("mode","local");
        String message = "Application started in Local Mode.";
        JOptionPane.showMessageDialog(parentFrame, message, "Local Mode", JOptionPane.INFORMATION_MESSAGE);

        // Initialize the local simulation
        new InputWindow(); // Prompt for simulation inputs
        new TableStates();  // Initialize the state tables
        new Entrance("UI", GraphicsManager.getInstance().initialize());
        new Credits();
        // Add zone A to the server
        SystemHandler.getInstance().getInputVariables().put("Zone", 0);
        parentFrame.dispose(); // Close the mode selector window
    }

    // Method to generate a random 6-digit connection PIN
    private String generateConnectionPin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(pin);
    }

    /**
     * Callback method invoked when the server successfully accepts a client connection.
     * It transitions the GUI to the main simulation windows.
     */
    @Override
    public void onServerConnectionEstablished() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(frame, "A client has connected to the server.", "Connection Established", JOptionPane.INFORMATION_MESSAGE);
            // Launch main simulation windows
            new InputWindow();  // Prompt for simulation inputs
            new TableStates();   // Initialize the state tables
            new Entrance("UI", GraphicsManager.getInstance().initialize());
            // Add zone A to the server
            SystemHandler.getInstance().getInputVariables().put("Zone", 0);
            new Credits();
            // Close the mode selector window
            frame.dispose();
        });
    }

    /**
     * Callback method invoked when the client successfully connects to the server.
     * It transitions the GUI to the main simulation windows.
     */
    @Override
    public void onClientConnectionEstablished() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(frame, "Successfully connected to the server.", "Connection Established", JOptionPane.INFORMATION_MESSAGE);
            // Launch main simulation windows
            new InputWindow();  // Prompt for simulation inputs
            new TableStates();   // Initialize the state tables
            JPanel threads = GraphicsManager.getInstance().initialize();
            new Entrance("Entrance A", threads);
            GraphicsManager.getInstance().triggerRepaint();
            new Credits();
            // Add zone B to the server
            SystemHandler.getInstance().getInputVariables().put("Zone", 1);
            // Close the mode selector window
            frame.dispose();
        });
    }
}