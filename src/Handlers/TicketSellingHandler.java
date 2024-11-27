package Handlers;

import Agents.PlayerAgent;
import Agents.TicketSellerAgent;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SellingHandler manages TicketSellerAgents using the AbstractAgentHandler framework.
 * It ensures that agents are created, managed, and assigned to FanAgents efficiently.
 */
public class TicketSellingHandler extends AbstractAgentHandler<TicketSellerAgent.AgentState, TicketSellerAgent> {
    private static TicketSellingHandler instance = null;
    private final AtomicInteger sellerIdGenerator = new AtomicInteger(1);

    /**
     * Private constructor to enforce singleton pattern.
     *
     */
    private TicketSellingHandler() {
        super();
    }

    /**
     * Provides the singleton instance of SellingHandler.
     *
     * @return Singleton instance of SellingHandler.
     */
    public static synchronized TicketSellingHandler getInstance() {
        if (instance == null) {
            instance = new TicketSellingHandler();
        }
        return instance;
    }

    public Color getAgentColorForState(TicketSellerAgent.AgentState state) {
        switch (state) {
            case SELLING:
                return Color.GREEN;
            case WAITING:
                return Color.LIGHT_GRAY;
            default:
                return Color.BLACK;
        }
    }

    public void removeAgent(TicketSellerAgent seller) {
        this.removeAgentByName(seller.getName());
        System.out.println(seller.getName() + " has been removed from the handler.");
    }

    /**
     * Factory method to create individual TicketSellerAgent instances.
     *
     * @return A new instance of TicketSellerAgent.
     */
    @Override
    protected TicketSellerAgent createAgent() {
        String sellerName = "Ticket Seller-" + sellerIdGenerator.getAndIncrement();
        return new TicketSellerAgent(sellerName);
    }
}