package Network;

import Agents.FanAgent;
import Handlers.FanHandler;
import Handlers.SystemHandler;

public class MessageHandler {
    public static void handleMessage(String message) {
        String[] parts = message.split(",");
        if (parts.length == 3) {
            String command = parts[0].trim();
            String name = parts[1].trim();
            String zone = parts[2].trim();
            System.out.println("Command: " + command);
            System.out.println("Name: " + name);
            System.out.println("Zone: " + zone);
            if (command.equals("create")) {
                FanAgent fan = FanHandler.getInstance()
                        .createCustomAgent(name + " From: " + zone);
                        fan.setCurrentState(FanAgent.AgentState.REGISTER);
                try {
                    fan.goToRegisterZone();
                    Thread.sleep(SystemHandler.getInstance().getInputVariable("RegisterTime"));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
