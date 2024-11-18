package Agents;

import java.util.*;

public class StateMachine<S extends Enum<S>> {
    private S currentState;
    private final Map<S, List<Transition<S>>> transitions = new HashMap<>();
    private final Random random = new Random();

    public StateMachine(S initialState) {
        this.currentState = initialState;
    }

    // Add a transition from a state to another with a certain probability
    public void addTransition(S fromState, S toState, double probability) {
        transitions.computeIfAbsent(fromState, k -> new ArrayList<>()).add(new Transition<>(toState, probability));
    }

    // Determine and set the next state based on current state and probabilities
    public void nextState() {
        List<Transition<S>> possibleTransitions = transitions.get(currentState);
        if (possibleTransitions == null || possibleTransitions.isEmpty()) {
            System.out.println("No transitions defined for state: " + currentState);
            return;
        }

        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Transition<S> transition : possibleTransitions) {
            cumulativeProbability += transition.getProbability();
            if (rand <= cumulativeProbability) {
                System.out.println("Transitioning from " + currentState + " to " + transition.getTargetState());
                currentState = transition.getTargetState();
                return;
            }
        }

        // If no transition was taken, stay in the current state
        System.out.println("Staying in state: " + currentState);
    }

    public S getCurrentState() {
        return currentState;
    }

    public void setCurrentState(S state) {
        this.currentState = state;
    }
}
