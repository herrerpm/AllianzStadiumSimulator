package Agents;

import Handlers.SystemHandler;

import java.awt.*;
import java.util.Random;
import Utils.ZoneCoordinates;

public abstract class AbstractAgent<S extends Enum<S>> implements Runnable {

    protected S currentState;

    public void setName(String name) {
        this.name = name;
    }

    protected String name;
    protected Thread thread;
    private volatile boolean running = true;
    protected Point position = new Point(0,0);
    private static final int RADIUS = 5; // Radio máximo del movimiento aleatorio
    private Point destination; // Punto B
    private boolean reachedDestination = false; // Estado para verificar si llegó al destino
    private Rectangle currentZone; // Current zone where the Thread is at
    private int speed = SystemHandler.getInstance().getInputVariable("AgentSpeed");



    public void setDestination(Rectangle zone) {
        Random random = new Random();
        int x = zone.x + random.nextInt(zone.width);
        int y = zone.y + random.nextInt(zone.height);
        this.destination = new Point(x, y);
        this.reachedDestination = false;
    }

    public void updatePosition() {
        if (!reachedDestination && destination != null) {
            moveTowardsDestination();
        } else if(currentZone != null){
            updatePositionRandomly();
        }
    }

    private void moveTowardsDestination() {
        int dx = destination.x - position.x;
        int dy = destination.y - position.y;

        // Calcula la distancia total al destino
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= speed) {
            // Si está suficientemente cerca, considera que llegó
            position.setLocation(destination);
            reachedDestination = true;
        } else {
            // Calcula los incrementos en X e Y basados en la velocidad
            double ratio = speed / distance;
            int stepX = (int) Math.round(dx * ratio);
            int stepY = (int) Math.round(dy * ratio);

            position.translate(stepX, stepY);
        }
    }

    public void updatePositionRandomly() {
        if (currentZone == null) return;

        Random random = new Random();
        int newX = Math.max(currentZone.x,
                Math.min(currentZone.x + currentZone.width, position.x + random.nextInt(RADIUS * 2 + 1) - RADIUS));
        int newY = Math.max(currentZone.y,
                Math.min(currentZone.y + currentZone.height, position.y + random.nextInt(RADIUS * 2 + 1) - RADIUS));

        this.position.setLocation(newX, newY);
    }


    public void goToFoodZone(){
        currentZone = ZoneCoordinates.FOOD_ZONE;
        setDestination(currentZone);
    }

    public void goToEntrance(){
        currentZone = ZoneCoordinates.ENTRANCE_ZONE;
        setDestination(currentZone);
    }

    public void goToGeneralZone(){
        currentZone = ZoneCoordinates.GENERAL_ZONE;
        setDestination(currentZone);
    }

    public void goToBathroomZone(){
        currentZone = ZoneCoordinates.BATHROOM_ZONE;
        setDestination(currentZone);
    }

    public void goToStands(){
        currentZone = ZoneCoordinates.STANDS_ZONE;
        setDestination(currentZone);
    }

    public void goToTickets(){
        currentZone = ZoneCoordinates.TICKETS_ZONE;
        setDestination(currentZone);
    }

    public void goToRegisterZone(){
        currentZone = ZoneCoordinates.REGISTER_ZONE;
        setDestination(currentZone);
    }

    public void goToField(){
        currentZone = ZoneCoordinates.FIELD_ZONE;
        setDestination(currentZone);
    }

    public void goToBench(){
        currentZone = ZoneCoordinates.BENCH_ZONE;
        setDestination(currentZone);
    }

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

    protected abstract void _run();
    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            _run();
        }
    }
    // Modified draw method to accept Graphics parameter
    public abstract void draw(Graphics g);
}
