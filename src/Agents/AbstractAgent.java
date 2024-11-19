package Agents;

public abstract class AbstractAgent<S extends Enum<S>> {
    private S state;

    public AbstractAgent(S initialState) {
        this.state = initialState;
    }

    public S getState() {
        return state;
    }

    public void setState(S state) {
        this.state = state;
    }

    public abstract void performAction();
}
