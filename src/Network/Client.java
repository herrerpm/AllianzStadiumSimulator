// File: src/Network/Client.java

package Network;

import Agents.FanAgent;
import Handlers.FanHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A singleton Client class that connects to a server,
 * sends messages to the server, receives messages from the server,
 * and notifies listeners upon successful connections.
 */
public class Client {
    // Singleton instance
    private static Client instance = null;

    // Server configurations
    private String serverAddress;
    private int serverPort;
    private String connectionPin;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Flag to control the receiver thread
    private AtomicBoolean isRunning;

    // Listeners for connection events
    private final List<ConnectionListener> listeners;

    // Private constructor to prevent instantiation
    private Client() {
        isRunning = new AtomicBoolean(false);
        listeners = new CopyOnWriteArrayList<>();
    }

    /**
     * Retrieves the singleton instance of the Client.
     *
     * @return The singleton Client instance.
     */
    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    /**
     * Configures the client with the server's address, port, and connection PIN.
     *
     * @param address The server's IP address or hostname.
     * @param port    The server's port number.
     * @param pin     The connection PIN.
     */
    public void config(String address, int port, String pin) {
        this.serverAddress = address;
        this.serverPort = port;
        this.connectionPin = pin;
    }

    /**
     * Registers a ConnectionListener to receive connection events.
     *
     * @param listener The ConnectionListener to register.
     */
    public void addConnectionListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a previously registered ConnectionListener.
     *
     * @param listener The ConnectionListener to remove.
     */
    public void removeConnectionListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that a client connection has been established.
     */
    private void notifyClientConnectionEstablished() {
        for (ConnectionListener listener : listeners) {
            listener.onClientConnectionEstablished();
        }
    }

    /**
     * Connects to the server using the configured address, port, and PIN.
     *
     * @throws IOException If an I/O error occurs when creating the socket.
     */
    public void connect() throws IOException {
        if (serverAddress == null || serverPort <= 0 || connectionPin == null) {
            throw new IllegalStateException("Client not properly configured. Call config() first.");
        }

        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        isRunning.set(true);
        System.out.println("Connected to server " + serverAddress + " on port " + serverPort + ".");

        // Send the connection PIN to authenticate
        sendMessage(connectionPin);

        // Start receiving messages
        receiveMessageFromServer();
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            System.out.println("Sent to server: " + message);
        } else {
            System.err.println("Cannot send message. Not connected to the server.");
        }
    }

    /**
     * Starts a separate thread to continuously receive messages from the server.
     */
    public void receiveMessageFromServer() {
        if (socket == null || socket.isClosed()) {
            System.err.println("Cannot receive messages. Not connected to the server.");
            return;
        }

        new Thread(() -> {
            try {
                String message;
                while (isRunning.get() && (message = in.readLine()) != null) {
                    System.out.println("Received from server: " + message);

                    MessageHandler.handleMessage(message);

                    // Check if the server accepted the PIN
                    if (message.equals("Connected.")) {
                        notifyClientConnectionEstablished();
                    }
                }
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("Error receiving messages from server: " + e.getMessage());
                }
            } finally {
                close();
            }
        }, "Client-Receiver-Thread").start();
    }



    /**
     * Closes the connection with the server gracefully.
     */
    public void close() {
        isRunning.set(false);
        try {
            if (in != null) {
                in.close();
                System.out.println("Input stream closed.");
            }
            if (out != null) {
                out.close();
                System.out.println("Output stream closed.");
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Disconnected from server.");
            }
        } catch (IOException e) {
            System.err.println("Error closing the client: " + e.getMessage());
        }
    }

    /**
     * Example main method to demonstrate client usage.
     * Allows user to input messages from the console to send to the server.
     */
    public static void main(String[] args) {
        Client client = Client.getInstance();
        client.config("localhost", 12345, "123456"); // Set your server's address, port, and PIN

        // Optionally, add a ConnectionListener here if running client standalone
        // For GUI integration, listeners should be added from the GUI class

        try {
            client.connect();
        } catch (IOException e) {
            System.err.println("Client encountered an error: " + e.getMessage());
            client.close();
            return;
        }

        // Add a shutdown hook to gracefully close the client
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down client...");
            client.close();
        }));
    }
}
