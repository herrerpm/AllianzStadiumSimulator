package Agents;

import java.util.*;

public class PlayerStateMachine {
    private PlayerAgent.AgentState currentState;
    private final Map<PlayerAgent.AgentState, List<Transition<PlayerAgent.AgentState>>> transitions = new HashMap<>();
    private final Random random = new Random();
    private final PlayerAgent player; // Reference to the fan agent

    public PlayerStateMachine(PlayerAgent player) {
        this.player = player;
        this.currentState = player.getCurrentState(); // Initialize with the fan's current state
    }

    public void addTransition(PlayerAgent.AgentState fromState, PlayerAgent.AgentState toState, double probability) {
        transitions.computeIfAbsent(fromState, k -> new ArrayList<>()).add(new Transition<>(toState, probability));
    }

    public void nextState() {
        List<Transition<PlayerAgent.AgentState>> possibleTransitions = transitions.get(currentState);
        if (possibleTransitions == null || possibleTransitions.isEmpty()) {
            System.out.println("No transitions defined for state: " + currentState);
            return;
        }

        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Transition<PlayerAgent.AgentState> transition : possibleTransitions) {
            cumulativeProbability += transition.getProbability();
            if (rand <= cumulativeProbability) {
                System.out.println("Transitioning from " + currentState + " to " + transition.getTargetState());
                currentState = transition.getTargetState();
                player.setCurrentState(currentState); // Update fan's current state
                return;
            }
        }

        System.out.println("Staying in state: " + currentState);
    }

    public PlayerAgent.AgentState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PlayerAgent.AgentState state) {
        this.currentState = state;
        player.setCurrentState(state); // Keep fan's state synchronized
    }
}
