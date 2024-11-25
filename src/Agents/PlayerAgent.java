package Agents;

public class PlayerAgent extends AbstractAgent<PlayerAgent.AgentState> implements Runnable {

    public enum AgentState {
        PLAYING,
        ON_BENCH,
    }

    public PlayerStateMachine getStateMachine() {
        return stateMachine;
    }

    private final PlayerStateMachine stateMachine;
    private final int simulationSteps;

    public PlayerAgent(String name, int simulationSteps) {
        // Set the initial state to ENTERING_STADIUM
        super(name, AgentState.ON_BENCH);
        this.stateMachine = new PlayerStateMachine(this); // Pass the agent to the state machine
        initializeTransitions();
        this.simulationSteps = simulationSteps;
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
                stateMachine.nextState();
                break;

            case ON_BENCH:
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
        for (int i = 1; i <= simulationSteps; i++) {
            System.out.println("=== " + name + " Step " + i + " ===\n");
            performAction();
            System.out.println("----------------------------\n");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(name + " has completed all simulation steps.");
    }

}
