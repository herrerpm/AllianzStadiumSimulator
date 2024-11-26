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
    private static final int RADIUS = 10; // Radio máximo del movimiento aleatorio
    private Point destination; // Punto B
    private boolean reachedDestination = false; // Estado para verificar si llegó al destino
    private Rectangle currentZone; // Current zone where the Thread is at
    private int speed = SystemHandler.getInstance().getInputVariable("AgentSpeed");



    public void setDestination(Point destination) {
        this.destination = destination;
        this.reachedDestination = false; // Resetea el estado de llegada
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
        Random random = new Random();
        Point newPos;
        do {
            int dx = random.nextInt(RADIUS * 2 + 1) - RADIUS; // Desplazamiento aleatorio en X
            int dy = random.nextInt(RADIUS * 2 + 1) - RADIUS; // Desplazamiento aleatorio en Y
            newPos = new Point(position.x + dx, position.y + dy);
        } while (currentZone != null && !ZoneCoordinates.isWithinZone(currentZone, newPos)); // Asegura que la nueva posición esté dentro de los límites

        this.position = newPos;
    }

    public void goToFoodZone(){
        setDestination(ZoneCoordinates.FOOD_ZONE.getLocation());
        currentZone = ZoneCoordinates.FOOD_ZONE;
    }

    public void goToEntrance(){
        setDestination(ZoneCoordinates.ENTRANCE_ZONE.getLocation());
        currentZone = ZoneCoordinates.ENTRANCE_ZONE;
    }

    public void goToGeneralZone(){
        setDestination(ZoneCoordinates.GENERAL_ZONE.getLocation());
        currentZone = ZoneCoordinates.GENERAL_ZONE;
    }

    public void goToBathroomZone(){
        setDestination(ZoneCoordinates.BATHROOM_ZONE.getLocation());
        currentZone = ZoneCoordinates.BATHROOM_ZONE;
    }

    public void goToStands(){
        setDestination(ZoneCoordinates.STANDS_ZONE.getLocation());
        currentZone = ZoneCoordinates.STANDS_ZONE;
    }

    public void goToTickets(){
        setDestination(ZoneCoordinates.TICKETS_ZONE.getLocation());
        currentZone = ZoneCoordinates.TICKETS_ZONE;
    }

    public void goToRegisterZone(){
        setDestination(ZoneCoordinates.REGISTER_ZONE.getLocation());
        currentZone = ZoneCoordinates.REGISTER_ZONE;
    }

    public void goToField(){
        setDestination(ZoneCoordinates.FIELD_ZONE.getLocation());
        currentZone = ZoneCoordinates.FIELD_ZONE;
    }

    public void goToBench(){
        setDestination(ZoneCoordinates.BENCH_ZONE.getLocation());
        currentZone = ZoneCoordinates.BENCH_ZONE;
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

