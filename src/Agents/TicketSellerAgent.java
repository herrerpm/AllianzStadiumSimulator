// File: src/Agents/TicketSellerAgent.java

package Agents;

import Handlers.SellingHandler;
import Handlers.SystemHandler;
import Managers.GraphicsManager;
import java.awt.*;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {



    public enum AgentState {
        SELLING,
        WAITING,
        FINISHED
    }

    private static final int timeWithoutAttending = SystemHandler.getInstance().getInputVariable("TicketSellerTerminateTime");
    private long transactionTime;
    private volatile boolean running = true;


    // New attribute to store the current zone

    public TicketSellerAgent(String name) {

        super(name, AgentState.WAITING);
        this.transactionTime = System.currentTimeMillis();
    }
    private final static int side_lenght = 5;

    @Override
    public void draw(Graphics g) {
        g.setColor(getColorForState());
        g.drawRect(position.x, position.y, side_lenght, side_lenght);
    }

    private Color getColorForState() {
        return currentState == AgentState.SELLING ? Color.GREEN : Color.LIGHT_GRAY;
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
