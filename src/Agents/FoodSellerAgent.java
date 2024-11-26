package Agents;

import Handlers.SystemHandler;

import java.awt.*;

public class FoodSellerAgent  extends AbstractAgent<FoodSellerAgent.AgentState> implements Runnable{

    @Override
    public void draw(Graphics g) {
        int radius = 10;
        int centerX = position.x;
        int centerY = position.y;

        int[] xPoints = new int[5];
        int[] yPoints = new int[5];
        for (int i = 0; i < 5; i++) {
            xPoints[i] = centerX + (int) (radius * Math.cos(2 * Math.PI * i / 5));
            yPoints[i] = centerY + (int) (radius * Math.sin(2 * Math.PI * i / 5));
        }

        g.setColor(getColorForState());
        g.fillPolygon(xPoints, yPoints, 5);

        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 5);
    }
    private Color getColorForState() {
        return currentState == AgentState.SELLING ? Color.GREEN : Color.LIGHT_GRAY;
    }

    public enum AgentState{
        SELLING,
        WAITING
    }

    public FoodSellerAgent(String name) {
        super(name, AgentState.WAITING);
        position.x = 550;
        position.y = 10;
    }

    @Override
    public void _run() {
        // Since the seller's actions are managed by the TransactionManager,
        // the run method can be empty or manage additional behaviors if necessary.
        // The seller waits passively for transactions initiated by the TransactionManager
        try {
            goToFoodZone();
            Thread.sleep(SystemHandler.getInstance().getInputVariable("FoodSellerTime")); // Adjust as needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(getName() + " interrupted.");
        }
    }
}