package Handlers;

import Agents.FanAgent;
import Managers.ThreadManager;
import java.util.List;

/**
 * FanHandler manages FanAgent instances and their threads.
 * It extends AbstractAgentHandler to leverage common agent handling functionalities.
 */
public class FanHandler extends AbstractAgentHandler<FanAgent.AgentState, FanAgent> {

    // Private constructor to enforce singleton pattern
    private FanHandler() {
        // Initialize any necessary fields here if needed
    }

    // Holder class for lazy-loaded singleton instance
    private static class Holder {
        private static final FanHandler INSTANCE = new FanHandler();
    }

    /**
     * Retrieves the singleton instance of FanHandler.
     *
     * @return The singleton FanHandler instance.
     */
    public static FanHandler getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Factory method to create an individual FanAgent.
     *
     * @return A new instance of FanAgent.
     */
    @Override
    protected FanAgent createAgent() {
        int agentId = agents.size() + 1;
        String fanName = "Fan-" + agentId;
        return new FanAgent(fanName, 50);
    }

}