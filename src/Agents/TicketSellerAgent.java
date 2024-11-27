package Agents;

import Handlers.SystemHandler;
import java.awt.*;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {

    public enum AgentState {
        SELLING,
        WAITING,
        FINISHED
    }

    private static final int SIDE_LENGTH = 12;

    public TicketSellerAgent(String name) {
        super(name, AgentState.WAITING);
        position.x = 10;
        position.y = 150;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColorForState());
        g.fillRect(position.x, position.y, SIDE_LENGTH, SIDE_LENGTH);
    }

    @Override
    protected int getWidth() {
        return SIDE_LENGTH;
    }

    @Override
    protected int getHeight() {
        return SIDE_LENGTH;
    }

    private Color getColorForState() {
        return currentState == AgentState.SELLING ? Color.GREEN : Color.LIGHT_GRAY;
    }

    @Override
    public void _run() {
        // The seller's actions are managed by the TransactionManager
            try {
                Thread.sleep(1000); // Adjust as needed
                goToTickets();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " interrupted.");
            }
    }
}
