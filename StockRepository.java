package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockRepository {
    public static final int DEFAULT_REORDER_LEVEL = 5;

    private final Map<Integer, StockItem> items = new HashMap<>();
    private final ArrayList<StockMovement> movements = new ArrayList<>();

    /** Returns the stock record for a product, creating an empty one if needed. */
    public StockItem getOrCreate(int productId) {
        return items.computeIfAbsent(productId, id -> new StockItem(id, 0, DEFAULT_REORDER_LEVEL));
    }

    public void setStock(int productId, int quantity, int reorderLevel) {
        items.put(productId, new StockItem(productId, quantity, reorderLevel));
    }

    public int getQuantity(int productId) {
        return getOrCreate(productId).getQuantityOnHand();
    }

    public void stockIn(Product product, int quantity, String supplier) {
        getOrCreate(product.getId()).add(quantity);
        movements.add(new StockMovement(product.getId(), product.getName(), quantity, supplier, LocalDateTime.now()));
    }

    public void deduct(int productId, int quantity) {
        getOrCreate(productId).deduct(quantity);
    }

    public void restore(int productId, int quantity) {
        getOrCreate(productId).add(quantity);
    }

    public ArrayList<StockMovement> getMovements() {
        return new ArrayList<>(movements);
    }
}
