package Models;

import java.time.LocalDate;

public class Delivery {
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_DISPATCHED = "Dispatched";
    public static final String STATUS_DELIVERED = "Delivered";
    public static final String[] STATUSES = {STATUS_SCHEDULED, STATUS_DISPATCHED, STATUS_DELIVERED};

    private int id;
    private int orderId;
    private int agentId;
    private String agentName;
    private LocalDate scheduledDate;
    private String status;

    public Delivery(int id, int orderId, int agentId, String agentName, LocalDate scheduledDate, String status) {
        this.id = id;
        this.orderId = orderId;
        this.agentId = agentId;
        this.agentName = agentName;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
