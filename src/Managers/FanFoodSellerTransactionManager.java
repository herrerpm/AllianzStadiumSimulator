package Managers;

import Agents.FanAgent;
import Agents.FoodSellerAgent;
import Agents.TicketSellerAgent;
import Handlers.AbstractAgentHandler;
import Handlers.FanHandler;
import Handlers.FoodSellingHandler;
import Handlers.TicketSellingHandler;

import java.util.List;

public class FanFoodSellerTransactionManager extends AbstractTransactionManager<FanAgent, FoodSellerAgent>{

    private FanHandler fanHandler;
    private FoodSellingHandler sellingHandler;
    private int sellerTime; // in milliseconds

    // Eagerly initialized Singleton instance without parameters
    private static final FanFoodSellerTransactionManager INSTANCE = new FanFoodSellerTransactionManager();

    /**
     * Private constructor to prevent external instantiation.
     */
    private FanFoodSellerTransactionManager() {
        super();
    }

    /**
     * Retrieves the Singleton instance of TransactionManager.
     *
     * @return The Singleton TransactionManager instance.
     */
    public static FanFoodSellerTransactionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void configure(AbstractAgentHandler<?, FanAgent> requesterHandler,
                          AbstractAgentHandler<?, FoodSellerAgent> responderHandler,
                          int transactionTime) {
        if (this.fanHandler != null || this.sellingHandler != null) {
            throw new IllegalStateException("TransactionManager is already configured.");
        }
        this.fanHandler = (FanHandler) requesterHandler;
        this.sellingHandler = (FoodSellingHandler) responderHandler;
        this.sellerTime = transactionTime;

        List<FoodSellerAgent> sellers = sellingHandler.getAgents();
        availableResponders.addAll(sellers);

        System.out.println("TransactionManager configured with " + sellers.size() + " sellers and sellerTime = " + sellerTime + " ms.");
    }

    @Override
    public void handleTransaction(FanAgent fan) {
        if (fanHandler == null || sellingHandler == null || sellerTime <= 0) {
            throw new IllegalStateException("TransactionManager is not configured.");
        }

        try {
            // Take an available seller, blocking if none are available
            FoodSellerAgent seller = availableResponders.take();
            System.out.println(fan.getName() + " obtained " + seller.getName() + " for the transaction.");

            // Assign seller to fan
            seller.setCurrentState(FoodSellerAgent.AgentState.SELLING);
            fan.setCurrentState(FanAgent.AgentState.BUYING_FOOD);
            System.out.println(fan.getName() + " is buying a ticket from " + seller.getName());

            // Simulate the transaction
            completeTransaction(fan, seller);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("TransactionManager interrupted while handling transaction for " + fan.getName());
        }
    }

    @Override
    protected void completeTransaction(FanAgent fan, FoodSellerAgent seller) {
        try {
            // Simulate the transaction time
            Thread.sleep(sellerTime);

            // Reset seller state to WAITING
            seller.setCurrentState(FoodSellerAgent.AgentState.WAITING);
            System.out.println(seller.getName() + " is now waiting for the next customer.");

            // Make the seller available again
            availableResponders.offer(seller);
            System.out.println(seller.getName() + " is now available for new transactions.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("TransactionManager interrupted while completing transaction for " + fan.getName());
        } finally {
            // Update fan state to GENERAL_ZONE
            fan.setCurrentState(FanAgent.AgentState.GENERAL_ZONE);
            System.out.println(fan.getName() + " has moved to state: " + fan.getCurrentState());

            // Notify the fan that the transaction is complete
            synchronized (fan) {
                fan.notify();
            }
            System.out.println(fan.getName() + " has completed the transaction.");
        }
    }

    @Override
    public void shutdown() {
        // Implement shutdown logic if necessary
        // For example, interrupting any ongoing transactions or cleaning up resources
    }
}