// File: src/Agents/FanAgent.java

package Agents;

import Managers.FanFoodSellerTransactionManager;
import Managers.FanTicketSellerTransactionManager;
import Buffers.BathroomBuffer;

public class FanAgent extends AbstractAgent<FanAgent.AgentState> implements Runnable {

    public enum AgentState {
        ENTERING_STADIUM,      // New starting state
        INLINE_TOBUY,           // Represents being in line to buy a ticket
        BUYING_TICKET,
        INLINE_TOBUY_FOOD,      // Represents being in line to buy food
        BUYING_FOOD,
        BATHROOM_LINE,          // New state: waiting in line to use the bathroom
        BATHROOM,               // State: using the bathroom
        WATCHING_GAME,
        GENERAL_ZONE
    }

    private final FanStateMachine stateMachine;
    private final int simulationSteps;

    public FanAgent(String name, int simulationSteps) {
        super(name, AgentState.ENTERING_STADIUM);
        this.stateMachine = new FanStateMachine(this);
        initializeTransitions();
        this.simulationSteps = simulationSteps;
    }

    private void initializeTransitions() {
        // Define transitions for the state machine
        stateMachine.addTransition(AgentState.ENTERING_STADIUM, AgentState.INLINE_TOBUY, 1.0);

        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.INLINE_TOBUY_FOOD, 0.25);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BATHROOM_LINE, 0.25);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.WATCHING_GAME, 0.5);

        stateMachine.addTransition(AgentState.BATHROOM_LINE, AgentState.BATHROOM, 0.7);
        stateMachine.addTransition(AgentState.BATHROOM_LINE, AgentState.GENERAL_ZONE, 0.3);

        stateMachine.addTransition(AgentState.BATHROOM, AgentState.WATCHING_GAME, 0.7);
        stateMachine.addTransition(AgentState.BATHROOM, AgentState.GENERAL_ZONE, 0.3);

        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.INLINE_TOBUY_FOOD, 0.2);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.BATHROOM_LINE, 0.1);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.GENERAL_ZONE, 0.7);
    }

    public FanStateMachine getStateMachine() {
        return stateMachine;
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
                FanTicketSellerTransactionManager.getInstance().handleTransaction(this);
                synchronized (this) {
                    while (getCurrentState() == AgentState.BUYING_TICKET) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println(name + " interrupted while waiting for ticket transaction to complete.");
                            return;
                        }
                    }
                }
                stateMachine.nextState();
                break;

            case INLINE_TOBUY_FOOD:
                System.out.println(name + " is waiting in line to buy food.");
                FanFoodSellerTransactionManager.getInstance().handleTransaction(this);
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
                stateMachine.nextState();
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
                    Thread.sleep(2000); // Adjust duration as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + " was interrupted while using the bathroom.");
                } finally {
                    BathroomBuffer.getInstance().leaveBuffer(this);
                }
                stateMachine.nextState();
                break;

            case BUYING_TICKET:
                // This case should not occur as BUYING_TICKET is handled in INLINE_TOBUY
                break;

            case BUYING_FOOD:
                // This case should not occur as BUYING_FOOD is handled in INLINE_TOBUY_FOOD
                break;

            case WATCHING_GAME:
                System.out.println(name + " is watching the game.");
                stateMachine.nextState();
                break;

            case GENERAL_ZONE:
                System.out.println(name + " is in the general zone.");
                stateMachine.nextState();
                break;

            default:
                stateMachine.nextState();
                break;
        }
    }

    @Override
    public void run() {
        for (int i = 1; i <= simulationSteps; i++) {
            System.out.println("=== " + name + " Step " + i + " ===\n");
            performAction();
            System.out.println("----------------------------\n");
            try {
                Thread.sleep(3000); // Adjust as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(name + " has completed all simulation steps.");
    }
}
