package Views;

import Models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StockView extends JPanel {

    public JTable table;
    public DefaultTableModel tableModel;

    public StockChangePanel stockChangePanel;

    public StockView() {
        String[] columns = {"Product ID", "Quantity On Hand", "Reorder Level", "Status"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.NORTH);

        // Form
        stockChangePanel = new StockChangePanel();
        add(stockChangePanel, BorderLayout.CENTER);
    }

    public void displayStocks(ArrayList<StockItem> stockItems) {

        tableModel.setRowCount(0);

        for (StockItem stockItem : stockItems) {
            tableModel.addRow(new Object[]{
                    stockItem.getProductId(),
                    stockItem.getQuantityOnHand(),
                    stockItem.getReorderLevel(),
                    stockItem.isLowStock() ? "LOW" : "OK"
            });
        }

    }

    public class StockChangePanel extends JPanel {

        public JTextField productIdTextField;
        public JTextField quantityTextField;
        public JButton addStockButton;
        public JButton reduceStockButton;

        public StockChangePanel() {

            GridLayout gridLayout = new GridLayout(4, 2, 10, 10);
            setLayout(gridLayout);

            JLabel productIdLabel = new JLabel("Product ID:");
            productIdTextField = new JTextField();

            add(productIdLabel);
            add(productIdTextField);

            JLabel quantityLabel = new JLabel("Quantity Change:");
            quantityTextField = new JTextField();

            add(quantityLabel);
            add(quantityTextField);

            addStockButton = new JButton("Add Stock");
            add(new JLabel(""));
            add(addStockButton);

            reduceStockButton = new JButton("Reduce Stock");
            add(new JLabel(""));
            add(reduceStockButton);
        }
    }
}