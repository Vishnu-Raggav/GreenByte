package Views;

import Models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderView extends JPanel {

    public JTabbedPane tabbedPane;

    // New Order tab
    // TODO: replace the client text fields with a JComboBox filled from ClientRepository once the Clients module exists.
    public JTextField clientIdTextField;
    public JTextField clientNameTextField;
    public JComboBox<Product> productComboBox;
    public JTextField quantityTextField;
    public JButton addItemButton;
    public JButton removeItemButton;
    public JButton clearOrderButton;
    public JButton placeOrderButton;
    public JTable linesTable;
    public DefaultTableModel linesTableModel;
    public JLabel totalLabel;

    // Order History tab
    public JTable ordersTable;
    public DefaultTableModel ordersTableModel;
    public JTable orderItemsTable;
    public DefaultTableModel orderItemsTableModel;
    public JButton removeOrderButton;

    public OrderView() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("New Order", buildNewOrderTab());
        tabbedPane.addTab("Order History", buildHistoryTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel buildNewOrderTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Order form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Client ID:"));
        clientIdTextField = new JTextField();
        formPanel.add(clientIdTextField);

        formPanel.add(new JLabel("Client Name:"));
        clientNameTextField = new JTextField();
        formPanel.add(clientNameTextField);

        formPanel.add(new JLabel("Product:"));
        productComboBox = new JComboBox<>();
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product product) {
                    setText(String.format("%s - Rs. %.2f", product.getName(), product.getPrice()));
                }
                return this;
            }
        });
        formPanel.add(productComboBox);

        formPanel.add(new JLabel("Quantity:"));
        quantityTextField = new JTextField();
        formPanel.add(quantityTextField);

        addItemButton = new JButton("Add Item");
        formPanel.add(new JLabel(""));
        formPanel.add(addItemButton);

        tab.add(formPanel, BorderLayout.NORTH);

        // Order lines
        String[] columns = {"Product ID", "Product", "Unit Price", "Quantity", "Line Total"};
        linesTableModel = createReadOnlyModel(columns);
        linesTable = new JTable(linesTableModel);
        linesTable.setRowHeight(30);
        linesTable.setFillsViewportHeight(true);

        tab.add(new JScrollPane(linesTable), BorderLayout.CENTER);

        // Total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: Rs. 0.00");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        removeItemButton = new JButton("Remove Item");
        clearOrderButton = new JButton("Clear");
        placeOrderButton = new JButton("Place Order");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(removeItemButton);
        buttonPanel.add(clearOrderButton);
        buttonPanel.add(placeOrderButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        tab.add(bottomPanel, BorderLayout.SOUTH);

        return tab;
    }

    private JPanel buildHistoryTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] orderColumns = {"Order ID", "Client ID", "Client", "Date", "Items", "Total", "Status"};
        ordersTableModel = createReadOnlyModel(orderColumns);
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setRowHeight(30);
        ordersTable.setFillsViewportHeight(true);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] itemColumns = {"Product ID", "Product", "Unit Price", "Quantity", "Line Total"};
        orderItemsTableModel = createReadOnlyModel(itemColumns);
        orderItemsTable = new JTable(orderItemsTableModel);
        orderItemsTable.setRowHeight(30);
        orderItemsTable.setFillsViewportHeight(true);

        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        ordersScrollPane.setBorder(BorderFactory.createTitledBorder("Orders"));
        JScrollPane itemsScrollPane = new JScrollPane(orderItemsTable);
        itemsScrollPane.setBorder(BorderFactory.createTitledBorder("Items in selected order"));

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.add(ordersScrollPane);
        tablesPanel.add(itemsScrollPane);
        tab.add(tablesPanel, BorderLayout.CENTER);

        removeOrderButton = new JButton("Remove Order");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(removeOrderButton);
        tab.add(buttonPanel, BorderLayout.SOUTH);

        return tab;
    }

    private DefaultTableModel createReadOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void displayProductChoices(ArrayList<Product> products) {
        productComboBox.removeAllItems();

        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    public void displayOrderLines(ArrayList<OrderItem> items) {
        fillItemRows(linesTableModel, items);
    }

    public void displayOrderItems(ArrayList<OrderItem> items) {
        fillItemRows(orderItemsTableModel, items);
    }

    public void displayTotal(double total) {
        totalLabel.setText(String.format("Total: Rs. %.2f", total));
    }

    public void displayOrders(ArrayList<Order> orders) {

        ordersTableModel.setRowCount(0);

        for (Order order : orders) {
            ordersTableModel.addRow(new Object[]{
                    order.getId(),
                    order.getClientId(),
                    order.getClientName(),
                    order.getOrderDate(),
                    order.getItems().size(),
                    order.getTotal(),
                    order.getStatus()
            });
        }

    }

    private void fillItemRows(DefaultTableModel model, ArrayList<OrderItem> items) {

        model.setRowCount(0);

        for (OrderItem item : items) {
            model.addRow(new Object[]{
                    item.getProductId(),
                    item.getProductName(),
                    item.getUnitPrice(),
                    item.getQuantity(),
                    item.getLineTotal()
            });
        }

    }
}
