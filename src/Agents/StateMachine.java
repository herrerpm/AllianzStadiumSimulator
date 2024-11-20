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

        // Compute total probability to handle floating-point inaccuracies
        double totalProbability = 0.0;
        for (Transition<S> transition : possibleTransitions) {
            totalProbability += transition.getProbability();
        }

        double rand = random.nextDouble() * totalProbability;
        double cumulativeProbability = 0.0;
        for (Transition<S> transition : possibleTransitions) {
            cumulativeProbability += transition.getProbability();
            if (rand <= cumulativeProbability) {
                System.out.println("Transitioning from " + currentState + " to " + transition.getTargetState());
                currentState = transition.getTargetState();
                return;
            }
        }

        // Fallback in case of rounding errors
        Transition<S> lastTransition = possibleTransitions.get(possibleTransitions.size() - 1);
        System.out.println("Transitioning from " + currentState + " to " + lastTransition.getTargetState() + " (by default)");
        currentState = lastTransition.getTargetState();
    }


    public S getCurrentState() {
        return currentState;
    }

    public void setCurrentState(S state) {
        this.currentState = state;
    }
}
