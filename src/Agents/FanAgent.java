package Agents;

import java.util.Random;

public class FanAgent extends AbstractAgent<FanAgent.AgentState> implements Runnable {

    public enum AgentState {
        BUYING_TICKET,
        BUYING_FOOD,
        BATHROOM,
        WATCHING_GAME,
        GENERAL_ZONE
    }

    private final String name;
    private final StateMachine<AgentState> stateMachine;
    private final SellingHandler sellingHandler;
    private final Random random;
    private final int simulationSteps;

    public FanAgent(String name, SellingHandler sellingHandler, int simulationSteps) {
        super(AgentState.BUYING_TICKET);
        this.name = name;
        this.sellingHandler = sellingHandler;
        this.stateMachine = new StateMachine<>(AgentState.BUYING_TICKET);
        initializeTransitions();
        this.random = new Random();
        this.simulationSteps = simulationSteps;
    }

    public String getName() {
        return name;
    }

    // Initialize state transitions with probabilities
    private void initializeTransitions() {
        // From BUYING_TICKET
        stateMachine.addTransition(AgentState.BUYING_TICKET, AgentState.WATCHING_GAME, 0.6);
        stateMachine.addTransition(AgentState.BUYING_TICKET, AgentState.BUYING_FOOD, 0.3);
        stateMachine.addTransition(AgentState.BUYING_TICKET, AgentState.GENERAL_ZONE, 0.1);

        // From BUYING_FOOD
        stateMachine.addTransition(AgentState.BUYING_FOOD, AgentState.WATCHING_GAME, 0.5);
        stateMachine.addTransition(AgentState.BUYING_FOOD, AgentState.BATHROOM, 0.2);
        stateMachine.addTransition(AgentState.BUYING_FOOD, AgentState.GENERAL_ZONE, 0.3);

        // From BATHROOM
        stateMachine.addTransition(AgentState.BATHROOM, AgentState.WATCHING_GAME, 0.7);
        stateMachine.addTransition(AgentState.BATHROOM, AgentState.GENERAL_ZONE, 0.3);

        // From WATCHING_GAME
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.BUYING_FOOD, 0.2);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.BATHROOM, 0.1);
        stateMachine.addTransition(AgentState.WATCHING_GAME, AgentState.GENERAL_ZONE, 0.7);

        // From GENERAL_ZONE
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BUYING_FOOD, 0.3);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.BATHROOM, 0.2);
        stateMachine.addTransition(AgentState.GENERAL_ZONE, AgentState.WATCHING_GAME, 0.5);
    }

    public void performAction() {
        AgentState currentState = stateMachine.getCurrentState();
        System.out.println(name + " Current State: " + currentState);
        switch (currentState) {
            case BUYING_TICKET:
                System.out.println(name + " is attempting to buy a ticket.");
                sellingHandler.handleTicketRequest(this);
                break;
            case BUYING_FOOD:
                System.out.println(name + " is buying food.");
                break;
            case BATHROOM:
                System.out.println(name + " is using the bathroom.");
                break;
            case WATCHING_GAME:
                System.out.println(name + " is watching the game.");
                break;
            case GENERAL_ZONE:
                System.out.println(name + " is in the general zone.");
                break;
        }
        // Transition to the next state
        stateMachine.nextState();
        // Update the base class state
        this.setState(stateMachine.getCurrentState());
    }

    public AgentState getCurrentState() {
        return stateMachine.getCurrentState();
    }

    @Override
    public void run() {
        for (int i = 1; i <= simulationSteps; i++) {
            System.out.println("=== " + name + " Step " + i + " ===\n");
            performAction();
            System.out.println("----------------------------\n");
            // Adding a small delay for readability
            try {
                Thread.sleep(1000); // 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " interrupted.");
                break;
            }
        }
        System.out.println(name + " has completed all simulation steps.");
    }
}
