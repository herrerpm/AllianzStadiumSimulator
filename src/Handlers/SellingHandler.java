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
        TicketSellerAgent seller = new TicketSellerAgent(sellerName, this);
        return seller;
    }

    /**
     * Handles ticket requests from FanAgents by assigning them to available TicketSellerAgents.
     *
     * @param fanAgent The FanAgent requesting a ticket.
     */
    public void handleTicketRequest(FanAgent fanAgent) {
        List<TicketSellerAgent> availableSellers =  SellingHandler.instance.getAgentsByState(TicketSellerAgent.AgentState.WAITING);
        if (availableSellers.isEmpty()) {
            System.out.println("No sellers available for " + fanAgent.getName() + ". Request ignored.");
            return;
        }
        // Select a random available seller
        TicketSellerAgent seller = availableSellers.get(random.nextInt(availableSellers.size()));
        System.out.println(fanAgent.getName() + " is assigned to " + seller.getName());
        seller.addRequest(fanAgent);
    }
}
