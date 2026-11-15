package Controllers;

import Models.*;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class AgentNotificationController {

    private static final String EMAIL_PATTERN = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+";

    public OrderRepository orderRepository;
    public ClientRepository clientRepository;
    public EmailService emailService;

    public AgentNotificationController(OrderRepository orderRepository, ClientRepository clientRepository, EmailService emailService) {
        // Initialize
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    /**
     * Emails the delivery agent about a newly assigned delivery.
     * Call this from the code that creates the delivery, passing the agent's email address.
     */
    public void notifyDeliveryAssigned(Delivery delivery, String agentEmail) {
        if (delivery == null) {
            JOptionPane.showMessageDialog(null, "Please select a delivery!", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String email = agentEmail == null ? "" : agentEmail.trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, delivery.getAgentName() + " has no email address", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches(EMAIL_PATTERN)) {
            JOptionPane.showMessageDialog(null, "Invalid email address for " + delivery.getAgentName() + ": " + email, "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Order order = findOrder(delivery.getOrderId());

        if (order == null) {
            JOptionPane.showMessageDialog(null, "Order " + delivery.getOrderId() + " doesn't exist, so no email was sent", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Client client = findClient(order.getClientId());

        if (client == null) {
            JOptionPane.showMessageDialog(null, "Client " + order.getClientId() + " doesn't exist, so no email was sent", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!emailService.isConfigured()) {
            JOptionPane.showMessageDialog(null, "Email is not configured. Set the ECONEST_MAIL_USER and ECONEST_MAIL_PASSWORD environment variables.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        sendInBackground(email, buildSubject(delivery), buildBody(delivery, order, client));
    }

    private void sendInBackground(String email, String subject, String body) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                emailService.sendEmail(email, subject, body);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(null, "Assignment email sent to " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (ExecutionException err) {
                    JOptionPane.showMessageDialog(null, "Could not send email: " + err.getCause().getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                } catch (InterruptedException err) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }

    private Order findOrder(int orderId) {
        for (Order order : orderRepository.getAllOrders()) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }

    private Client findClient(int clientId) {
        for (Client client : clientRepository.getAllClients()) {
            if (client.getId() == clientId) {
                return client;
            }
        }
        return null;
    }

    private String buildSubject(Delivery delivery) {
        return "New EcoNest delivery: Order #" + delivery.getOrderId() + " on " + delivery.getScheduledDate();
    }

    private String buildBody(Delivery delivery, Order order, Client client) {
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(delivery.getAgentName()).append(",\n\n");
        body.append("A new delivery has been assigned to you.\n\n");
        body.append("Delivery ID    : ").append(delivery.getId()).append("\n");
        body.append("Order ID       : ").append(order.getId()).append("\n");
        body.append("Delivery date  : ").append(delivery.getScheduledDate()).append("\n\n");
        body.append("Deliver to:\n");
        body.append(client.getName()).append("\n");
        body.append(client.getAddress()).append("\n");
        body.append("Phone: ").append(client.getPhone()).append("\n\n");
        body.append("Items to deliver:\n");

        for (OrderItem item : order.getItems()) {
            body.append(String.format("  %s x %d%n", item.getProductName(), item.getQuantity()));
        }

        body.append(String.format("%nOrder value: Rs. %,.2f%n%n", order.getTotal()));
        body.append("Please contact the client if you have any trouble finding the address.\n");
        body.append("EcoNest - sustainable living, delivered.\n");

        return body.toString();
    }
}
