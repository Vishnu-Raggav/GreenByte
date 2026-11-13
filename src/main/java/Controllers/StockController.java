package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class StockController {

    private static final int DEFAULT_REORDER_LEVEL = 10;

    public StockView view;
    public StockRepository repository;
    public ProductRepository productRepository;

    public StockController(StockView stockView, StockRepository stockRepository, ProductRepository productRepository) {
        // Initialize
        view = stockView;
        repository = stockRepository;
        this.productRepository = productRepository;

        // Add Data (product ids match the sample products)
        repository.addStockItem(new StockItem(1, 25, 10));
        repository.addStockItem(new StockItem(2, 6, 10));
        repository.addStockItem(new StockItem(3, 40, 15));
        repository.addStockItem(new StockItem(4, 12, 12));
        repository.addStockItem(new StockItem(5, 30, 8));

        syncWithProducts();

        // Show Data
        view.displayStocks(repository.getAllStockItems());

        // Products may have been added or removed since this panel was last shown
        view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                syncWithProducts();
                view.displayStocks(repository.getAllStockItems());
            }
        });

        view.stockChangePanel.addStockButton.addActionListener(e -> {
            changeStock(true);
        });

        view.stockChangePanel.reduceStockButton.addActionListener(e -> {
            changeStock(false);
        });
    }

    private void changeStock(boolean isAdding) {

        String productId = view.stockChangePanel.productIdTextField.getText().trim();
        String quantity = view.stockChangePanel.quantityTextField.getText().trim();

        if (productId.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int productIdInt;
        int quantityInt;

        try {
            productIdInt = Integer.parseInt(productId);
            quantityInt = Integer.parseInt(quantity);
        } catch (NumberFormatException err) {
            JOptionPane.showMessageDialog(null, "Whole numbers required for product id and quantity", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StockItem stockItem = repository.getStockItemByProductId(productIdInt);

        if (stockItem == null) {
            JOptionPane.showMessageDialog(null, "No product exists with this id!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantityInt <= 0) {
            JOptionPane.showMessageDialog(null, "Quantity must be greater than 0!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (isAdding) {
            stockItem.increaseQuantity(quantityInt);
        } else if (!stockItem.decreaseQuantity(quantityInt)) {
            JOptionPane.showMessageDialog(null, "Not enough stock! Only " + stockItem.getQuantityOnHand() + " units on hand.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        repository.updateStockItem(stockItem);
        view.displayStocks(repository.getAllStockItems());

        JOptionPane.showMessageDialog(null, "Stock updated! Product " + productIdInt + " now has " + stockItem.getQuantityOnHand() + " units.", "Success", JOptionPane.INFORMATION_MESSAGE);

        clearTextFields(new JTextField[]{
                view.stockChangePanel.productIdTextField,
                view.stockChangePanel.quantityTextField
        });
    }

    // Every product gets a stock row, and stock rows of removed products are deleted
    private void syncWithProducts() {

        ArrayList<Product> products = productRepository.getAllProducts();

        for (Product product : products) {
            if (repository.getStockItemByProductId(product.getId()) == null) {
                repository.addStockItem(new StockItem(product.getId(), 0, DEFAULT_REORDER_LEVEL));
            }
        }

        for (StockItem stockItem : repository.getAllStockItems()) {
            if (!productExists(stockItem.getProductId())) {
                repository.removeStockItem(stockItem.getProductId());
            }
        }
    }

    private boolean productExists(int productId) {
        for (Product product : productRepository.getAllProducts()) {
            if (product.getId() == productId) {
                return true;
            }
        }
        return false;
    }

    private void clearTextFields(JTextField[] textFields) {
        for (JTextField textField : textFields) {
            textField.setText("");
        }
    }
}