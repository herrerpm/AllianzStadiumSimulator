package Agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SellingHandler {
    private static SellingHandler instance = null;
    private final Map<String, TicketSellerAgent> sellerMap;
    private final Random random = new Random();

    private SellingHandler(int numberOfSellers) {
        sellerMap = new HashMap<>();
        for (int i = 1; i <= numberOfSellers; i++) {
            String sellerName = "Seller-" + i;
            TicketSellerAgent seller = new TicketSellerAgent(sellerName, this);
            sellerMap.put(sellerName, seller);
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
        if (sellerMap.isEmpty()) {
            System.out.println("No sellers available for " + fanAgent.getName() + ". Request ignored.");
            return;
        }
        String[] sellerKeys = sellerMap.keySet().toArray(new String[0]);
        String randomSellerKey = sellerKeys[random.nextInt(sellerKeys.length)];
        TicketSellerAgent seller = sellerMap.get(randomSellerKey);
        System.out.println(fanAgent.getName() + " is assigned to " + seller.getName());
        seller.addRequest(fanAgent);
    }

    public synchronized List<TicketSellerAgent> getAvailableSellers() {
        List<TicketSellerAgent> availableSellers = new ArrayList<>();
        for (TicketSellerAgent seller : sellerMap.values()) {
            if (seller.getCurrentState() == TicketSellerAgent.AgentState.WAITING) {
                availableSellers.add(seller);
            }
        }
        return availableSellers;
    }

    public synchronized List<TicketSellerAgent> getSellingSellers() {
        List<TicketSellerAgent> sellingSellers = new ArrayList<>();
        for (TicketSellerAgent seller : sellerMap.values()) {
            if (seller.getCurrentState() == TicketSellerAgent.AgentState.SELLING) {
                sellingSellers.add(seller);
            }
        }
        return sellingSellers;
    }
}
