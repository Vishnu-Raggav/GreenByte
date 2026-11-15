package Views;

import Models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class ReportView extends JPanel {

    public static final int MIN_YEAR = 2000;
    public static final int MAX_YEAR = 2100;

    public JComboBox<String> monthCombo;
    public JSpinner yearSpinner;
    public JButton generateButton;

    public JTabbedPane reportTabs;

    public JLabel ordersPlacedLabel;
    public JLabel cancelledOrdersLabel;
    public JLabel revenueLabel;
    public JLabel averageOrderLabel;
    public JTable productSalesTable;
    public DefaultTableModel productSalesTableModel;
    public JTable statusTable;
    public DefaultTableModel statusTableModel;

    public JLabel productCountLabel;
    public JLabel unitsOnHandLabel;
    public JLabel stockValueLabel;
    public JLabel lowStockCountLabel;
    public JTable lowStockTable;
    public DefaultTableModel lowStockTableModel;

    public ReportView() {
        setLayout(new BorderLayout());

        // Month selector
        monthCombo = new JComboBox<>();
        for (Month month : Month.values()) {
            monthCombo.addItem(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        yearSpinner = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), MIN_YEAR, MAX_YEAR, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        generateButton = new JButton("Generate");

        JPanel selectorPanel = new JPanel(new FlowLayout());
        selectorPanel.add(new JLabel("Month:"));
        selectorPanel.add(monthCombo);
        selectorPanel.add(new JLabel("Year:"));
        selectorPanel.add(yearSpinner);
        selectorPanel.add(generateButton);

        add(selectorPanel, BorderLayout.NORTH);

        // Report tabs
        reportTabs = new JTabbedPane();
        reportTabs.addTab("Sales Report", buildSalesPanel());
        reportTabs.addTab("Inventory Report", buildInventoryPanel());

        add(reportTabs, BorderLayout.CENTER);
    }

    private JPanel buildSalesPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        ordersPlacedLabel = addSummaryRow(summaryPanel, "Orders Placed:");
        cancelledOrdersLabel = addSummaryRow(summaryPanel, "Cancelled Orders:");
        revenueLabel = addSummaryRow(summaryPanel, "Revenue (excluding cancelled):");
        averageOrderLabel = addSummaryRow(summaryPanel, "Average Order Value:");

        productSalesTableModel = createReadOnlyModel(new String[]{"Product ID", "Product", "Quantity Sold", "Revenue"});
        productSalesTable = createTable(productSalesTableModel);

        statusTableModel = createReadOnlyModel(new String[]{"Status", "Orders"});
        statusTable = createTable(statusTableModel);

        JScrollPane productSalesScroll = new JScrollPane(productSalesTable);
        productSalesScroll.setBorder(BorderFactory.createTitledBorder("Top-Selling Products"));

        JScrollPane statusScroll = new JScrollPane(statusTable);
        statusScroll.setBorder(BorderFactory.createTitledBorder("Orders by Status"));

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        tablesPanel.add(productSalesScroll);
        tablesPanel.add(statusScroll);

        JPanel salesPanel = new JPanel(new BorderLayout(10, 10));
        salesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        salesPanel.add(summaryPanel, BorderLayout.NORTH);
        salesPanel.add(tablesPanel, BorderLayout.CENTER);
        return salesPanel;
    }

    private JPanel buildInventoryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        productCountLabel = addSummaryRow(summaryPanel, "Products in Catalogue:");
        unitsOnHandLabel = addSummaryRow(summaryPanel, "Total Units on Hand:");
        stockValueLabel = addSummaryRow(summaryPanel, "Stock Value (at selling price):");
        lowStockCountLabel = addSummaryRow(summaryPanel, "Low-Stock Products:");

        lowStockTableModel = createReadOnlyModel(new String[]{"Product ID", "Product", "On Hand", "Reorder Level", "Alert"});
        lowStockTable = createTable(lowStockTableModel);

        JScrollPane lowStockScroll = new JScrollPane(lowStockTable);
        lowStockScroll.setBorder(BorderFactory.createTitledBorder("Low-Stock Alerts"));

        JPanel inventoryPanel = new JPanel(new BorderLayout(10, 10));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inventoryPanel.add(summaryPanel, BorderLayout.NORTH);
        inventoryPanel.add(lowStockScroll, BorderLayout.CENTER);
        return inventoryPanel;
    }

    private JLabel addSummaryRow(JPanel panel, String caption) {
        JLabel valueLabel = new JLabel("-");
        panel.add(new JLabel(caption));
        panel.add(valueLabel);
        return valueLabel;
    }

    private DefaultTableModel createReadOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {
        JTable newTable = new JTable(model);
        newTable.setRowHeight(30);
        newTable.setFillsViewportHeight(true);
        return newTable;
    }

    public void displaySalesSummary(int ordersPlaced, int cancelledOrders, double revenue, double averageOrder) {
        ordersPlacedLabel.setText(String.valueOf(ordersPlaced));
        cancelledOrdersLabel.setText(String.valueOf(cancelledOrders));
        revenueLabel.setText(String.format("Rs. %,.2f", revenue));
        averageOrderLabel.setText(String.format("Rs. %,.2f", averageOrder));
    }

    public void displayProductSales(ArrayList<ProductSales> productSales) {

        productSalesTableModel.setRowCount(0);

        for (ProductSales sales : productSales) {
            productSalesTableModel.addRow(new Object[]{
                    sales.getProductId(),
                    sales.getProductName(),
                    sales.getQuantitySold(),
                    String.format("%,.2f", sales.getRevenue())
            });
        }

    }

    public void displayStatusCounts(Map<String, Integer> statusCounts) {

        statusTableModel.setRowCount(0);

        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            statusTableModel.addRow(new Object[]{
                    entry.getKey(),
                    entry.getValue()
            });
        }

    }

    public void displayInventorySummary(int productCount, int unitsOnHand, double stockValue, int lowStockCount) {
        productCountLabel.setText(String.valueOf(productCount));
        unitsOnHandLabel.setText(String.valueOf(unitsOnHand));
        stockValueLabel.setText(String.format("Rs. %,.2f", stockValue));
        lowStockCountLabel.setText(String.valueOf(lowStockCount));
    }

    public void displayLowStock(ArrayList<LowStockAlert> alerts) {

        lowStockTableModel.setRowCount(0);

        for (LowStockAlert alert : alerts) {
            lowStockTableModel.addRow(new Object[]{
                    alert.getProductId(),
                    alert.getProductName(),
                    alert.getQuantityOnHand(),
                    alert.getReorderLevel(),
                    alert.getAlertText()
            });
        }

    }
}
