package Agents;

import Buffers.BathroomBuffer;
import Buffers.GameBuffer;
import Handlers.SystemHandler;
import java.awt.*;

public class PlayerAgent extends AbstractAgent<PlayerAgent.AgentState> implements Runnable {

    public enum AgentState {
        PLAYING,
        ENTERING_FIELD,
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
            double angle = Math.toRadians(120 * i - 90);
            xPoints[i] = position.x + (int) (SIZE * Math.cos(angle));
            yPoints[i] = position.y + (int) (SIZE * Math.sin(angle));
        }

        g.fillPolygon(xPoints, yPoints,3);

    }

    @Override
    protected int getWidth() {
        return SIZE;
    }



    public void enterField() {
        GameBuffer buffer = GameBuffer.getInstance();
        buffer.tryEnterBuffer(this);
        System.out.println(name + " is attempting to enter the field.");
        goToField();
    }

    public void leaveField() {
        GameBuffer buffer = GameBuffer.getInstance();
        buffer.leaveBuffer(this);
        goToBench();
        System.out.println(name + " has left the field.");


    @Override
    protected int getHeight() {
        return SIZE;
    }

    private Color getColorForState() {
        return currentState == AgentState.PLAYING ? Color.RED : Color.YELLOW;
    }

    private void initializeTransitions() {
        stateMachine.addTransition(PlayerAgent.AgentState.ON_BENCH, AgentState.PLAYING, 0.95);
        stateMachine.addTransition(AgentState.PLAYING, AgentState.ON_BENCH, 0.05);
    }

    public void performAction() {
        System.out.println(name + " Current State: " + currentState);
        switch (currentState) {
            case ENTERING_FIELD:
                System.out.println(name + " is waiting to enter the field.");
                boolean entered = GameBuffer.getInstance().tryEnterBuffer(this);
                if (entered) {
                    setCurrentState(AgentState.PLAYING);
                } else {
                    System.out.println(name + " remains waiting the substitution.");
                }
                break;

            case PLAYING:
                try {
                    // Simulate time spent in the bathroom
                    System.out.println(name + " is using the bathroom.");
                    goToField();
                    Thread.sleep(SystemHandler.getInstance().getInputVariable("EnteringStadium"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + " was interrupted while entering the field");
                } finally {
                    GameBuffer.getInstance().leaveBuffer(this);
                }
                stateMachine.nextState();
                break;

            case ON_BENCH:
                System.out.println(name + " is on the bench.");
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