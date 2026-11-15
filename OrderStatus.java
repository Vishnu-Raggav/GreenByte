package Models;

public enum OrderStatus {
    PENDING("Pending"),
    ASSIGNED("Agent Assigned"),
    DISPATCHED("Dispatched"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
