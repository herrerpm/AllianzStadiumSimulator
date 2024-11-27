package Agents;

import java.util.*;

public class PlayerStateMachine {
    private final Map<PlayerAgent.AgentState, List<Transition<PlayerAgent.AgentState>>> transitions = new HashMap<>();
    private final Random random = new Random();
    private final PlayerAgent player; // Reference to the fan agent

    public PlayerStateMachine(PlayerAgent player) {
        this.player = player;
    }

    public void addTransition(PlayerAgent.AgentState fromState, PlayerAgent.AgentState toState, double probability) {
        transitions.computeIfAbsent(fromState, k -> new ArrayList<>()).add(new Transition<>(toState, probability));
    }

    public void nextState() {
        List<Transition<PlayerAgent.AgentState>> possibleTransitions = transitions.get(player.getCurrentState());
        if (possibleTransitions == null || possibleTransitions.isEmpty()) {
            System.out.println("No transitions defined for state: " + player.getCurrentState());
            return;
        }

        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Transition<PlayerAgent.AgentState> transition : possibleTransitions) {
            cumulativeProbability += transition.getProbability();
            if (rand <= cumulativeProbability) {
                System.out.println("Transitioning from " + player.getCurrentState() + " to " + transition.getTargetState());
                player.setCurrentState(transition.getTargetState()); // Update fan's current state
                return;
            }
        }

        System.out.println("Staying in state: " + player.getCurrentState());
    }

    public PlayerAgent.AgentState getCurrentState() {
        return player.getCurrentState();
    }

}