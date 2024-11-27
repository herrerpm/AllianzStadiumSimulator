package Handlers;

import Agents.AbstractAgent;
import Managers.ThreadManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AbstractAgentHandler provides a generalized framework for managing agents and their threads.
 *
 * @param <S> Enum type representing the state of the agent.
 * @param <A> Type of agent extending AbstractAgent<S>.
 */
public abstract class AbstractAgentHandler<S extends Enum<S>, A extends AbstractAgent<S>> {
    protected final List<A> agents;

    protected AbstractAgentHandler() {
        this.agents = new ArrayList<>();
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
            String threadName = agent.getName(); // Assuming agent names are unique

            // Use ThreadManager to create and start the thread for the agent
            Thread thread = threadManager.createAndStartThread(agent, threadName);

            // Assign the thread to the agent
            agent.setThread(thread);
        }
    }

    /**
     * Creates and starts a single agent with a custom name.
     *
     * This method uses the ThreadManager to create and manage the thread for the custom agent.
     *
     * @param name The custom name for the agent.
     * @return The created agent.
     */
    public A createCustomAgent(String name) {
        ThreadManager threadManager = ThreadManager.getInstance();

        // Instantiate the agent using the factory method
        A agent = createAgent();

        // Set the custom name for the agent
        agent.setName(name);

        // Add the agent to the list
        agents.add(agent);

        // Use ThreadManager to create and start the thread for the agent
        Thread thread = threadManager.createAndStartThread(agent, name);

        // Assign the thread to the agent
        agent.setThread(thread);

        return agent; // Return the created agent
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
     * Returns the count of agents in a specific state.
     *
     * @param state The state to filter agents by.
     * @return The number of agents in the specified state.
     */
    public int getAgentCountByState(S state) {
        int count = 0;
        for (A agent : agents) {
            if (agent.getCurrentState() == state) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retrieves all threads associated with the agents.
     *
     * @return List of threads.
     */
    public List<Thread> getAgentThreads() {
        List<Thread> threads = new ArrayList<>();
        for (A agent : agents) {
            Thread thread = agent.getThread();
            if (thread != null) {
                threads.add(thread);
            }
        }
        return threads;
    }

    /**
     * Removes an agent by its name.
     *
     * This method stops the agent's thread and removes it from the list of managed agents.
     *
     * @param name The name of the agent to remove.
     * @return true if the agent was found and removed; false otherwise.
     */
    public boolean removeAgentByName(String name) {
        Iterator<A> iterator = agents.iterator();
        while (iterator.hasNext()) {
            A agent = iterator.next();
            if (agent.getName().equals(name)) {
                agent.stopAgent();
                // Remove the agent from the list
                iterator.remove();
                return true;
            }
        }
        return false; // Agent not found
    }
}