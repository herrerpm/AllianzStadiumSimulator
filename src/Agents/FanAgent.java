package Agents;

import Managers.FanTicketSellerTransactionManager;

public class FanAgent extends AbstractAgent<FanAgent.AgentState> implements Runnable {

    public enum AgentState {
        ENTERING_STADIUM, // New starting state
        INLINE_TOBUY,      // Represents being in line to buy a ticket
        BUYING_TICKET,
        BUYING_FOOD,
        BATHROOM,
        WATCHING_GAME,
        GENERAL_ZONE
    }

    public FanStateMachine getStateMachine() {
        return stateMachine;
    }

    private final FanStateMachine stateMachine;
    private final int simulationSteps;

    public FanAgent(String name, int simulationSteps) {
        // Set the initial state to ENTERING_STADIUM
        super(name, AgentState.ENTERING_STADIUM);
        this.stateMachine = new FanStateMachine(this); // Pass the agent to the state machine
        initializeTransitions();
        this.simulationSteps = simulationSteps;
    }

    private void initializeTransitions() {
        // Define transition from ENTERING_STADIUM to INLINE_TOBUY with probability 1.0
        stateMachine.addTransition(AgentState.ENTERING_STADIUM, AgentState.INLINE_TOBUY, 1.0);

        // Other transitions remain the same
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BUYING_FOOD, 0.3);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BATHROOM, 0.2);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.WATCHING_GAME, 0.5);

        stateMachine.addTransition(AgentState.BUYING_FOOD, AgentState.WATCHING_GAME, 0.5);
        stateMachine.addTransition(AgentState.BUYING_FOOD, AgentState.BATHROOM, 0.2);
        stateMachine.addTransition(AgentState.BUYING_FOOD, AgentState.GENERAL_ZONE, 0.3);

        stateMachine.addTransition(AgentState.BATHROOM, AgentState.WATCHING_GAME, 0.7);
        stateMachine.addTransition(AgentState.BATHROOM, AgentState.GENERAL_ZONE, 0.3);

        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.BUYING_FOOD, 0.2);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.BATHROOM, 0.1);
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

            case BUYING_FOOD:
                System.out.println(name + " is buying food.");
                stateMachine.nextState();
                break;

            case BATHROOM:
                System.out.println(name + " is using the bathroom.");
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(name + " has completed all simulation steps.");
    }
}