package Handlers;

import Agents.FanAgent;
import Agents.PlayerAgent;
import Agents.TicketSellerAgent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerHandler  extends AbstractAgentHandler<PlayerAgent.AgentState, PlayerAgent>{

    // Private constructor to enforce singleton pattern
    private PlayerHandler() {
        // Initialize any necessary fields here if needed
    }

    // Holder class for lazy-loaded singleton instance
    private static class Holder {
        private static final PlayerHandler INSTANCE = new PlayerHandler();
    }

    /**
     * Retrieves the singleton instance of FanHandler.
     *
     * @return The singleton FanHandler instance.
     */
    public static PlayerHandler getInstance() {
        return PlayerHandler.Holder.INSTANCE;
    }

    /**
     * Factory method to create an individual FanAgent.
     *
     * @return A new instance of FanAgent.
     */
    @Override
    protected PlayerAgent createAgent() {
        int agentId = agents.size() + 1;
        String playerName = "Player-" + agentId;
        return new PlayerAgent(playerName);
    }

}