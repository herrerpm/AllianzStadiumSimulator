// File: TransactionManager.java
package Managers;

import Agents.FanAgent;
import Agents.TicketSellerAgent;
import Handlers.AbstractAgentHandler;
import Handlers.FanHandler;
import Handlers.SellingHandler;

import java.util.List;

/**
 * TransactionManager handles interactions between FanAgents and TicketSellerAgents.
 * It extends AbstractTransactionManager to provide specific implementation for these agent types.
 */
public class FanTicketSellerTransactionManager extends AbstractTransactionManager<FanAgent, TicketSellerAgent> {

    private FanHandler fanHandler;
    private SellingHandler sellingHandler;
    private int sellerTime; // in milliseconds

    // Eagerly initialized Singleton instance without parameters
    private static final FanTicketSellerTransactionManager INSTANCE = new FanTicketSellerTransactionManager();

    /**
     * Private constructor to prevent external instantiation.
     */
    private FanTicketSellerTransactionManager() {
        super();
    }

    /**
     * Retrieves the Singleton instance of TransactionManager.
     *
     * @return The Singleton TransactionManager instance.
     */
    public static FanTicketSellerTransactionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void configure(AbstractAgentHandler<?, FanAgent> requesterHandler,
                          AbstractAgentHandler<?, TicketSellerAgent> responderHandler,
                          int transactionTime) {
        if (this.fanHandler != null || this.sellingHandler != null) {
            throw new IllegalStateException("TransactionManager is already configured.");
        }
        this.fanHandler = (FanHandler) requesterHandler;
        this.sellingHandler = (SellingHandler) responderHandler;
        this.sellerTime = transactionTime;

        List<TicketSellerAgent> sellers = sellingHandler.getAgents();
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
            TicketSellerAgent seller = availableResponders.take();
            System.out.println(fan.getName() + " obtained " + seller.getName() + " for the transaction.");

            // Assign seller to fan
            seller.setCurrentState(TicketSellerAgent.AgentState.SELLING);
            fan.setCurrentState(FanAgent.AgentState.BUYING_TICKET);
            System.out.println(fan.getName() + " is buying a ticket from " + seller.getName());

            // Simulate the transaction
            completeTransaction(fan, seller);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("TransactionManager interrupted while handling transaction for " + fan.getName());
        }
    }

    @Override
    protected void completeTransaction(FanAgent fan, TicketSellerAgent seller) {
        try {
            // Simulate the transaction time
            Thread.sleep(sellerTime);

            // Reset seller state to WAITING
            seller.setCurrentState(TicketSellerAgent.AgentState.WAITING);
            System.out.println(seller.getName() + " is now waiting for the next customer.");

            // Make the seller available again
            availableResponders.offer(seller);
            System.out.println(seller.getName() + " is now available for new transactions.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("TransactionManager interrupted while completing transaction for " + fan.getName());
        } finally {
            // Update fan state to GENERAL_ZONE
            fan.getStateMachine().setCurrentState(FanAgent.AgentState.GENERAL_ZONE);
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
