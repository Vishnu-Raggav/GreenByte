package Models;

import java.time.LocalDateTime;

/** A stock-in event: goods received from a supplier. */
public class StockMovement {
    private final int productId;
    private final String productName;
    private final int quantity;
    private final String supplier;
    private final LocalDateTime date;

    public StockMovement(int productId, String productName, int quantity, String supplier, LocalDateTime date) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.supplier = supplier;
        this.date = date;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public String getSupplier() { return supplier; }
    public LocalDateTime getDate() { return date; }
}
