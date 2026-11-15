package Models;

import java.util.ArrayList;

public class DeliveryRepository {

    private ArrayList<Delivery> deliveries = new ArrayList<>();

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    public ArrayList<Delivery> getAllDeliveries() {
        return new ArrayList<>(deliveries);
    }

    public void updateDelivery(Delivery updatedDelivery) {
        for (int i = 0; i < deliveries.size(); i++) {
            if (deliveries.get(i).getId() == updatedDelivery.getId()) {
                deliveries.set(i, updatedDelivery);
                return;
            }
        }
    }

    public void removeDelivery(int id) {
        deliveries.removeIf(delivery -> delivery.getId() == id);
    }

    /** Returns the next free delivery id (highest existing id + 1, or 1 when empty). */
    public int getNextId() {
        int maxId = 0;
        for (Delivery delivery : deliveries) {
            if (delivery.getId() > maxId) {
                maxId = delivery.getId();
            }
        }
        return maxId + 1;
    }
}
