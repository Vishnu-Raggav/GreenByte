package Models;

public class LowStockAlert {
    private int productId;
    private String productName;
    private int quantityOnHand;
    private int reorderLevel;

    public LowStockAlert(int productId, String productName, int quantityOnHand, int reorderLevel) {
        this.productId = productId;
        this.productName = productName;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    /** Text shown in the alert table: empty shelves are more urgent than merely low ones. */
    public String getAlertText() {
        return quantityOnHand == 0 ? "Out of stock" : "Low stock";
    }
}
