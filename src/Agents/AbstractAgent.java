package Agents;

public abstract class AbstractAgent<S extends Enum<S>> implements Runnable {

    protected S currentState;
    protected String name;
    protected Thread thread;
    private volatile boolean running = true;
    public AbstractAgent(String name, S initialState) {
        this.name = name;
        this.currentState = initialState;
    }

    public S getCurrentState() {
        return currentState;
    }

    public void setCurrentState(S state) {
        this.currentState = state;
    }

    public String getName() {
        return name;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    protected abstract void _run();
    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            _run();
        }
    }
}