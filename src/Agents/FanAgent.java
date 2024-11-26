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
        REGISTER,
        INLINE_TOBUY,
        BUYING_TICKET,
        BUYING_FOOD,
        INLINE_TOBUY_FOOD,
        BATHROOM_LINE,          // New state: waiting in line to use the bathroom
        BATHROOM,
        WATCHING_GAME,
        GENERAL_ZONE,
        EXIT
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
        g.setColor(getColorForState()); // Ensure color is set based on state
        g.fillOval(position.x, position.y, diameter, diameter);
    }

    private Color getColorForState() {
        switch (currentState) {
            case ENTERING_STADIUM:
                return Color.BLUE;
            case REGISTER:
                return Color.PINK;
            case INLINE_TOBUY:
                return Color.BLUE;
            case BUYING_TICKET:
                return Color.RED;
            case BUYING_FOOD:
                return Color.GREEN;
            case INLINE_TOBUY_FOOD:
                return Color.YELLOW;
            case BATHROOM_LINE:
                return Color.MAGENTA;
            case BATHROOM:
                return Color.CYAN;
            case WATCHING_GAME:
                return Color.LIGHT_GRAY;
            case GENERAL_ZONE:
                return Color.GRAY;
            default:
                return Color.BLACK;
        }
    }

    private void initializeTransitions() {
        // Define transition from ENTERING_STADIUM to INLINE_TOBUY with probability 1.0
        stateMachine.addTransition(AgentState.ENTERING_STADIUM, AgentState.INLINE_TOBUY, 1.0);
        stateMachine.addTransition(AgentState.REGISTER, AgentState.GENERAL_ZONE, 1.0);

        // Other transitions remain the same
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.INLINE_TOBUY_FOOD, 0.3);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BATHROOM_LINE, 0.2);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.WATCHING_GAME, 0.4);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.EXIT, 0.03);

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
                if (!isDestinationSet()) {
                    goToEntrance();
                }
                if (hasReachedDestination()) {
                    stateMachine.nextState();
                }
                break;

            case INLINE_TOBUY:
                System.out.println(name + " is waiting in line to buy a ticket.");
                if (!isDestinationSet()) {
                    goToTickets();
                }
                if (hasReachedDestination()) {
                    // Start transaction (will block until a seller is available)
                    FanTicketSellerTransactionManager.getInstance().handleTransaction(this);
                }
                break;

            case BUYING_TICKET:
                // This state is handled by the transaction manager
                break;

            case REGISTER:
                System.out.println(name + " is registering.");
                if (!isDestinationSet()) {
                    goToRegisterZone();
                }
                if (hasReachedDestination()) {
                    try {
                        System.out.println(name + " is in the registering.");
                        Thread.sleep(SystemHandler.getInstance().getInputVariable("RegisterTime")); // Adjust duration as needed
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println(name + " was interrupted while registering.");
                    }
                    stateMachine.nextState();
                }
                break;

            case INLINE_TOBUY_FOOD:
                System.out.println(name + " is waiting in line to buy food.");
                if (!isDestinationSet()) {
                    goToFoodZone();
                }
                if (hasReachedDestination()) {
                    // Start transaction (will block until a seller is available)
                    FanFoodSellerTransactionManager.getInstance().handleTransaction(this);
                }
                break;

            case BUYING_FOOD:
                // This state is handled by the transaction manager
                break;

            case BATHROOM_LINE:
                System.out.println(name + " is waiting in line to use the bathroom.");
                if (!isDestinationSet()) {
                    goToBathroomZone();
                }
                if (hasReachedDestination()) {
                    boolean entered = BathroomBuffer.getInstance().tryEnterBuffer(this);
                    if (entered) {
                        setCurrentState(AgentState.BATHROOM);
                    } else {
                        System.out.println(name + " remains in the bathroom line.");
                        // Optionally, you can wait for some time before retrying
                    }
                }
                break;

            case BATHROOM:
                try {
                    // Simulate time spent in the bathroom
                    System.out.println(name + " is using the bathroom.");
                    goToBathroomZone(); // Possibly move to a specific bathroom point
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
                if (!isDestinationSet()) {
                    goToStands();
                }
                if (hasReachedDestination()) {
                    stateMachine.nextState();
                }
                break;

            case GENERAL_ZONE:
                System.out.println(name + " is in the general zone.");
                if (!isDestinationSet()) {
                    goToGeneralZone();
                }
                if (hasReachedDestination()) {
                    stateMachine.nextState();
                }
                break;

            case EXIT:
                System.out.println(name + " is exiting the stadium.");
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
