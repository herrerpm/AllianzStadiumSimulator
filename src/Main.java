import Agents.FanAgent;
import Agents.SellingHandler;

public class Main {
    public static void main(String[] args) {
        System.out.println("Agent Simulation Started!\n");

        int numberOfSellers = 3;
        SellingHandler sellingHandler = SellingHandler.getInstance(numberOfSellers);
        FanAgent fan1 = new FanAgent("FanAgent-1", sellingHandler, 5);
        FanAgent fan2 = new FanAgent("FanAgent-2", sellingHandler, 5);
        FanAgent fan3 = new FanAgent("FanAgent-3", sellingHandler, 5);
        Thread fanThread1 = new Thread(fan1);
        Thread fanThread2 = new Thread(fan2);
        Thread fanThread3 = new Thread(fan3);
        fanThread1.start();
        fanThread2.start();
        fanThread3.start();

        try {
            fanThread1.join();
            fanThread2.join();
            fanThread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted while waiting for FanAgents to finish.");
        }

        System.out.println("Agent Simulation Ended!");
        System.exit(0);
    }
}
