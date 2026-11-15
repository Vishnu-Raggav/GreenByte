package Models;

public class ProductSales {
    private int productId;
    private String productName;
    private int quantitySold;
    private double revenue;

    public ProductSales(int productId, String productName, int quantitySold, double revenue) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    /** Adds one order line to the running totals of this product. */
    public void addSale(int quantity, double amount) {
        quantitySold += quantity;
        revenue += amount;
    }
}
