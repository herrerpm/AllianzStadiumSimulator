package Agents;

import java.util.ArrayList;
import java.util.List;


public class FanHandler {

    private final List<FanAgent> fanAgents;
    private final List<Thread> fanThreads;
    private FanHandler() {
        this.fanAgents = new ArrayList<>();
        this.fanThreads = new ArrayList<>();
    }

    private static class Holder {
        private static final FanHandler INSTANCE = new FanHandler();
    }

    public static FanHandler getInstance() {
        return Holder.INSTANCE;
    }

    public void createFans(int numberOfFans, SellingHandler sellingHandler, int simulationSteps) {
        for (int i = 1; i <= numberOfFans; i++) {
            String fanName = "Fan-" + i;
            FanAgent fanAgent = new FanAgent(fanName, sellingHandler, simulationSteps);
            Thread fanThread = new Thread(fanAgent, fanName + "-Thread");

            fanAgents.add(fanAgent);
            fanThreads.add(fanThread);

            fanThread.start();
        }
    }

    public List<FanAgent> getFanAgents() {
        return new ArrayList<>(fanAgents);
    }

    public List<Thread> getFanThreads() {
        return new ArrayList<>(fanThreads);
    }

    public int getFanBuyingTicketCount() {
        return (int) fanAgents.stream()
                .filter(fan -> fan.getCurrentState() == FanAgent.AgentState.BUYING_TICKET)
                .count();
    }

    public int getFanBuyingFoodCount() {
        return (int) fanAgents.stream()
                .filter(fan -> fan.getCurrentState() == FanAgent.AgentState.BUYING_FOOD)
                .count();
    }

    public int getFanBathroomCount() {
        return (int) fanAgents.stream()
                .filter(fan -> fan.getCurrentState() == FanAgent.AgentState.BATHROOM)
                .count();
    }

    public int getFanWatchingGameCount() {
        return (int) fanAgents.stream()
                .filter(fan -> fan.getCurrentState() == FanAgent.AgentState.WATCHING_GAME)
                .count();
    }

    public int getFanGeneralZoneCount() {
        return (int) fanAgents.stream()
                .filter(fan -> fan.getCurrentState() == FanAgent.AgentState.GENERAL_ZONE)
                .count();
    }

    public void waitForAllFansToComplete() {
        for (Thread thread : fanThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("FanHandler interrupted while waiting for fans to complete.");
            }
        }
    }
}
