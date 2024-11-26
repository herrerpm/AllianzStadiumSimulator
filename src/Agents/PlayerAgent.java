package Agents;

import Handlers.SystemHandler;
import java.awt.*;

public class PlayerAgent extends AbstractAgent<PlayerAgent.AgentState> implements Runnable {

    public enum AgentState {
        PLAYING,
        ON_BENCH,
    }

    private final PlayerStateMachine stateMachine;
    private static final int SIZE = 10;

    public PlayerAgent(String name) {
        super(name, AgentState.ON_BENCH);
        this.stateMachine = new PlayerStateMachine(this);
        initializeTransitions();
        position.x = 0;
        position.y = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColorForState());
        // Drawing logic for the player (e.g., triangle)
        // Calculate the vertices of the triangle (equilateral)
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(120 * i - 90); // Angles: -90°, 30°, 150°
            xPoints[i] = position.x + (int) (SIZE * Math.cos(angle));
            yPoints[i] = position.y + (int) (SIZE * Math.sin(angle));
        }

        g.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    protected int getWidth() {
        return SIZE;
    }

    @Override
    protected int getHeight() {
        return SIZE;
    }

    private Color getColorForState() {
        return currentState == AgentState.PLAYING ? Color.RED : Color.YELLOW;
    }

    private void initializeTransitions() {
        stateMachine.addTransition(PlayerAgent.AgentState.ON_BENCH, AgentState.PLAYING, 1.0);
        stateMachine.addTransition(AgentState.PLAYING, AgentState.ON_BENCH, 0.7);
    }

    public void performAction() {
        PlayerAgent.AgentState currentState = stateMachine.getCurrentState();
        System.out.println(name + " Current State: " + currentState);
        switch (currentState) {
            case PLAYING:
                goToField();
                System.out.println(name + " is on field playing.");
                stateMachine.nextState();
                break;

            case ON_BENCH:
                goToBench();
                System.out.println(name + " is subbed off, now is on the bench.");
                stateMachine.nextState();
                break;

            default:
                stateMachine.nextState();
                break;
        }
    }

    @Override
    public void _run() {
        performAction();
        System.out.println("----------------------------\n");
        try {
            Thread.sleep(SystemHandler.getInstance().getInputVariable("PlayerStateChangeTime"));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(name + " has completed all simulation steps.");
    }
}
