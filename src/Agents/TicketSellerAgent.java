package Agents;

import java.util.LinkedList;
import java.util.Queue;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {

    public enum AgentState {
        SELLING,
        WAITING
    }

    private final String name;
    private final SellingHandler handler;
    private final Object sellerLock = new Object();
    private final Queue<FanAgent> requestQueue = new LinkedList<>();

    public TicketSellerAgent(String name, SellingHandler handler) {
        super(AgentState.WAITING);
        this.name = name;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public Object getSellerLock() {
        return sellerLock;
    }

    public void addRequest(FanAgent fanAgent) {
        synchronized (requestQueue) {
            requestQueue.add(fanAgent);
            requestQueue.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            FanAgent fanAgent = null;
            synchronized (requestQueue) {
                while (requestQueue.isEmpty()) {
                    try {
                        requestQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println(name + " was interrupted.");
                        return;
                    }
                }
                fanAgent = requestQueue.poll();
            }

            if (fanAgent != null) {
                processRequest(fanAgent);
            }
        }
    }

    private void processRequest(FanAgent fanAgent) {
        Object firstLock, secondLock;
        if (System.identityHashCode(this) < System.identityHashCode(fanAgent)) {
            firstLock = this.sellerLock;
            secondLock = fanAgent.getFanLock();
        } else {
            firstLock = fanAgent.getFanLock();
            secondLock = this.sellerLock;
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                System.out.println(name + " is selling a ticket to " + fanAgent.getName());
                sellTicket(fanAgent);
                fanAgent.getFanLock().notify();
            }
        }
    }

    private void sellTicket(FanAgent fanAgent) {
        System.out.println(name + " starts selling a ticket to " + fanAgent.getName());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " interrupted while selling a ticket.");
        }
        System.out.println(name + " has sold a ticket to " + fanAgent.getName());
    }
}
