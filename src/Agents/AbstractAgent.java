package Agents;

import Handlers.SystemHandler;
import java.awt.*;
import java.util.Random;
import Utils.ZoneCoordinates;

public abstract class AbstractAgent<S extends Enum<S>> implements Runnable {

    protected S currentState;
    protected String name;
    protected Thread thread;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private volatile boolean running = true;
    protected Point position = new Point(0, 0);
    private static final int RADIUS = 5; // Maximum radius for random movement
    private Point destination; // Destination point
    private boolean reachedDestination = false; // State to check if destination is reached
    protected Rectangle currentZone; // Current zone where the agent is
    private int speed = SystemHandler.getInstance().getInputVariable("AgentSpeed");

    protected Color color = Color.BLACK;
    private Color nextColor = Color.BLACK;

    public AbstractAgent(String name, S initialState) {
        this.name = name;
        this.currentState = initialState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDestination(Rectangle zone) {
        Random random = new Random();
        int maxX = zone.width - getWidth();
        int maxY = zone.height - getHeight();

        int x = zone.x + random.nextInt(Math.max(1, maxX + 1));
        int y = zone.y + random.nextInt(Math.max(1, maxY + 1));

        this.destination = new Point(x, y);
        this.reachedDestination = false;
    }

    public void updatePosition() {
        if (!reachedDestination && destination != null) {
            moveTowardsDestination();
        } else if (currentZone != null) {
            updatePositionRandomly();
        }
    }

    private void moveTowardsDestination() {
        if (currentZone == null) return;

        int dx = destination.x - position.x;
        int dy = destination.y - position.y;

        // Calculate the total distance to the destination
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= speed) {
            // If close enough, consider that it has arrived
            position.setLocation(destination);
            reachedDestination = true;
            color = nextColor; // Update the color upon arrival
        } else {
            // Calculate increments in X and Y based on speed
            double ratio = speed / distance;
            int stepX = (int) Math.round(dx * ratio);
            int stepY = (int) Math.round(dy * ratio);

            int candidateX = position.x + stepX;
            int candidateY = position.y + stepY;

            // Adjust min and max values to account for agent size
            int minX = currentZone.x;
            int maxX = currentZone.x + currentZone.width - getWidth();
            int minY = currentZone.y;
            int maxY = currentZone.y + currentZone.height - getHeight();

            int newX = Math.max(minX, Math.min(candidateX, maxX));
            int newY = Math.max(minY, Math.min(candidateY, maxY));

            position.setLocation(newX, newY);
        }
    }

    public void updatePositionRandomly() {
        if (currentZone == null) return;

        Random random = new Random();

        int deltaX = random.nextInt(RADIUS * 2 + 1) - RADIUS;
        int deltaY = random.nextInt(RADIUS * 2 + 1) - RADIUS;

        int candidateX = position.x + deltaX;
        int candidateY = position.y + deltaY;

        // Adjust min and max values to account for agent size
        int minX = currentZone.x;
        int maxX = currentZone.x + currentZone.width - getWidth();
        int minY = currentZone.y;
        int maxY = currentZone.y + currentZone.height - getHeight();

        int newX = Math.max(minX, Math.min(candidateX, maxX));
        int newY = Math.max(minY, Math.min(candidateY, maxY));

        this.position.setLocation(newX, newY);
    }

    // Methods to move to different zones
    public void goToFoodZone() {
        currentZone = ZoneCoordinates.FOOD_ZONE;
        setDestination(currentZone);
        nextColor = Color.GREEN; // Set the desired color
    }

    public void goToEntrance() {
        currentZone = ZoneCoordinates.ENTRANCE_ZONE;
        setDestination(currentZone);
        nextColor = Color.BLUE;
    }

    public void goToGeneralZone() {
        currentZone = ZoneCoordinates.GENERAL_ZONE;
        setDestination(currentZone);
        nextColor = Color.GRAY;
    }

    public void goToBathroomZone() {
        currentZone = ZoneCoordinates.BATHROOM_ZONE;
        setDestination(currentZone);
        nextColor = Color.CYAN;
    }

    public void goToStands() {
        currentZone = ZoneCoordinates.STANDS_ZONE;
        setDestination(currentZone);
        nextColor = Color.LIGHT_GRAY;
    }

    public void goToTickets() {
        currentZone = ZoneCoordinates.TICKETS_ZONE;
        setDestination(currentZone);
        nextColor = Color.RED;
    }

    public void goToRegisterZone() {
        currentZone = ZoneCoordinates.REGISTER_ZONE;
        setDestination(currentZone);
        nextColor = Color.PINK;
    }

    public void goToField() {
        currentZone = ZoneCoordinates.FIELD_ZONE;
        setDestination(currentZone);
        nextColor = Color.MAGENTA;
    }

    public void goToBench() {
        currentZone = ZoneCoordinates.BENCH_ZONE;
        setDestination(currentZone);
        nextColor = Color.YELLOW;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
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

    // Modified draw method to accept Graphics parameter
    public abstract void draw(Graphics g);

    // Abstract methods to get agent's dimensions
    protected abstract int getWidth();

    protected abstract int getHeight();
}