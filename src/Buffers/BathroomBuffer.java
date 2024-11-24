// File: src/Buffers/BathroomBuffer.java

package Buffers;

import Agents.FanAgent;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BathroomBuffer is a Singleton class that manages access to the bathroom for FanAgents.
 * It restricts the number of fans that can be in the bathroom concurrently.
 */
public class BathroomBuffer extends AbstractBuffer<FanAgent> {

    private static BathroomBuffer instance = null;
    private static final Object lock = new Object();

    /**
     * Private constructor to prevent external instantiation.
     *
     * @param capacity The maximum number of fans allowed in the bathroom at the same time.
     */
    private BathroomBuffer(int capacity) {
        super(capacity);
    }

    /**
     * Initializes the singleton instance with the specified capacity.
     * This method should be called once during application initialization.
     *
     * @param capacity The maximum number of fans allowed in the bathroom at the same time.
     * @return The singleton instance of BathroomBuffer.
     */
    public static BathroomBuffer getInstance(int capacity) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new BathroomBuffer(capacity);
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves the already initialized singleton instance.
     *
     * @return The singleton instance of BathroomBuffer.
     * @throws IllegalStateException if the instance has not been initialized yet.
     */
    public static BathroomBuffer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BathroomBuffer has not been initialized. Call getInstance(int capacity) first.");
        }
        return instance;
    }

    @Override
    protected void onEnter(FanAgent fan) {
        // Update the fan's state or perform actions upon entering the bathroom.
        System.out.println(fan.getName() + " has entered the bathroom. Current Occupancy: " + getCurrentOccupancy());
        // Additional actions can be implemented here (e.g., logging, UI updates)
    }

    @Override
    protected void onLeave(FanAgent fan) {
        // Update the fan's state or perform actions upon leaving the bathroom.
        System.out.println(fan.getName() + " has left the bathroom. Current Occupancy: " + getCurrentOccupancy());
        // Additional actions can be implemented here (e.g., logging, UI updates)
    }
}
