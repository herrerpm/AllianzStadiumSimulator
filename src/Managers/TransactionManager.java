package Managers;

import Agents.FanAgent;
import Agents.TicketSellerAgent;
import Handlers.FanHandler;
import Handlers.SellingHandler;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TransactionManager handles interactions between FanAgents and TicketSellerAgents.
 * It manages transactions by making fans wait for an available seller sequentially.
 * Implemented as a singleton with late configuration.
 */
public class TransactionManager {

    private FanHandler fanHandler;
    private SellingHandler sellingHandler;
    private int sellerTime; // in milliseconds
    private final BlockingQueue<TicketSellerAgent> availableSellers;

    // Eagerly initialized Singleton instance without parameters
    private static final TransactionManager INSTANCE = new TransactionManager();

    /**
     * Private constructor to prevent external instantiation.
     */
    private TransactionManager() {
        this.availableSellers = new LinkedBlockingQueue<>();
    }

    /**
     * Retrieves the Singleton instance of TransactionManager.
     *
     * @return The Singleton TransactionManager instance.
     */
    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Configures the TransactionManager with necessary dependencies and initializes resources.
     * This method should be called once after user input is handled and agents are created.
     *
     * @param fanHandler      The handler managing FanAgents.
     * @param sellingHandler  The handler managing TicketSellerAgents.
     * @param sellerTime      The time in milliseconds for a transaction.
     */
    public synchronized void configure(FanHandler fanHandler, SellingHandler sellingHandler, int sellerTime) {
        if (this.fanHandler != null || this.sellingHandler != null) {
            throw new IllegalStateException("TransactionManager is already configured.");
        }
        this.fanHandler = fanHandler;
        this.sellingHandler = sellingHandler;
        this.sellerTime = sellerTime;

        List<TicketSellerAgent> sellers = sellingHandler.getAgents();
        availableSellers.addAll(sellers);

        System.out.println("TransactionManager configured with " + sellers.size() + " sellers and sellerTime = " + sellerTime + " ms.");
    }

    /**
     * Handles a transaction between a FanAgent and an available TicketSellerAgent.
     * Fans will wait until a seller is available.
     *
     * @param fan The FanAgent requesting to buy a ticket.
     */
    public void handleTransaction(FanAgent fan) {
        if (fanHandler == null || sellingHandler == null || sellerTime <= 0) {
            throw new IllegalStateException("TransactionManager is not configured.");
        }

        try {
            // Take an available seller, blocking if none are available
            TicketSellerAgent seller = availableSellers.take();
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

    /**
     * Completes the transaction by resetting the states of FanAgent and TicketSellerAgent.
     * Makes the seller available again.
     *
     * @param fan    The FanAgent involved in the transaction.
     * @param seller The TicketSellerAgent involved in the transaction.
     */
    private void completeTransaction(FanAgent fan, TicketSellerAgent seller) {
        try {
            // Simulate the transaction time
            Thread.sleep(sellerTime);

            // Reset seller state to WAITING
            seller.setCurrentState(TicketSellerAgent.AgentState.WAITING);
            System.out.println(seller.getName() + " is now waiting for the next customer.");

            // Make the seller available again
            availableSellers.offer(seller);
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

    /**
     * Shuts down the TransactionManager.
     * Currently, no resources need explicit shutdown in this implementation.
     * If future enhancements require resource management, implement it here.
     */
    public void shutdown() {
        // No scheduler or executor to shut down in this implementation
        // If you add resources like threads or executors in the future, handle their shutdown here
    }
}
