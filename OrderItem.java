package Models;

/** One line of an order. Name and price are copied from the product at order time. */
public class OrderItem {
    private final int productId;
    private final String productName;
    private final double unitPrice;
    private int quantity;

    public OrderItem(int productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public void addQuantity(int qty) { quantity += qty; }

    public double getSubtotal() {
        return unitPrice * quantity;
    }
}
