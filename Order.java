package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order implements Identifiable {
    private final int id;
    private final Client client;
    private final LocalDate orderDate;
    private final List<OrderItem> items;
    private OrderStatus status = OrderStatus.PENDING;
    private DeliveryAgent agent;
    private LocalDate deliveryDate;

    public Order(int id, Client client, LocalDate orderDate, List<OrderItem> items) {
        this.id = id;
        this.client = client;
        this.orderDate = orderDate;
        this.items = new ArrayList<>(items);
    }

    @Override
    public int getId() { return id; }
    public Client getClient() { return client; }
    public LocalDate getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public OrderStatus getStatus() { return status; }
    public DeliveryAgent getAgent() { return agent; }
    public LocalDate getDeliveryDate() { return deliveryDate; }

    public void setStatus(OrderStatus status) { this.status = status; }
    public void setAgent(DeliveryAgent agent) { this.agent = agent; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public int getTotalQuantity() {
        int qty = 0;
        for (OrderItem item : items) {
            qty += item.getQuantity();
        }
        return qty;
    }
}
