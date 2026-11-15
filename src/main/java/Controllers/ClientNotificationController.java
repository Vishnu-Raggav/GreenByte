package Controllers;

import Models.*;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class ClientNotificationController {

    private static final String EMAIL_PATTERN = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+";

    public ClientRepository clientRepository;
    public EmailService emailService;

    public ClientNotificationController(ClientRepository clientRepository, EmailService emailService) {
        // Initialize
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    /**
     * Emails the client of the given order to say it has been dispatched.
     * Call this from the code that changes an order's status to "Dispatched".
     */
    public void notifyOrderDispatched(Order order) {
        if (order == null) {
            JOptionPane.showMessageDialog(null, "Please select an order!", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Client client = findClient(order.getClientId());

        if (client == null) {
            JOptionPane.showMessageDialog(null, "Client " + order.getClientId() + " doesn't exist, so no email was sent for order " + order.getId(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String email = client.getEmail() == null ? "" : client.getEmail().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, client.getName() + " has no email address", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches(EMAIL_PATTERN)) {
            JOptionPane.showMessageDialog(null, "Invalid email address for " + client.getName() + ": " + email, "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!emailService.isConfigured()) {
            JOptionPane.showMessageDialog(null, "Email is not configured. Set the ECONEST_MAIL_USER and ECONEST_MAIL_PASSWORD environment variables.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        sendInBackground(email, buildSubject(order), buildBody(order, client));
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
                    JOptionPane.showMessageDialog(null, "Dispatch email sent to " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (ExecutionException err) {
                    JOptionPane.showMessageDialog(null, "Could not send email: " + err.getCause().getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                } catch (InterruptedException err) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }

    private Client findClient(int clientId) {
        for (Client client : clientRepository.getAllClients()) {
            if (client.getId() == clientId) {
                return client;
            }
        }
        return null;
    }

    private String buildSubject(Order order) {
        return "Your EcoNest order #" + order.getId() + " has been dispatched";
    }

    private String buildBody(Order order, Client client) {
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(client.getName()).append(",\n\n");
        body.append("Good news! Your order #").append(order.getId())
                .append(" placed on ").append(order.getOrderDate())
                .append(" has been dispatched and is on its way to:\n")
                .append(client.getAddress()).append("\n\n");
        body.append("Order summary:\n");

        for (OrderItem item : order.getItems()) {
            body.append(String.format("  %s x %d  -  Rs. %,.2f%n", item.getProductName(), item.getQuantity(), item.getLineTotal()));
        }

        body.append(String.format("%nTotal: Rs. %,.2f%n%n", order.getTotal()));
        body.append("Thank you for choosing EcoNest.\n");
        body.append("EcoNest - sustainable living, delivered.\n");

        return body.toString();
    }
}
