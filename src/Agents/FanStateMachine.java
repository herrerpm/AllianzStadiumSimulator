package Agents;

import java.util.*;

public class FanStateMachine {
    private FanAgent.AgentState currentState;
    private final Map<FanAgent.AgentState, List<Transition<FanAgent.AgentState>>> transitions = new HashMap<>();
    private final Random random = new Random();
    private final FanAgent fan; // Reference to the fan agent

    public FanStateMachine(FanAgent fan) {
        this.fan = fan;
        this.currentState = fan.getCurrentState(); // Initialize with the fan's current state
    }

    public void addTransition(FanAgent.AgentState fromState, FanAgent.AgentState toState, double probability) {
        transitions.computeIfAbsent(fromState, k -> new ArrayList<>()).add(new Transition<>(toState, probability));
    }

    public void nextState() {
        List<Transition<FanAgent.AgentState>> possibleTransitions = transitions.get(currentState);
        if (possibleTransitions == null || possibleTransitions.isEmpty()) {
            System.out.println("No transitions defined for state: " + currentState);
            return;
        }

        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Transition<FanAgent.AgentState> transition : possibleTransitions) {
            cumulativeProbability += transition.getProbability();
            if (rand <= cumulativeProbability) {
                System.out.println("Transitioning from " + currentState + " to " + transition.getTargetState());
                currentState = transition.getTargetState();
                fan.setCurrentState(currentState); // Update fan's current state
                return;
            }
        }

        System.out.println("Staying in state: " + currentState);
    }

    public FanAgent.AgentState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(FanAgent.AgentState state) {
        this.currentState = state;
        fan.currentState = state;
    }
}