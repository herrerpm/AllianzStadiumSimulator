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

    public int getAgentCountByState(S state) {
        List<A> agentsInState = new ArrayList<>();
        for (A agent : agents) {
            if (agent.getCurrentState() == state) {
                agentsInState.add(agent);
            }
        }
        return agentsInState.size();
    }

    public List<Thread> getAgentThreads() {
        List<Thread> threadsInState = new ArrayList<>();
        for (A agent : agents) {
            Thread thread = agent.getThread();
            threadsInState.add(thread);
        }
        return threadsInState;
    }
}
