package Agents;

import java.awt.*;

public abstract class AbstractAgent<S extends Enum<S>> implements Runnable {

    protected S currentState;
    protected String name;
    protected Thread thread;
    protected Point position = new Point(0,0);

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

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

    // Modified draw method to accept Graphics parameter
    public abstract void draw(Graphics g);
}
