// File: src/Agents/TicketSellerAgent.java

package Agents;

import Handlers.SellingHandler;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {

    public enum AgentState {
        SELLING,
        WAITING,
        FINISHED
    }

    private static final int timeWithoutAttending = 10000;
    private long transactionTime;
    private volatile boolean running = true;


    // New attribute to store the current zone

    public TicketSellerAgent(String name) {

        super(name, AgentState.WAITING);
        this.transactionTime = System.currentTimeMillis();
    }
    @Override
    public void _run() {
        // The seller's actions are managed by the TransactionManager,
        // so this can remain empty or include additional behaviors if necessary.
        while (running) {
            try {
                // Sellers wait passively for transactions
                Thread.sleep(1000); // Adjust as needed
                if (getCurrentState() == AgentState.WAITING &&
                        (System.currentTimeMillis() - transactionTime > timeWithoutAttending)) {
                    System.out.println(getName() + " Seller finished work");
                    terminate();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " interrupted.");
                break;
            }
        }
    }
    private void terminate() {
        running = false;
        SellingHandler.getInstance().removeAgent(this);
        setCurrentState(AgentState.FINISHED);
        Thread.currentThread().interrupt();
    }
}
