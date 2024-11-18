package Agents;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TicketSellerAgent extends AbstractAgent<TicketSellerAgent.AgentState> implements Runnable {

    public enum AgentState {
        SELLING,
        WAITING
    }

    private final String name;
    private final SellingHandler handler;
    private final BlockingQueue<FanAgent> ticketRequests;

    public TicketSellerAgent(String name, SellingHandler handler) {
        super(AgentState.WAITING);
        this.name = name;
        this.handler = handler;
        this.ticketRequests = new LinkedBlockingQueue<>();
    }

    public String getName() {
        return name;
    }

    public void assignTicketRequest(FanAgent fanAgent) {
        try {
            ticketRequests.put(fanAgent);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " interrupted while receiving a ticket request.");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                FanAgent fanAgent = ticketRequests.take();
                setCurrentState(AgentState.SELLING);
                performAction(fanAgent);
                setCurrentState(AgentState.WAITING);
                handler.notifySellerAvailable(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " thread interrupted.");
                break;
            }
        }
    }


    public void performAction(FanAgent fanAgent) {
        System.out.println(name + " is selling a ticket to " + fanAgent.getName());
        sellTicket(fanAgent);
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
