package Handlers;

import Agents.AbstractAgent;
import Managers.ThreadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractAgentHandler provides a generalized framework for managing agents and their threads.
 *
 * @param <S> Enum type representing the state of the agent.
 * @param <A> Type of agent extending AbstractAgent<S>.
 */
public abstract class AbstractAgentHandler<S extends Enum<S>, A extends AbstractAgent<S>> {
    protected final List<A> agents;
    protected final List<Thread> agentThreads;

    protected AbstractAgentHandler() {
        this.agents = new ArrayList<>();
        this.agentThreads = new ArrayList<>();
    }

    /**
     * Creates and starts a specified number of agents.
     *
     * This implementation uses the ThreadManager to create and manage threads for each agent.
     * Subclasses must provide the logic to instantiate individual agents by implementing the method.
     *
     * @param numberOfAgents  Number of agents to create.
     */
    public void createAgents(int numberOfAgents) {
        ThreadManager threadManager = ThreadManager.getInstance();

        for (int i = 0; i < numberOfAgents; i++) {
            // Instantiate the agent using the factory method
            A agent = createAgent();
            agents.add(agent);

            // Create a unique thread name, e.g., "Agent-1", "Agent-2", etc.
            String threadName = "Agent-" + (i + 1);

            // Use ThreadManager to create and start the thread for the agent
            Thread thread = threadManager.createAndStartThread((Runnable) agent, threadName);
            agentThreads.add(thread);
        }
    }

    /**
     * Factory method to create an individual agent.
     *
     * Subclasses must implement this method to provide specific agent instances.
     *
     * @return A new instance of A extending AbstractAgent<S>.
     */
    protected abstract A createAgent();

    /**
     * Returns a list of all agents.
     *
     * @return List of agents.
     */
    public List<A> getAgents() {
        return new ArrayList<>(agents);
    }

    /**
     * Returns a list of all agent threads.
     *
     * @return List of threads.
     */
    public List<Thread> getAgentThreads() {
        return new ArrayList<>(agentThreads);
    }

    /**
     * Retrieves a list of agents that are currently in the specified state.
     *
     * @param state The state to filter agents by.
     * @return List of agents in the specified state.
     */
    public List<A> getAgentsByState(S state) {
        List<A> agentsInState = new ArrayList<>();
        for (A agent : agents) {
            if (agent.getCurrentState() == state) {
                agentsInState.add(agent);
            }
        }
        return agentsInState;
    }

    /**
     * Counts the number of agents currently in the specified state.
     *
     * @param state The state to filter agents by.
     * @return Number of agents in the specified state.
     */
    public int getAgentCountByState(S state) {
        return (int) agents.stream()
                .filter(agent -> agent.getCurrentState() == state)
                .count();
    }

    /**
     * Retrieves a list of threads that are in the specified Thread.State.
     *
     * @param state The thread state to filter by.
     * @return List of threads in the specified state.
     */
    public List<Thread> getThreadsByState(Thread.State state) {
        List<Thread> threadsInState = new ArrayList<>();
        for (Thread thread : agentThreads) {
            if (thread.getState() == state) {
                threadsInState.add(thread);
            }
        }
        return threadsInState;
    }

    /**
     * Counts the number of threads currently in the specified Thread.State.
     *
     * @param state The thread state to filter by.
     * @return Number of threads in the specified state.
     */
    public int getThreadCountByState(Thread.State state) {
        return (int) agentThreads.stream()
                .filter(thread -> thread.getState() == state)
                .count();
    }
}
