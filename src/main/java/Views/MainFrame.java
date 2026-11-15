package Views;

import Models.*;
import Controllers.*;

import javax.swing.*;
import java.awt.*;

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

        sidebar.add(productsBtn);
        sidebar.add(stocksBtn);
        sidebar.add(clientsBtn);

        addButtonActionListener(productsBtn, contentPanel, cardLayout, "products");
        addButtonActionListener(stocksBtn, contentPanel, cardLayout, "stocks");
        addButtonActionListener(clientsBtn, contentPanel, cardLayout, "clients");

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

        contentPanel.add(productView, "products");
        contentPanel.add(stockView, "stocks");
        contentPanel.add(clientView, "clients");

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
