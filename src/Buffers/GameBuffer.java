// File: src/Buffers/GameBuffer.java

package Buffers;

import Agents.PlayerAgent;

/**
 * GameBuffer manages the access of PlayerAgents to the playing field.
 * It restricts the number of players that can be on the field concurrently.
 * This class follows the Singleton design pattern to ensure only one instance exists.
 */
public class GameBuffer extends AbstractBuffer<PlayerAgent> {

    // Singleton instance
    private static GameBuffer instance = null;
    private static final Object lock = new Object();

    /**
     * Private constructor to prevent external instantiation.
     *
     * @param capacity The maximum number of players allowed on the field simultaneously.
     */
    private GameBuffer(int capacity) {
        super(capacity);
    }

    /**
     * Initializes the singleton instance with the specified capacity.
     * This method should be called once during application initialization.
     *
     * @param capacity The maximum number of players allowed on the field at the same time.
     * @return The singleton instance of GameBuffer.
     */
    public static GameBuffer getInstance(int capacity) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new GameBuffer(capacity);
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves the already initialized singleton instance.
     *
     * @return The singleton instance of GameBuffer.
     * @throws IllegalStateException if the instance has not been initialized yet.
     */
    public static GameBuffer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GameBuffer has not been initialized. Call getInstance(int capacity) first.");
        }
        return instance;
    }

    @Override
    protected void onEnter(PlayerAgent player) {
        // Update the player's state or perform actions upon entering the field.
        System.out.println(player.getName() + " has entered the field. Current Occupancy: " + getCurrentOccupancy());
//        player.setCurrentState(PlayerAgent.AgentState.PLAYING);
        // Additional actions can be implemented here (e.g., logging, UI updates)
    }

    @Override
    protected void onLeave(PlayerAgent player) {
        // Update the player's state or perform actions upon leaving the field.
        System.out.println(player.getName() + " has left the field. Current Occupancy: " + getCurrentOccupancy());
//        player.setCurrentState(PlayerAgent.AgentState.ON_BENCH);
        // Additional actions can be implemented here (e.g., logging, UI updates)
    }
}
