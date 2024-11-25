// File: src/Agents/TicketSellerAgent.java

package Agents;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {

    public enum AgentState {
        SELLING,
        WAITING
    }

    // New attribute to store the current zone

    public TicketSellerAgent(String name) {
        super(name, AgentState.WAITING);
    }
    @Override
    public void _run() {
        // The seller's actions are managed by the TransactionManager,
        // so this can remain empty or include additional behaviors if necessary.
        while (true) {
            try {
                // Sellers wait passively for transactions
                Thread.sleep(1000); // Adjust as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " interrupted.");
                break;
            }
        }
    }
}
