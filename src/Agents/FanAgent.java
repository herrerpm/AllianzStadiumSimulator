package Agents;

import Handlers.FanHandler;
import Handlers.SellingHandler;
import Handlers.SystemHandler;
import Managers.GraphicsManager;
import Managers.FanFoodSellerTransactionManager;
import Managers.FanTicketSellerTransactionManager;
import Buffers.BathroomBuffer;

import java.awt.*;

public class FanAgent extends AbstractAgent<FanAgent.AgentState> implements Runnable {

    public enum AgentState {
        ENTERING_STADIUM,
        INLINE_TOBUY,
        BUYING_TICKET,
        BUYING_FOOD,
        INLINE_TOBUY_FOOD,
        BATHROOM_LINE,
        BATHROOM,
        WATCHING_GAME,
        GENERAL_ZONE,
        EXIT
    }

    public void setCurrentState(FanAgent.AgentState state){
        this.stateMachine.setCurrentState(state);
    }

    private final FanStateMachine stateMachine;

    private final static int diameter = 20;

    public FanAgent(String name) {
        // Set the initial state to ENTERING_STADIUM
        super(name, AgentState.ENTERING_STADIUM);
        this.stateMachine = new FanStateMachine(this); // Pass the agent to the state machine
        initializeTransitions();
        position.x = 0;
        position.y = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColorForState());
        g.fillOval(position.x, position.y, diameter, diameter);
    }
    private Color getColorForState() {
        switch (currentState) {
            case ENTERING_STADIUM:
                return Color.BLUE;
            case INLINE_TOBUY:
                return Color.ORANGE;
            case BUYING_TICKET:
                return Color.RED;
            case BUYING_FOOD:
                return Color.GREEN;
            case BATHROOM:
                return Color.CYAN;
            case WATCHING_GAME:
                return Color.MAGENTA;
            case GENERAL_ZONE:
                return Color.GRAY;
            default:
                return Color.BLACK; // Default color
        }
    }

    private void initializeTransitions() {
        // Define transition from ENTERING_STADIUM to INLINE_TOBUY with probability 1.0
        stateMachine.addTransition(AgentState.ENTERING_STADIUM, AgentState.INLINE_TOBUY, 1.0);

        // Other transitions remain the same
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.INLINE_TOBUY_FOOD, 0.3);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BATHROOM_LINE, 0.2);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.WATCHING_GAME, 0.5);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.EXIT, 0.1);

        stateMachine.addTransition(AgentState.BATHROOM_LINE, AgentState.BATHROOM, 0.7);
        stateMachine.addTransition(AgentState.BATHROOM_LINE, AgentState.GENERAL_ZONE, 0.3);


        stateMachine.addTransition(AgentState.BATHROOM, AgentState.WATCHING_GAME, 0.7);
        stateMachine.addTransition(AgentState.BATHROOM, AgentState.GENERAL_ZONE, 0.3);

        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.INLINE_TOBUY_FOOD, 0.2);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.BATHROOM_LINE, 0.1);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.GENERAL_ZONE, 0.7);
    }

    public void performAction() {
        AgentState currentState = stateMachine.getCurrentState();
        System.out.println(name + " Current State: " + currentState);
        switch (currentState) {
            case ENTERING_STADIUM:
                System.out.println(name + " is entering the stadium.");
                stateMachine.nextState();
                break;

            case INLINE_TOBUY:
                System.out.println(name + " is waiting in line to buy a ticket.");
                // Start transaction (will block until a seller is available)
                FanTicketSellerTransactionManager.getInstance().handleTransaction(this);

                // Wait for transaction to complete
                synchronized (this) {
                    while (getCurrentState() == AgentState.BUYING_TICKET) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println(name + " interrupted while waiting for transaction to complete.");
                            return;
                        }
                    }
                }
                // After transaction, proceed to next state
                stateMachine.nextState();
                break;

            case BUYING_TICKET:
                // This case should not occur, as we handle BUYING_TICKET in INLINE_TOBUY
                break;

            case INLINE_TOBUY_FOOD:
                System.out.println(name + " is waiting in line to buy food.");
                // Start transaction (will block until a seller is available)
                FanFoodSellerTransactionManager.getInstance().handleTransaction(this);

                // Wait for transaction to complete
                synchronized (this) {
                    while (getCurrentState() == AgentState.BUYING_FOOD) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println(name + " interrupted while waiting for food transaction to complete.");
                            return;
                        }
                    }
                }
                // After transaction, proceed to next state
                stateMachine.nextState();
                break;

            case BUYING_FOOD:
                break;

            case BATHROOM_LINE:
                System.out.println(name + " is waiting in line to use the bathroom.");
                boolean entered = BathroomBuffer.getInstance().tryEnterBuffer(this);
                if (entered) {
                    stateMachine.nextState(); // Transition to BATHROOM
                } else {
                    System.out.println(name + " remains in the bathroom line.");
                    // Optionally, you can wait for some time before retrying
                }
                break;

            case BATHROOM:
                try {
                    // Simulate time spent in the bathroom
                    System.out.println(name + " is using the bathroom.");
                    Thread.sleep(SystemHandler.getInstance().getInputVariable("BathroomTime")); // Adjust duration as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + " was interrupted while using the bathroom.");
                } finally {
                    BathroomBuffer.getInstance().leaveBuffer(this);
                }
                stateMachine.nextState();
                break;

            case WATCHING_GAME:
                System.out.println(name + " is watching the game.");
                stateMachine.nextState();
                break;

            case GENERAL_ZONE:
                System.out.println(name + " is in the general zone.");
                stateMachine.nextState();
                break;
            case EXIT:
                System.out.println(name + " Exit the stadium");
                terminate();
                break;

            default:
                stateMachine.nextState();
                break;
        }
    }

    private void terminate() {
        FanHandler.getInstance().removeAgent(this);
        setCurrentState(AgentState.EXIT);
        Thread.currentThread().interrupt();
    }

    @Override
    public void _run() {
        performAction();
        System.out.println("----------------------------\n");
        try {
            Thread.sleep(SystemHandler.getInstance().getInputVariable("FanStateChangeTime"));
            // Trigger repaint after state change
            GraphicsManager.getInstance().triggerRepaint();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(name + " has completed all simulation steps.");
    }
}