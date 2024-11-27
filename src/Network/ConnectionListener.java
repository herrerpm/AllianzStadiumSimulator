// File: src/Network/ConnectionListener.java

package Network;

/**
 * ConnectionListener defines callback methods that are triggered upon successful
 * connections in server and client modes.
 */
public interface ConnectionListener {
    /**
     * Called when the server successfully accepts a client connection.
     */
    void onServerConnectionEstablished();

    /**
     * Called when the client successfully connects to the server.
     */
    void onClientConnectionEstablished();
}
