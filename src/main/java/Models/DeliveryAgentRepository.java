package Models;

import java.util.ArrayList;

public class DeliveryAgentRepository {

    private ArrayList<DeliveryAgent> deliveryAgents = new ArrayList<>();

    public void addDeliveryAgent(DeliveryAgent deliveryAgent) {
        deliveryAgents.add(deliveryAgent);
    }

    public ArrayList<DeliveryAgent> getAllDeliveryAgents() {
        return new ArrayList<>(deliveryAgents);
    }

    /** Returns the agent with the given id, or null when there is none. */
    public DeliveryAgent getDeliveryAgentById(int id) {
        for (DeliveryAgent deliveryAgent : deliveryAgents) {
            if (deliveryAgent.getId() == id) {
                return deliveryAgent;
            }
        }
        return null;
    }

    public void updateDeliveryAgent(DeliveryAgent updatedDeliveryAgent) {
        for (int i = 0; i < deliveryAgents.size(); i++) {
            if (deliveryAgents.get(i).getId() == updatedDeliveryAgent.getId()) {
                deliveryAgents.set(i, updatedDeliveryAgent);
                return;
            }
        }
    }

    public void removeDeliveryAgent(int id) {
        deliveryAgents.removeIf(deliveryAgent -> deliveryAgent.getId() == id);
    }
}
