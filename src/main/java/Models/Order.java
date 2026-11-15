package Models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    public static final String STATUS_PLACED = "Placed";

    private int id;
    private int clientId;
    private String clientName;
    private LocalDate orderDate;
    private String status;
    private ArrayList<OrderItem> items;

    public Order(int id, int clientId, String clientName, LocalDate orderDate, String status, ArrayList<OrderItem> items) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.status = status;
        this.items = new ArrayList<>(items);
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = new ArrayList<>(items);
    }

    /** Sum of all line totals; used by the Orders screen and by the reports module. */
    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }
}
