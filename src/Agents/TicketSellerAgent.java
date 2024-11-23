package Agents;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {

    public enum AgentState {
        SELLING,
        WAITING
    }

    public TicketSellerAgent(String name) {
        super(name, AgentState.WAITING);
    }

    @Override
    public void run() {
        // Since the seller's actions are managed by the TransactionManager,
        // the run method can be empty or manage additional behaviors if necessary.
        while (true) {
            // The seller waits passively for transactions initiated by the TransactionManager
            try {
                Thread.sleep(1000); // Adjust as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " interrupted.");
                break;
            }
        }
    }
}
