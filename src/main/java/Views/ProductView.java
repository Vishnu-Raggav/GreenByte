package Views;

import Models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ProductView extends JPanel {

    public JButton addButton;
    public JButton updateButton;
    public JButton removeButton;
    public JButton clearButton;

    public JTable table;
    public DefaultTableModel tableModel;

    public JPanel formPanel;
    public CardLayout formLayout;
    public AddProductPanel addProductPanel;
    public UpdateProductPanel updateProductPanel;

    public ProductView() {

        String[] columns = {
                "ID", "Name", "Category", "Price", "Eco Rating"
        };

        Product[] products = {
                new Product(1, "Bamboo Planter", "Planters", 1500.00, 5),
                new Product(2, "Snake Plant", "Indoor Plants", 2500.00, 4),
                new Product(3, "Coconut Coir Pot", "Planters", 850.00, 5),
                new Product(4, "Organic Soil Mix", "Gardening", 1200.00, 4),
                new Product(5, "Peace Lily", "Indoor Plants", 3200.00, 5)
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.NORTH);

        // Buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        removeButton = new JButton("Remove");
        clearButton = new JButton("Clear");

        FlowLayout flowLayout = new FlowLayout();
        JPanel buttonPanel = new JPanel(flowLayout);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Forms Container Panel
        formLayout = new CardLayout();
        formPanel = new JPanel(formLayout);

        // Form Panels
        addProductPanel = new AddProductPanel();
        updateProductPanel = new UpdateProductPanel();
        JPanel emptyPanel = new JPanel();

        formPanel.add(addProductPanel, "add");
        formPanel.add(updateProductPanel, "update");
        formPanel.add(emptyPanel, "empty");
        formLayout.show(formPanel, "empty");

        add(formPanel, BorderLayout.SOUTH);
    }

    public void displayProducts(ArrayList<Product> products) {

        tableModel.setRowCount(0);

        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getEcoRating()
            });
        }

    }

    public class AddProductPanel extends JPanel {

        public JTextField idTextField;
        public JTextField nameTextField;
        public JTextField categoryTextField;
        public JTextField priceTextField;
        public JTextField ratingTextField;
        public JButton submitButton;

        public AddProductPanel() {
            GridLayout gridLayout = new GridLayout(6, 2, 10, 10);
            setLayout(gridLayout);

            JLabel idLabel = new JLabel("Product ID:");
            idTextField = new JTextField();

            add(idLabel);
            add(idTextField);

            JLabel nameLabel = new JLabel("Product Name:");
            nameTextField = new JTextField();

            add(nameLabel);
            add(nameTextField);

            JLabel categoryLabel = new JLabel("Product Category:");
            categoryTextField = new JTextField();

            add(categoryLabel);
            add(categoryTextField);

            JLabel priceLabel = new JLabel("Product Price:");
            priceTextField = new JTextField();

            add(priceLabel);
            add(priceTextField);

            JLabel ratingLabel = new JLabel("Product Eco-Rating:");
            ratingTextField = new JTextField();

            add(ratingLabel);
            add(ratingTextField);

            submitButton = new JButton("Add Product");
            add(new JLabel(""));
            add(submitButton);
        }
    }

    public class UpdateProductPanel extends JPanel {

        public JTextField idTextField;
        public JTextField nameTextField;
        public JTextField categoryTextField;
        public JTextField priceTextField;
        public JTextField ratingTextField;
        public JButton submitButton;

        public UpdateProductPanel() {
            GridLayout gridLayout = new GridLayout(6, 2, 10, 10);
            setLayout(gridLayout);

            JLabel idLabel = new JLabel("Product ID:");
            idTextField = new JTextField();

            add(idLabel);
            add(idTextField);

            JLabel nameLabel = new JLabel("Product Name:");
            nameTextField = new JTextField();

            add(nameLabel);
            add(nameTextField);

            JLabel categoryLabel = new JLabel("Product Category:");
            categoryTextField = new JTextField();

            add(categoryLabel);
            add(categoryTextField);

            JLabel priceLabel = new JLabel("Product Price:");
            priceTextField = new JTextField();

            add(priceLabel);
            add(priceTextField);

            JLabel ratingLabel = new JLabel("Product Eco-Rating:");
            ratingTextField = new JTextField();

            add(ratingLabel);
            add(ratingTextField);

            submitButton = new JButton("Update Product");
            add(new JLabel(""));
            add(submitButton);
        }
    }
}
