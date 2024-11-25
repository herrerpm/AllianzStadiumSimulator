package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A singleton Server class that handles client connections,
 * reads messages from clients, and sends messages to clients.
 */
public class Server {
    // Singleton instance
    private static Server instance = null;

    // Server configurations
    private int port;
    private ServerSocket serverSocket;
    private boolean isRunning;

    // Map to keep track of connected clients
    private ConcurrentHashMap<String, ClientHandler> clients;

    // Private constructor to prevent instantiation
    private Server() {
        clients = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the singleton instance of the Server.
     *
     * @return The singleton Server instance.
     */
    public static synchronized Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    /**
     * Configures the server with the specified port.
     *
     * @param port The port number on which the server will listen.
     */
    public void config(int port) {
        this.port = port;
    }

    /**
     * Starts the server and begins listening for client connections.
     *
     * @throws IOException If an I/O error occurs when opening the socket.
     */
    public void start() throws IOException {
        if (port <= 0) {
            throw new IllegalStateException("Server port not configured. Call config() first.");
        }

        serverSocket = new ServerSocket(port);
        isRunning = true;
        System.out.println("Server started. Listening on port " + port + "...");

        // Main loop to accept client connections
        new Thread(() -> {
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    String clientId = clientSocket.getRemoteSocketAddress().toString();
                    System.out.println("Client connected: " + clientId);

                    ClientHandler handler = new ClientHandler(clientSocket, clientId);
                    clients.put(clientId, handler);
                    new Thread(handler).start();
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    /**
     * Stops the server and disconnects all clients.
     */
    public void stop() {
        isRunning = false;
        try {
            // Close the server socket to stop accepting new clients
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }

            // Disconnect all clients
            for (ClientHandler handler : clients.values()) {
                handler.sendMessage("Server is shutting down.");
                handler.stop();
            }
            clients.clear();
        } catch (IOException e) {
            System.err.println("Error stopping the server: " + e.getMessage());
        }
    }

    /**
     * Sends a message to a specific client.
     *
     * @param clientId The unique identifier of the client.
     * @param message  The message to send.
     */
    public void sendToClient(String clientId, String message) {
        ClientHandler handler = clients.get(clientId);
        if (handler != null) {
            handler.sendMessage(message);
        } else {
            System.err.println("Client " + clientId + " not found.");
        }
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message The message to send.
     */
    public void sendToAllClients(String message) {
        for (ClientHandler handler : clients.values()) {
            handler.sendMessage(message);
        }
    }

    /**
     * Inner class to handle individual client connections.
     */
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String clientId;
        private PrintWriter out;
        private BufferedReader in;
        private boolean isClientRunning;

        public ClientHandler(Socket socket, String clientId) {
            this.clientSocket = socket;
            this.clientId = clientId;
            this.isClientRunning = true;
            try {
                // Initialize input and output streams
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                System.err.println("Error initializing I/O for client " + clientId + ": " + e.getMessage());
                stop();
            }
        }

        /**
         * Sends a message to the client.
         *
         * @param message The message to send.
         */
        public void sendMessage(String message) {
            out.println(message);
        }

        /**
         * Stops the client handler by closing streams and socket.
         */
        public void stop() {
            isClientRunning = false;
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
                System.out.println("Client disconnected: " + clientId);
            } catch (IOException e) {
                System.err.println("Error closing resources for client " + clientId + ": " + e.getMessage());
            } finally {
                clients.remove(clientId);
            }
        }

        /**
         * Reads a message from the client.
         *
         * @return The message read from the client, or null if the connection is closed.
         * @throws IOException If an I/O error occurs while reading.
         */
        public String readFromClient() throws IOException {
            return in.readLine();
        }

        @Override
        public void run() {
            try {
                // Send a welcome message to the client
                sendMessage("Welcome to the server!");

                String message;
                while (isClientRunning && (message = readFromClient()) != null) {
                    System.out.println("Received from " + clientId + ": " + message);
                    // Optionally, process the message or respond
                    // For example, echo the message back to the client
                    sendMessage("Echo: " + message);
                }
            } catch (IOException e) {
                if (isClientRunning) {
                    System.err.println("Error communicating with client " + clientId + ": " + e.getMessage());
                }
            } finally {
                stop();
            }
        }
    }

    /**
     * Example main method to demonstrate server usage.
     */
    public static void main(String[] args) {
        Server server = Server.getInstance();
        server.config(12345); // Set your desired port

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
            return;
        }

        // Add a shutdown hook to gracefully stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server...");
            server.stop();
        }));
    }
}
