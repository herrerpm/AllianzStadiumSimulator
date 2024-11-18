package Agents;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SellingHandler {
    private static SellingHandler instance = null;
    private final BlockingQueue<TicketSellerAgent> availableSellers;

    private SellingHandler(int numberOfSellers) {
        availableSellers = new LinkedBlockingQueue<>();
        for (int i = 1; i <= numberOfSellers; i++) {
            TicketSellerAgent seller = new TicketSellerAgent("Seller-" + i, this);
            availableSellers.add(seller);
            Thread sellerThread = new Thread(seller);
            sellerThread.start();
        }
    }

    public static synchronized SellingHandler getInstance(int numberOfSellers) {
        if (instance == null) {
            instance = new SellingHandler(numberOfSellers);
        }
        return instance;
    }

    public void handleTicketRequest(FanAgent fanAgent) {
        try {
            // Take an available seller (blocks if none are available)
            TicketSellerAgent seller = availableSellers.take();
            System.out.println(fanAgent.getName() + " is assigned to " + seller.getName());
            // Assign the request to the seller
            seller.assignTicketRequest(fanAgent);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("SellingHandler interrupted while waiting for available seller.");
        }
    }

    public void notifySellerAvailable(TicketSellerAgent seller) {
        try {
            availableSellers.put(seller);
            System.out.println(seller.getName() + " is now available.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("SellingHandler interrupted while notifying seller availability.");
        }
    }
}
