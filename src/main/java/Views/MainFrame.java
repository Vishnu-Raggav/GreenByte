package Views;

import java.awt.CardLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import Controllers.ProductController;
import Controllers.StockController;
import Models.ProductRepository;
import Models.StockRepository;

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

        sidebar.add(productsBtn);
        sidebar.add(stocksBtn);
        addButtonActionListener(productsBtn, contentPanel, cardLayout, "products");
        addButtonActionListener(stocksBtn, contentPanel, cardLayout, "stocks");
        JButton ordersBtn = new JButton("Orders");
sidebar.add(ordersBtn);
addButtonActionListener(ordersBtn, contentPanel, cardLayout, "orders");

        // Panels as Cards
        ProductView productView = new ProductView();
        ProductRepository productRepository = new ProductRepository();
        ProductController productController = new ProductController(productView, productRepository);

        StockView stockView = new StockView();
        StockRepository stockRepository = new StockRepository();
        StockController stockController = new StockController(stockView, stockRepository, productRepository);

        contentPanel.add(productView, "products");
        contentPanel.add(stockView, "stocks");
        OrderView orderView = new OrderView();
        OrderRepository orderRepository = new OrderRepository();
OrderController orderController = new OrderController(orderView, orderRepository, productRepository);
contentPanel.add(orderView, "orders");

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
