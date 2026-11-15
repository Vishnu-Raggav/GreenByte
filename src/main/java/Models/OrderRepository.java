package Models;

import java.util.ArrayList;

public class OrderRepository {

    private ArrayList<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public ArrayList<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public void updateOrder(Order updatedOrder) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == updatedOrder.getId()) {
                orders.set(i, updatedOrder);
                return;
            }
        }
    }

    public void removeOrder(int id) {
        orders.removeIf(order -> order.getId() == id);
    }

    /** Returns the next free order id (highest existing id + 1, or 1 when empty). */
    public int getNextId() {
        int maxId = 0;
        for (Order order : orders) {
            if (order.getId() > maxId) {
                maxId = order.getId();
            }
        }
        return maxId + 1;
    }
}
