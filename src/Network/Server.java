// File: src/Network/Server.java

package Network;

import Agents.FanAgent;
import Handlers.FanHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A singleton Server class that handles client connections,
 * reads messages from clients, sends messages to clients,
 * and notifies listeners upon successful connections.
 */
public class Server {
    // Singleton instance
    private static Server instance = null;

    // Server configurations
    private int port;
    private ServerSocket serverSocket;
    private boolean isRunning;

    // Connection PIN for client authentication
    private String connectionPin;

    // List to keep track of connected clients
    public List<ClientHandler> clients;

    // Listeners for connection events
    private final List<ConnectionListener> listeners;

    // Private constructor to prevent instantiation
    private Server() {
        clients = new CopyOnWriteArrayList<>();
        listeners = new CopyOnWriteArrayList<>();
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
     * Sets the connection PIN required for client authentication.
     *
     * @param pin The connection PIN.
     */
    public void setConnectionPin(String pin) {
        this.connectionPin = pin;
    }

    /**
     * Retrieves the server's IP address.
     *
     * @return The server's IP address as a String.
     * @throws UnknownHostException If the local host name could not be resolved into an address.
     */
    public String getServerIp() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
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
     * Notifies all registered listeners that a server connection has been established.
     */
    private void notifyServerConnectionEstablished() {
        for (ConnectionListener listener : listeners) {
            listener.onServerConnectionEstablished();
        }
    }

    /**
     * Notifies all registered listeners that a client has successfully connected.
     */
    private void notifyClientConnectionEstablished() {
        for (ConnectionListener listener : listeners) {
            listener.onClientConnectionEstablished();
        }
    }

    /**
     * Starts the server and begins listening for client connections.
     *
     * @throws IOException If an I/O error occurs when opening the socket.
     */
    public void startServer() throws IOException {
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
                    clients.add(handler);
                    new Thread(handler, "ClientHandler-" + clientId).start();
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        }, "Server-Accept-Thread").start();
    }

    /**
     * Stops the server and disconnects all clients.
     */
    public void stopServer() {
        isRunning = false;
        try {
            // Close the server socket to stop accepting new clients
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }

            // Disconnect all clients
            for (ClientHandler handler : clients) {
                handler.sendMessage("Server is shutting down.");
                handler.stop();
            }
            clients.clear();
        } catch (IOException e) {
            System.err.println("Error stopping the server: " + e.getMessage());
        }
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message The message to send.
     */
    public void sendToAllClients(String message) {
        for (ClientHandler handler : clients) {
            handler.sendMessage(message);
        }
    }

    /**
     * Inner class to handle individual client connections.
     */
    public class ClientHandler implements Runnable {
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
                clients.remove(this);
            }
        }

        @Override
        public void run() {
            try {
                // Handle client communication here
                String receivedPin = in.readLine();
                if (!connectionPin.equals(receivedPin)) {
                    sendMessage("Invalid PIN. Disconnecting.");
                    return;
                }
                sendMessage("Connected.");
                notifyClientConnectionEstablished();

                String message;
                while (isClientRunning && (message = in.readLine()) != null) {
                    MessageHandler.handleMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Error communicating with client " + clientId + ": " + e.getMessage());
            } finally {
                stop();
            }
        }
    }
}