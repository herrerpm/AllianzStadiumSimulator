package Handlers;

import Agents.TicketSellerAgent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SellingHandler manages TicketSellerAgents using the AbstractAgentHandler framework.
 * It ensures that agents are created, managed, and assigned to FanAgents efficiently.
 */
public class TicketSellingHandler extends AbstractAgentHandler<TicketSellerAgent.AgentState, TicketSellerAgent> {
    private static TicketSellingHandler instance = null;
    private final Random random = new Random();
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