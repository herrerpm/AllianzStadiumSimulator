package Agents;

public class Transition<S extends Enum<S>> {
    private final S targetState;
    private final double probability;

    public Transition(S targetState, double probability) {
        this.targetState = targetState;
        this.probability = probability;
    }

    public S getTargetState() {
        return targetState;
    }

    public double getProbability() {
        return probability;
    }
}
