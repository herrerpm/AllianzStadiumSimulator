package Agents;

import java.awt.*;

public class FoodSellerAgent  extends AbstractAgent<FoodSellerAgent.AgentState> implements Runnable{

    @Override
    public void draw(Graphics g) {

    }

    public enum AgentState{
        SELLING,
        WAITING
    }

    public FoodSellerAgent(String name) {
        super(name, AgentState.WAITING);
    }

    @Override
    public void _run() {
        // Since the seller's actions are managed by the TransactionManager,
        // the run method can be empty or manage additional behaviors if necessary.
            // The seller waits passively for transactions initiated by the TransactionManager
        try {
            Thread.sleep(1000); // Adjust as needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(getName() + " interrupted.");
        }
    }
}
