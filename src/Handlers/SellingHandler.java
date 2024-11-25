package Handlers;

import Agents.FanAgent;
import Agents.TicketSellerAgent;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SellingHandler manages TicketSellerAgents using the AbstractAgentHandler framework.
 * It ensures that agents are created, managed, and assigned to FanAgents efficiently.
 */
public class SellingHandler extends AbstractAgentHandler<TicketSellerAgent.AgentState, TicketSellerAgent> {
    private static SellingHandler instance = null;
    private final Random random = new Random();
    private final AtomicInteger sellerIdGenerator = new AtomicInteger(1);

    /**
     * Private constructor to enforce singleton pattern.
     *
     */
    private SellingHandler() {
        super();
    }

    /**
     * Provides the singleton instance of SellingHandler.
     *
     * @return Singleton instance of SellingHandler.
     */
    public static synchronized SellingHandler getInstance() {
        if (instance == null) {
            instance = new SellingHandler();
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
        String sellerName = "Seller-" + sellerIdGenerator.getAndIncrement();
        return new TicketSellerAgent(sellerName);
    }

    public void removeAgent(TicketSellerAgent seller) {
        getAgents().remove(seller);
        System.out.println(seller.getName() + " has been removed from the handler.");
    }

}