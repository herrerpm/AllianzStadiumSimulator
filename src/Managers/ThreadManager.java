package Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ThreadManager is a singleton class responsible for managing threads.
 * It provides functionalities to create, start, and retrieve threads based on their states.
 */
public class ThreadManager {

    // Thread-safe list to store all created threads
    private final List<Thread> threads;

    // Private constructor to enforce singleton pattern
    private ThreadManager() {
        threads = new CopyOnWriteArrayList<>();
    }

    /**
     * Holder class for implementing the Singleton pattern.
     * The INSTANCE is created when the Holder class is loaded.
     */
    private static class Holder {
        private static final ThreadManager INSTANCE = new ThreadManager();
    }

    /**
     * Retrieves the singleton instance of ThreadManager.
     *
     * @return The singleton ThreadManager instance.
     */
    public static ThreadManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Creates a new thread with the given Runnable and name, starts it,
     * and adds it to the managed threads list.
     *
     * @param runnable   The Runnable task for the thread.
     * @param threadName The name of the thread.
     * @return The created and started Thread instance.
     */
    public Thread createAndStartThread(Runnable runnable, String threadName) {
        Thread thread = new Thread(runnable, threadName);
        threads.add(thread);
        thread.start();
        return thread;
    }

    /**
     * Retrieves a list of threads that are currently in the specified state.
     *
     * @param state The Thread.State to filter threads by.
     * @return A list of threads in the specified state.
     */
    public List<Thread> getThreadsByState(Thread.State state) {
        List<Thread> result = new ArrayList<>();
        for (Thread thread : threads) {
            if (thread.getState() == state) {
                result.add(thread);
            }
        }
        return result;
    }

    /**
     * Retrieves a list of all managed threads.
     *
     * @return A list of all threads managed by ThreadManager.
     */
    public List<Thread> getAllThreads() {
        return new ArrayList<>(threads);
    }

    /**
     * Retrieves the count of threads that are currently in the specified state.
     *
     * @param state The Thread.State to filter threads by.
     * @return The number of threads in the specified state.
     */
    public int getThreadCountByState(Thread.State state) {
        int count = 0;
        for (Thread thread : threads) {
            if (thread.getState() == state) {
                count++;
            }
        }
        return count;
    }

    /**
     * Removes threads from the managed list that have terminated.
     * This helps in preventing memory leaks by cleaning up references to dead threads.
     */
    public void removeTerminatedThreads() {
        threads.removeIf(thread -> thread.getState() == Thread.State.TERMINATED);
    }

    /**
     * Waits for all managed threads to complete their execution.
     * This method blocks until all threads have finished.
     */
    public void waitForAllThreadsToComplete() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("ThreadManager interrupted while waiting for threads to complete.");
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }

    /**
     * Interrupts all managed threads.
     * Use this method with caution as it attempts to stop all threads abruptly.
     */
    public void interruptAllThreads() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
