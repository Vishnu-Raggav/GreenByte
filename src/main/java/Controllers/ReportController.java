package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.ParseException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportController {

    private static final String STATUS_CANCELLED = "Cancelled";

    public ReportView view;
    public OrderRepository orderRepository;
    public ProductRepository productRepository;
    public StockRepository stockRepository;

    public ReportController(ReportView reportView, OrderRepository orderRepository,
                            ProductRepository productRepository, StockRepository stockRepository) {
        // Initialize
        view = reportView;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;

        // Show Data
        generateReport();

        // Orders, products and stock may have changed in other modules, so rebuild the report whenever this screen is shown
        view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                generateReport();
            }
        });

        view.generateButton.addActionListener(e -> {
            generateReport();
        });
    }

    private void generateReport() {
        YearMonth month = getSelectedMonth();

        if (month == null) {
            return;
        }

        showSalesReport(getOrdersOfMonth(month));
        showInventoryReport();
    }

    private YearMonth getSelectedMonth() {
        try {
            view.yearSpinner.commitEdit();
        } catch (ParseException err) {
            view.yearSpinner.setValue(view.yearSpinner.getValue());
            JOptionPane.showMessageDialog(null,
                    "Year must be a number between " + ReportView.MIN_YEAR + " and " + ReportView.MAX_YEAR,
                    "Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int year = (int) view.yearSpinner.getValue();
        int monthNumber = view.monthCombo.getSelectedIndex() + 1;
        return YearMonth.of(year, monthNumber);
    }

    private ArrayList<Order> getOrdersOfMonth(YearMonth month) {
        ArrayList<Order> monthOrders = new ArrayList<>();

        for (Order order : orderRepository.getAllOrders()) {
            if (order.getOrderDate() != null && YearMonth.from(order.getOrderDate()).equals(month)) {
                monthOrders.add(order);
            }
        }

        return monthOrders;
    }

    private void showSalesReport(ArrayList<Order> monthOrders) {
        Map<String, Integer> statusCounts = new LinkedHashMap<>();
        Map<Integer, ProductSales> salesByProduct = new LinkedHashMap<>();
        int cancelledOrders = 0;
        int validOrders = 0;
        double revenue = 0;

        for (Order order : monthOrders) {
            statusCounts.merge(order.getStatus(), 1, Integer::sum);

            if (STATUS_CANCELLED.equalsIgnoreCase(order.getStatus())) {
                cancelledOrders++;
                continue;
            }

            validOrders++;
            revenue += order.getTotal();

            for (OrderItem item : order.getItems()) {
                ProductSales sales = salesByProduct.get(item.getProductId());

                if (sales == null) {
                    sales = new ProductSales(item.getProductId(), item.getProductName(), 0, 0);
                    salesByProduct.put(item.getProductId(), sales);
                }

                sales.addSale(item.getQuantity(), item.getLineTotal());
            }
        }

        double averageOrder = validOrders == 0 ? 0 : revenue / validOrders;

        ArrayList<ProductSales> rankedSales = new ArrayList<>(salesByProduct.values());
        rankedSales.sort(Comparator.comparingDouble(ProductSales::getRevenue).reversed());

        view.displaySalesSummary(monthOrders.size(), cancelledOrders, revenue, averageOrder);
        view.displayProductSales(rankedSales);
        view.displayStatusCounts(statusCounts);
    }

    private void showInventoryReport() {
        Map<Integer, Product> productsById = new HashMap<>();

        for (Product product : productRepository.getAllProducts()) {
            productsById.put(product.getId(), product);
        }

        ArrayList<LowStockAlert> alerts = new ArrayList<>();
        int unitsOnHand = 0;
        double stockValue = 0;

        for (StockItem stockItem : stockRepository.getAllStockItems()) {
            Product product = productsById.get(stockItem.getProductId());

            if (product == null) {
                continue;
            }

            unitsOnHand += stockItem.getQuantityOnHand();
            stockValue += stockItem.getQuantityOnHand() * product.getPrice();

            if (stockItem.isLowStock()) {
                alerts.add(new LowStockAlert(product.getId(), product.getName(),
                        stockItem.getQuantityOnHand(), stockItem.getReorderLevel()));
            }
        }

        alerts.sort(Comparator.comparingInt(LowStockAlert::getQuantityOnHand));

        view.displayInventorySummary(productsById.size(), unitsOnHand, stockValue, alerts.size());
        view.displayLowStock(alerts);
    }
}
