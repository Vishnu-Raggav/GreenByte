package Views;

import Models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DeliveryView extends JPanel {

    public JTable ordersTable;
    public DefaultTableModel ordersTableModel;
    public JTable deliveriesTable;
    public DefaultTableModel deliveriesTableModel;

    public JTextField agentIdTextField;
    public JTextField agentNameTextField;
    public JTextField scheduledDateTextField;
    public JButton assignButton;

    public JComboBox<String> statusComboBox;
    public JButton updateStatusButton;
    public JButton removeButton;

    public DeliveryView() {
        setLayout(new BorderLayout(0, 10));

        // Tables
        ordersTableModel = createReadOnlyModel(new String[]{"Order ID", "Client", "Date", "Total", "Order Status"});
        ordersTable = createTable(ordersTableModel);

        deliveriesTableModel = createReadOnlyModel(new String[]{"Delivery ID", "Order ID", "Agent ID", "Agent", "Scheduled Date", "Status"});
        deliveriesTable = createTable(deliveriesTableModel);

        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        ordersScrollPane.setBorder(BorderFactory.createTitledBorder("Orders"));

        JScrollPane deliveriesScrollPane = new JScrollPane(deliveriesTable);
        deliveriesScrollPane.setBorder(BorderFactory.createTitledBorder("Deliveries"));

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.add(ordersScrollPane);
        tablesPanel.add(deliveriesScrollPane);

        add(tablesPanel, BorderLayout.CENTER);

        // Assign form
        agentIdTextField = new JTextField();
        agentNameTextField = new JTextField();
        scheduledDateTextField = new JTextField();
        assignButton = new JButton("Assign Agent");

        JPanel assignPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign an agent to the selected order"));

        assignPanel.add(new JLabel("Agent ID:"));
        assignPanel.add(agentIdTextField);

        assignPanel.add(new JLabel("Agent Name:"));
        assignPanel.add(agentNameTextField);

        assignPanel.add(new JLabel("Delivery Date (yyyy-MM-dd):"));
        assignPanel.add(scheduledDateTextField);

        assignPanel.add(new JLabel(""));
        assignPanel.add(assignButton);

        // Status controls
        statusComboBox = new JComboBox<>(Delivery.STATUSES);
        updateStatusButton = new JButton("Update Status");
        removeButton = new JButton("Remove Assignment");

        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Selected delivery"));
        statusPanel.add(new JLabel("New Status:"));
        statusPanel.add(statusComboBox);
        statusPanel.add(updateStatusButton);
        statusPanel.add(removeButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.add(assignPanel, BorderLayout.NORTH);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
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
        newTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return newTable;
    }

    public void displayOrders(ArrayList<Order> orders) {

        ordersTableModel.setRowCount(0);

        for (Order order : orders) {
            ordersTableModel.addRow(new Object[]{
                    order.getId(),
                    order.getClientName(),
                    order.getOrderDate(),
                    String.format("%,.2f", order.getTotal()),
                    order.getStatus()
            });
        }

    }

    public void displayDeliveries(ArrayList<Delivery> deliveries) {

        deliveriesTableModel.setRowCount(0);

        for (Delivery delivery : deliveries) {
            deliveriesTableModel.addRow(new Object[]{
                    delivery.getId(),
                    delivery.getOrderId(),
                    delivery.getAgentId(),
                    delivery.getAgentName(),
                    delivery.getScheduledDate(),
                    delivery.getStatus()
            });
        }

    }
}
