package Views;

import java.awt.CardLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import Controllers.*;
import Models.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("EcoNest");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel sidebar = new JPanel();
        JPanel contentPanel = new JPanel(cardLayout);

        // Sidebar
        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        JButton productsBtn = new JButton("Products");
        JButton stocksBtn = new JButton("Stocks");
        JButton clientsBtn = new JButton("Clients");
        JButton ordersBtn = new JButton("Orders");
        JButton reportsBtn = new JButton("Reports");
        JButton deliveriesBtn = new JButton("Deliveries");
        JButton deliveryAgentsBtn = new JButton("Deliverie Agents");

        sidebar.add(productsBtn);
        sidebar.add(stocksBtn);
        sidebar.add(clientsBtn);
        sidebar.add(ordersBtn);
        sidebar.add(reportsBtn);
        sidebar.add(deliveriesBtn);
        sidebar.add(deliveryAgentsBtn);

        addButtonActionListener(productsBtn, contentPanel, cardLayout, "products");
        addButtonActionListener(stocksBtn, contentPanel, cardLayout, "stocks");
        addButtonActionListener(clientsBtn, contentPanel, cardLayout, "clients");
        addButtonActionListener(ordersBtn, contentPanel, cardLayout, "orders");
        addButtonActionListener(reportsBtn, contentPanel, cardLayout, "reports");
        addButtonActionListener(deliveriesBtn, contentPanel, cardLayout, "deliveries");
        addButtonActionListener(deliveryAgentsBtn, contentPanel, cardLayout, "agents");

        // Panels as Cards
        ProductView productView = new ProductView();
        ProductRepository productRepository = new ProductRepository();
        ProductController productController = new ProductController(productView, productRepository);

        StockView stockView = new StockView();
        StockRepository stockRepository = new StockRepository();
        StockController stockController = new StockController(stockView, stockRepository, productRepository);

        ClientView clientView = new ClientView();
        ClientRepository clientRepository = new ClientRepository();
        ClientController clientController = new ClientController(clientView, clientRepository);

        EmailService emailService = new EmailService();
        ClientNotificationController clientNotificationController = new ClientNotificationController(clientRepository, emailService);

        OrderView orderView = new OrderView();
        OrderRepository orderRepository = new OrderRepository();
        OrderController orderController = new OrderController(orderView, orderRepository, productRepository);

        ReportView reportView = new ReportView();
        ReportController reportController = new ReportController(reportView, orderRepository, productRepository, stockRepository);

        AgentNotificationController agentNotificationController = new AgentNotificationController(orderRepository, clientRepository, emailService);

        DeliveryView deliveryView = new DeliveryView();
        DeliveryRepository deliveryRepository = new DeliveryRepository();
        DeliveryController deliveryController = new DeliveryController(deliveryView, deliveryRepository, orderRepository, clientNotificationController);

        DeliveryAgentView deliveryAgentView = new DeliveryAgentView();
        DeliveryAgentRepository deliveryAgentRepository = new DeliveryAgentRepository();
        DeliveryAgentController deliveryAgentController = new DeliveryAgentController(deliveryAgentView, deliveryAgentRepository);

        contentPanel.add(productView, "products");
        contentPanel.add(stockView, "stocks");
        contentPanel.add(clientView, "clients");
        contentPanel.add(orderView, "orders");
        contentPanel.add(reportView, "reports");
        contentPanel.add(deliveryView, "deliveries");
        contentPanel.add(deliveryAgentView, "agents");

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
        add(splitPane);

        setVisible(true);
    }

    private void addButtonActionListener(JButton btn, JPanel panel, CardLayout layout, String panelName) {
        btn.addActionListener(e -> {
            layout.show(panel, panelName);
        });
    }
}
