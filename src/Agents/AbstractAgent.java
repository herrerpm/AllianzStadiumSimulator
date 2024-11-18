package Agents;

public abstract class AbstractAgent<S extends Enum<S>> {

    protected S currentState;

    public AbstractAgent(S initialState) {
        this.currentState = initialState;
    }

    public S getCurrentState() {
        return currentState;
    }

    public void setCurrentState(S state) {
        this.currentState = state;
    }

}