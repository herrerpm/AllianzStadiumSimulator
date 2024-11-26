package Handlers;

import Agents.FanAgent;
import Agents.TicketSellerAgent;
import Managers.ThreadManager;

import java.awt.*;
import java.util.List;

/**
 * FanHandler manages FanAgent instances and their threads.
 * It extends AbstractAgentHandler to leverage common agent handling functionalities.
 */
public class FanHandler extends AbstractAgentHandler<FanAgent.AgentState, FanAgent> {

    // Private constructor to enforce singleton pattern
    private FanHandler() {
        super();
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
        FanAgent fan = new FanAgent(fanName);
        return fan;
    }
    public Color getAgentColorForState(FanAgent.AgentState state) {
        switch (state) {
            case ENTERING_STADIUM:
                return Color.BLUE;
            case REGISTER:
                return Color.PINK;
            case INLINE_TOBUY:
                return Color.BLUE;
            case BUYING_TICKET:
                return Color.RED;
            case BUYING_FOOD:
                return Color.GREEN;
            case INLINE_TOBUY_FOOD:
                return Color.YELLOW;
            case BATHROOM_LINE:
                return Color.MAGENTA;
            case BATHROOM:
                return Color.CYAN;
            case WATCHING_GAME:
                return Color.LIGHT_GRAY;
            case GENERAL_ZONE:
                return Color.GRAY;
            default:
                return Color.BLACK; // Default color
        }
    }


    public void removeAgent(FanAgent fan) {
        getAgents().remove(fan);
    }

}