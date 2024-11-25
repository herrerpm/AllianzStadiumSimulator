package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A singleton Client class that connects to a server,
 * sends messages to the server, receives messages from the server,
 * and can gracefully close the connection.
 */
public class Client {
    // Singleton instance
    private static Client instance = null;

    // Server configurations
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Flag to control the receiver thread
    private AtomicBoolean isRunning;

    // Private constructor to prevent instantiation
    private Client() {
        isRunning = new AtomicBoolean(false);
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
     * Configures the client with the server's address and port.
     *
     * @param address The server's IP address or hostname.
     * @param port    The server's port number.
     */
    public void config(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
    }

    /**
     * Connects to the server using the configured address and port.
     *
     * @throws IOException If an I/O error occurs when creating the socket.
     */
    public void connect() throws IOException {
        if (serverAddress == null || serverPort <= 0) {
            throw new IllegalStateException("Client not configured. Call config() first.");
        }

        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        isRunning.set(true);
        System.out.println("Connected to server " + serverAddress + " on port " + serverPort + ".");
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send.
     */
    public void sendMessageToServer(String message) {
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
                }
            } catch (IOException e) {
                if (isRunning.get()) {
                    System.err.println("Error receiving messages from server: " + e.getMessage());
                }
            } finally {
                close();
            }
        }).start();
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
        client.config("localhost", 12345); // Set your server's address and port

        try {
            client.connect();
            client.receiveMessageFromServer();

            // Read user input from the console and send to the server
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            System.out.println("Enter messages to send to the server (type 'exit' to quit):");

            while ((userInput = consoleReader.readLine()) != null) {
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                client.sendMessageToServer(userInput);
            }

            // Close resources
            client.close();
            consoleReader.close();
        } catch (IOException e) {
            System.err.println("Client encountered an error: " + e.getMessage());
            client.close();
        }
    }
}
