package Models;

import java.util.ArrayList;

public class StockRepository {

    private ArrayList<StockItem> stockItems = new ArrayList<>();

    public void addStockItem(StockItem stockItem) {
        stockItems.add(stockItem);
    }

    public ArrayList<StockItem> getAllStockItems() {
        return new ArrayList<>(stockItems);
    }

    public StockItem getStockItemByProductId(int productId) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProductId() == productId) {
                return stockItem;
            }
        }
        return null;
    }

    public void updateStockItem(StockItem updatedStockItem) {
        for (int i = 0; i < stockItems.size(); i++) {
            if (stockItems.get(i).getProductId() == updatedStockItem.getProductId()) {
                stockItems.set(i, updatedStockItem);
                return;
            }
        }
    }

    public void removeStockItem(int productId) {
        stockItems.removeIf(stockItem -> stockItem.getProductId() == productId);
    }

    public ArrayList<StockItem> getLowStockItems() {
        ArrayList<StockItem> lowStockItems = new ArrayList<>();
        for (StockItem stockItem : stockItems) {
            if (stockItem.isLowStock()) {
                lowStockItems.add(stockItem);
            }
        }
        return lowStockItems;
    }
}