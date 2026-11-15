package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DeliveryController {

    public DeliveryView view;
    public DeliveryRepository repository;
    public OrderRepository orderRepository;
    public ClientNotificationController clientNotificationController;

    public DeliveryController(DeliveryView deliveryView, DeliveryRepository deliveryRepository,
                              OrderRepository orderRepository, ClientNotificationController clientNotificationController) {
        // Initialize
        view = deliveryView;
        repository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.clientNotificationController = clientNotificationController;

        // Add Data (order ids match the sample orders created by OrderController)
        // TODO: use real agent ids and names once the Delivery Agents module exists.
        repository.addDelivery(new Delivery(1, 1, 1, "Kamal Silva", LocalDate.of(2026, 11, 4), Delivery.STATUS_DELIVERED));
        repository.addDelivery(new Delivery(2, 2, 2, "Nuwan Fernando", LocalDate.of(2026, 11, 7), Delivery.STATUS_DISPATCHED));

        // Show Data
        showData();

        // Orders may have changed in the Orders module, so refresh both tables whenever this screen is shown
        view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                showData();
            }
        });

        view.assignButton.addActionListener(e -> {
            assignAgent();
        });

        view.updateStatusButton.addActionListener(e -> {
            updateStatus();
        });

        view.removeButton.addActionListener(e -> {
            removeAssignment();
        });
    }

    private void assignAgent() {
        int selectedRow = view.ordersTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an order!", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = (int) view.ordersTableModel.getValueAt(selectedRow, 0);

        String agentId = view.agentIdTextField.getText().trim();
        String agentName = view.agentNameTextField.getText().trim();
        String scheduledDate = view.scheduledDateTextField.getText().trim();

        if (agentId.isEmpty() || agentName.isEmpty() || scheduledDate.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int agentIdInt;
        LocalDate scheduledDateValue;

        try {
            agentIdInt = Integer.parseInt(agentId);
        } catch (NumberFormatException err) {
            JOptionPane.showMessageDialog(null, "Number required for agent id", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            scheduledDateValue = LocalDate.parse(scheduledDate);
        } catch (DateTimeParseException err) {
            JOptionPane.showMessageDialog(null, "Delivery date must be a real date in the format yyyy-MM-dd", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Order order = findOrder(orderId);

        if (order == null) {
            JOptionPane.showMessageDialog(null, "Order doesn't exist", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Delivery delivery : repository.getAllDeliveries()) {
            if (delivery.getOrderId() == orderId) {
                JOptionPane.showMessageDialog(null, "Order already has a delivery assigned", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (agentIdInt < 1) {
            JOptionPane.showMessageDialog(null, "Agent id must be greater than 0", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!Order.STATUS_PLACED.equals(order.getStatus())) {
            JOptionPane.showMessageDialog(null, "Only orders with status " + Order.STATUS_PLACED + " can be assigned", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (scheduledDateValue.isBefore(order.getOrderDate())) {
            JOptionPane.showMessageDialog(null, "Delivery date cannot be before the order date (" + order.getOrderDate() + ")", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: email the agent about the new delivery once the agent notification module exists.
        Delivery newDelivery = new Delivery(repository.getNextId(), orderId, agentIdInt, agentName, scheduledDateValue, Delivery.STATUS_SCHEDULED);
        repository.addDelivery(newDelivery);
        showData();
        JOptionPane.showMessageDialog(null, "Agent Assigned Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearTextFields(new JTextField[]{
                view.agentIdTextField,
                view.agentNameTextField,
                view.scheduledDateTextField
        });
    }

    private void updateStatus() {
        Delivery delivery = getSelectedDelivery();

        if (delivery == null) {
            return;
        }

        String newStatus = (String) view.statusComboBox.getSelectedItem();
        int currentIndex = indexOfStatus(delivery.getStatus());
        int newIndex = indexOfStatus(newStatus);

        if (newIndex == currentIndex) {
            JOptionPane.showMessageDialog(null, "Delivery is already " + delivery.getStatus(), "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (newIndex != currentIndex + 1) {
            JOptionPane.showMessageDialog(null, "Status can only move forward one step at a time (Scheduled, Dispatched, Delivered)", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        delivery.setStatus(newStatus);
        repository.updateDelivery(delivery);

        // The order carries the same status text as the delivery
        Order order = findOrder(delivery.getOrderId());

        if (order != null) {
            order.setStatus(newStatus);
            orderRepository.updateOrder(order);
        }

        showData();
        JOptionPane.showMessageDialog(null, "Delivery Status Updated to " + newStatus, "Success", JOptionPane.INFORMATION_MESSAGE);

        if (order != null && Delivery.STATUS_DISPATCHED.equals(newStatus)) {
            clientNotificationController.notifyOrderDispatched(order);
        }
    }

    private void removeAssignment() {
        Delivery delivery = getSelectedDelivery();

        if (delivery == null) {
            return;
        }

        if (!Delivery.STATUS_SCHEDULED.equals(delivery.getStatus())) {
            JOptionPane.showMessageDialog(null, "Only scheduled deliveries can be removed", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        repository.removeDelivery(delivery.getId());
        showData();
    }

    /** Returns the delivery selected in the table, or null after telling the user to select one. */
    private Delivery getSelectedDelivery() {
        int selectedRow = view.deliveriesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a delivery!", "Information", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        int deliveryId = (int) view.deliveriesTableModel.getValueAt(selectedRow, 0);

        for (Delivery delivery : repository.getAllDeliveries()) {
            if (delivery.getId() == deliveryId) {
                return delivery;
            }
        }

        return null;
    }

    private Order findOrder(int orderId) {
        for (Order order : orderRepository.getAllOrders()) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }

    private int indexOfStatus(String status) {
        for (int i = 0; i < Delivery.STATUSES.length; i++) {
            if (Delivery.STATUSES[i].equals(status)) {
                return i;
            }
        }
        return -1;
    }

    private void showData() {
        view.displayOrders(orderRepository.getAllOrders());
        view.displayDeliveries(repository.getAllDeliveries());
    }

    private void clearTextFields(JTextField[] textFields) {
        for (JTextField field : textFields) {
            field.setText("");
        }
    }
}
