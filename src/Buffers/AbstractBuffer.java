// File: src/Buffers/AbstractBuffer.java

package Buffers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbstractBuffer is a generic buffer that controls access for a specific type of agent.
 * It uses a Semaphore to limit the number of agents that can be in the buffer concurrently.
 *
 * @param <T> The type of agent that interacts with the buffer.
 */
public abstract class AbstractBuffer<T> {
    private final Semaphore semaphore;
    private final int capacity;
    private final AtomicInteger waitingCount; // Tracks the number of agents waiting to enter

    /**
     * Constructor initializes the buffer with a specified capacity.
     *
     * @param capacity The maximum number of agents allowed in the buffer at the same time.
     */
    public AbstractBuffer(int capacity) {
        this.capacity = capacity;
        this.semaphore = new Semaphore(capacity, true);
        this.waitingCount = new AtomicInteger(0);
    }

    /**
     * Agent attempts to enter the buffer. This method blocks until a permit is available.
     *
     * @param agent The agent trying to enter the buffer.
     * @throws InterruptedException if the current thread is interrupted while waiting.
     */
    public void enterBuffer(T agent) throws InterruptedException {
        semaphore.acquire();
        waitingCount.decrementAndGet(); // Agent is no longer waiting
        onEnter(agent);
    }

    /**
     * Agent attempts to enter the buffer without blocking.
     *
     * @param agent The agent trying to enter the buffer.
     * @return true if the agent successfully entered the buffer, false otherwise.
     */
    public boolean tryEnterBuffer(T agent) {
        boolean acquired = semaphore.tryAcquire();
        if (acquired) {
            onEnter(agent);
        } else {
            waitingCount.incrementAndGet(); // Agent is waiting
        }
        return acquired;
    }

    /**
     * Agent leaves the buffer, releasing a permit and allowing another agent to enter.
     *
     * @param agent The agent leaving the buffer.
     */
    public void leaveBuffer(T agent) {
        onLeave(agent);
        semaphore.release();
    }

    /**
     * Hook method called when an agent enters the buffer. Subclasses should implement
     * specific behaviors upon entering.
     *
     * @param agent The agent entering the buffer.
     */
    protected abstract void onEnter(T agent);

    /**
     * Hook method called when an agent leaves the buffer. Subclasses should implement
     * specific behaviors upon leaving.
     *
     * @param agent The agent leaving the buffer.
     */
    protected abstract void onLeave(T agent);

    /**
     * Returns the current number of agents in the buffer.
     *
     * @return The number of agents currently in the buffer.
     */
    public int getCurrentOccupancy() {
        return capacity - semaphore.availablePermits();
    }

    /**
     * Returns the number of agents waiting to enter the buffer.
     *
     * @return The number of agents waiting in line.
     */
    public int getWaitingCount() {
        return waitingCount.get();
    }

    /**
     * Returns the capacity of the buffer.
     *
     * @return The maximum number of agents allowed in the buffer.
     */
    public int getCapacity() {
        return capacity;
    }
}