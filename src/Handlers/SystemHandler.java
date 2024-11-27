package Handlers;

import Buffers.BathroomBuffer;
import Buffers.GameBuffer;
import Managers.FanFoodSellerTransactionManager;
import Managers.FanTicketSellerTransactionManager;

import java.util.Map;
import java.util.HashMap;

public class SystemHandler {
    private static SystemHandler instance;
    private  FanHandler fanHandler;
    private  TicketSellingHandler ticketSellingHandler;
    private  FoodSellingHandler foodSellingHandler;
    private PlayerHandler playerHandler;


    private final Map<String, Integer> inputVariables = new HashMap<>();
    private final Map<String, String> systemVariables = new HashMap<>();
    private SystemHandler() {
        inputVariables.put("nfans", 20);
        inputVariables.put("capacidadEstadio", 100);
        inputVariables.put("vendedoresBoletos", 5);
        inputVariables.put("vendedoresComida", 5);
        inputVariables.put("njugadores", 5);
        inputVariables.put("capacidadBaños", 6);
        inputVariables.put("TicketSellerTime", 3000);
        inputVariables.put("FoodSellerTime", 3000);
        inputVariables.put("PlayerStateChangeTime", 10000);
        inputVariables.put("AgentSpeed", 100);
        inputVariables.put("BathroomTime", 2000);
        inputVariables.put("FanStateChangeTime", 3000);
        inputVariables.put("TicketSellerTerminateTime", 10000);
        inputVariables.put("UpdateInterval", 500);
        inputVariables.put("RegisterTime", 1000);
        inputVariables.put("EnteringStadium", 1000);
    }
    public static SystemHandler getInstance(){
        if(instance == null){
            instance = new SystemHandler();
        }
        return instance;
    }

    public Map<String, Integer> getInputVariables(){
        return inputVariables;
    }
    public Map<String, String> getSystemVariables(){
        return systemVariables;
    }

    public int getInputVariable(String key) {
        return inputVariables.getOrDefault(key, 0);
    }
    public void instantiateAgents(){
        fanHandler = FanHandler.getInstance();
        ticketSellingHandler = TicketSellingHandler.getInstance();
        foodSellingHandler = FoodSellingHandler.getInstance();
        playerHandler = PlayerHandler.getInstance();

        int nfans = getInputVariable("nfans");
        int vendedoresBoletos = getInputVariable("vendedoresBoletos");
        int njugadores = getInputVariable("njugadores");
        int vendedoresComida = getInputVariable("vendedoresComida");
        int ticketSellerTime = getInputVariable("TicketSellerTime");
        int foodSellerTime = getInputVariable("FoodSellerTime");

        FanHandler.getInstance().createAgents(nfans);
        TicketSellingHandler.getInstance().createAgents(vendedoresBoletos);
        FoodSellingHandler.getInstance().createAgents(vendedoresComida);
        PlayerHandler.getInstance().createAgents(njugadores);

        FanTicketSellerTransactionManager.getInstance().configure(
                fanHandler,
                ticketSellingHandler,
                ticketSellerTime
        );

        FanFoodSellerTransactionManager.getInstance().configure(
                fanHandler,
                foodSellingHandler,
                foodSellerTime
        );

        BathroomBuffer.getInstance(SystemHandler.getInstance().getInputVariable("capacidadBaños"));
        GameBuffer.getInstance(SystemHandler.getInstance().getInputVariable("njugadores"));

    }

    public void updateVariables(Map<String, Integer> updatedVariables) {
        inputVariables.putAll(updatedVariables);

    }
}