package Agents;

import Handlers.SystemHandler;

import java.awt.*;

public class PlayerAgent extends AbstractAgent<PlayerAgent.AgentState> implements Runnable {

    public enum AgentState {
        PLAYING,
        ON_BENCH,
    }

    public PlayerStateMachine getStateMachine() {
        return stateMachine;
    }

    private final PlayerStateMachine stateMachine;

    private static final int size = 5;
    @Override
    public void draw(Graphics g) {
        g.setColor(getColorForState());
        // Calcular los vértices del triángulo (equilátero)
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(120 * i - 90); // Ángulos: -90°, 30°, 150°
            xPoints[i] = position.x + (int) (size * Math.cos(angle));
            yPoints[i] = position.y + (int) (size * Math.sin(angle));
        }

        g.fillPolygon(xPoints, yPoints, 3);
    }
    private Color getColorForState() {
        return currentState == AgentState.PLAYING ? Color.RED : Color.YELLOW;
    }

    public PlayerAgent(String name) {
        // Set the initial state to ENTERING_STADIUM
        super(name, AgentState.ON_BENCH);
        this.stateMachine = new PlayerStateMachine(this); // Pass the agent to the state machine
        initializeTransitions();
        position.x = 0;
        position.y = 0;
    }

    private void initializeTransitions() {
        // Define transition from ENTERING_STADIUM to INLINE_TOBUY with probability 1.0
        stateMachine.addTransition(PlayerAgent.AgentState.ON_BENCH, AgentState.PLAYING, 1.0);
        stateMachine.addTransition(AgentState.PLAYING, AgentState.ON_BENCH, 0.7);
    }

    public void performAction() {
        PlayerAgent.AgentState currentState = stateMachine.getCurrentState();
        System.out.println(name + " Current State: " + currentState);
        switch (currentState) {
            case PLAYING:
                System.out.println(name + " is on field playing.");
                goToField();
                stateMachine.nextState();
                break;

            case ON_BENCH:
                System.out.println(name + " is subbed off, now is on the bench.");
                goToBench();
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