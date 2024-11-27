package Handlers;

import Agents.FoodSellerAgent;
import Agents.TicketSellerAgent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FoodSellingHandler extends AbstractAgentHandler<FoodSellerAgent.AgentState, FoodSellerAgent> {
    private static FoodSellingHandler instance = null;
    private final AtomicInteger sellerIdGenerator = new AtomicInteger(1);

    /**
     * Private constructor to enforce singleton pattern.
     *
     */
    private FoodSellingHandler() {
        super();
    }

    /**
     * Provides the singleton instance of SellingHandler.
     *
     * @return Singleton instance of SellingHandler.
     */
    public static synchronized FoodSellingHandler getInstance() {
        if (instance == null) {
            instance = new FoodSellingHandler();
        }
        return instance;
    }

    /**
     * Factory method to create individual TicketSellerAgent instances.
     *
     * @return A new instance of TicketSellerAgent.
     */
    @Override
    protected FoodSellerAgent createAgent() {
        String sellerName = "Food Seller-" + sellerIdGenerator.getAndIncrement();
        return new FoodSellerAgent(sellerName);
    }
}