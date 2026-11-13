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
        sidebar.add(productsBtn);
        addButtonActionListener(productsBtn, contentPanel, cardLayout, "products");

        // Panels as Cards
        ProductView productView = new ProductView();
        ProductRepository productRepository = new ProductRepository();
        ProductController productController = new ProductController(productView, productRepository);

        contentPanel.add(productView, "products");

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
