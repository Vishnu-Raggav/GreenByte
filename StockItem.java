package Models;

/** Inventory record for one product: quantity on hand and its reorder level. */
public class StockItem {
    private final int productId;
    private int quantityOnHand;
    private int reorderLevel;

    public StockItem(int productId, int quantityOnHand, int reorderLevel) {
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }

    public int getProductId() { return productId; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public int getReorderLevel() { return reorderLevel; }

    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public void add(int qty) { quantityOnHand += qty; }
    public void deduct(int qty) { quantityOnHand -= qty; }

    /** A product is low on stock when quantity is at or below its reorder level. */
    public boolean isLow() {
        return quantityOnHand <= reorderLevel;
    }
}
