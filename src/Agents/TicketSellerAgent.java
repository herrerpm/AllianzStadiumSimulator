// File: src/Agents/TicketSellerAgent.java

package Agents;

import Managers.GraphicsManager;

import java.awt.*;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {



    public enum AgentState {
        SELLING,
        WAITING
    }

    // New attribute to store the current zone

    public TicketSellerAgent(String name) {
        super(name, AgentState.WAITING);
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
