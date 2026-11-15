package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderController {

    private static final int MAX_QUANTITY = 1000;

    public OrderView view;
    public OrderRepository repository;
    public ProductRepository productRepository;

    private ArrayList<OrderItem> currentItems = new ArrayList<>();

    public OrderController(OrderView orderView, OrderRepository orderRepository, ProductRepository productRepository) {
        // Initialize
        view = orderView;
        repository = orderRepository;
        this.productRepository = productRepository;

        // Add Data (product ids match the sample products created by ProductController)
        // TODO: use real client ids and names once ClientController seeds its sample clients.
        seedOrder(1, 1, "Nimal Perera", LocalDate.of(2026, 11, 3), "Delivered", new int[][]{{1, 2}, {4, 1}});
        seedOrder(2, 2, "Green Offices Ltd", LocalDate.of(2026, 11, 6), "Dispatched", new int[][]{{2, 5}, {5, 5}});
        seedOrder(3, 3, "Kumari Silva", LocalDate.of(2026, 11, 10), Order.STATUS_PLACED, new int[][]{{3, 4}});
        seedOrder(4, 1, "Nimal Perera", LocalDate.of(2026, 11, 12), Order.STATUS_PLACED, new int[][]{{5, 1}, {1, 1}, {4, 3}});

        // Show Data
        view.displayProductChoices(productRepository.getAllProducts());
        view.displayOrders(repository.getAllOrders());

        // Product list may have changed in the Products module, so refresh it whenever this screen is shown
        view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                view.displayProductChoices(productRepository.getAllProducts());
            }
        });

        view.addItemButton.addActionListener(e -> {
            Product product = (Product) view.productComboBox.getSelectedItem();
            String quantity = view.quantityTextField.getText().trim();

            if (product == null) {
                JOptionPane.showMessageDialog(null, "Please select a product!", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (quantity.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantityInt;

            try {
                quantityInt = Integer.parseInt(quantity);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Number required for quantity", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (quantityInt < 1 || quantityInt > MAX_QUANTITY) {
                JOptionPane.showMessageDialog(null, "Quantity must be between 1 and " + MAX_QUANTITY, "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // TODO: check the requested quantity against StockRepository once the Stock module exists.
            addOrIncreaseItem(product, quantityInt);
        });

        view.removeItemButton.addActionListener(e -> {
            int selectedRow = view.linesTable.getSelectedRow();

            if (selectedRow != -1) {
                int productId = (int) view.linesTableModel.getValueAt(selectedRow, 0);
                currentItems.removeIf(item -> item.getProductId() == productId);
                refreshOrderLines();
            } else {
                JOptionPane.showMessageDialog(null, "Please select an item!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.clearOrderButton.addActionListener(e -> {
            resetNewOrderForm();
        });

        view.placeOrderButton.addActionListener(e -> {
            String clientId = view.clientIdTextField.getText().trim();
            String clientName = view.clientNameTextField.getText().trim();

            if (clientId.isEmpty() || clientName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int clientIdInt;

            try {
                clientIdInt = Integer.parseInt(clientId);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Number required for client id", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (clientIdInt < 1) {
                JOptionPane.showMessageDialog(null, "Client id must be greater than 0", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (currentItems.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please add at least one item to the order!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // TODO: reduce the stock of each ordered product through StockRepository once the Stock module exists.
            Order newOrder = new Order(repository.getNextId(), clientIdInt, clientName, LocalDate.now(), Order.STATUS_PLACED, currentItems);
            repository.addOrder(newOrder);
            view.displayOrders(repository.getAllOrders());
            JOptionPane.showMessageDialog(null,
                    String.format("Order %d placed successfully. Total: Rs. %.2f", newOrder.getId(), newOrder.getTotal()),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            resetNewOrderForm();
        });

        view.ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedOrderItems();
            }
        });

        view.removeOrderButton.addActionListener(e -> {
            int selectedRow = view.ordersTable.getSelectedRow();

            if (selectedRow != -1) {
                int orderId = (int) view.ordersTableModel.getValueAt(selectedRow, 0);
                int choice = JOptionPane.showConfirmDialog(null, "Remove order " + orderId + "?", "Confirm", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    repository.removeOrder(orderId);
                    view.displayOrders(repository.getAllOrders());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an order!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void seedOrder(int id, int clientId, String clientName, LocalDate date, String status, int[][] lines) {
        ArrayList<OrderItem> items = new ArrayList<>();

        for (int[] line : lines) {
            Product product = findProduct(line[0]);
            if (product != null) {
                items.add(new OrderItem(product.getId(), product.getName(), product.getPrice(), line[1]));
            }
        }

        if (!items.isEmpty()) {
            repository.addOrder(new Order(id, clientId, clientName, date, status, items));
        }
    }

    private Product findProduct(int id) {
        for (Product product : productRepository.getAllProducts()) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private void addOrIncreaseItem(Product product, int quantity) {
        for (OrderItem item : currentItems) {
            if (item.getProductId() == product.getId()) {
                int newQuantity = item.getQuantity() + quantity;

                if (newQuantity > MAX_QUANTITY) {
                    JOptionPane.showMessageDialog(null, "Quantity must be between 1 and " + MAX_QUANTITY, "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                item.setQuantity(newQuantity);
                refreshOrderLines();
                view.quantityTextField.setText("");
                return;
            }
        }

        currentItems.add(new OrderItem(product.getId(), product.getName(), product.getPrice(), quantity));
        refreshOrderLines();
        view.quantityTextField.setText("");
    }

    private void refreshOrderLines() {
        view.displayOrderLines(currentItems);
        view.displayTotal(calculateTotal(currentItems));
    }

    private double calculateTotal(ArrayList<OrderItem> items) {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getLineTotal();
        }
        return total;
    }

    private void resetNewOrderForm() {
        currentItems = new ArrayList<>();
        refreshOrderLines();
        clearTextFields(new JTextField[] {
                view.clientIdTextField,
                view.clientNameTextField,
                view.quantityTextField
        });
    }

    private void showSelectedOrderItems() {
        int selectedRow = view.ordersTable.getSelectedRow();

        if (selectedRow == -1) {
            view.displayOrderItems(new ArrayList<>());
            return;
        }

        int orderId = (int) view.ordersTableModel.getValueAt(selectedRow, 0);

        for (Order order : repository.getAllOrders()) {
            if (order.getId() == orderId) {
                view.displayOrderItems(order.getItems());
                return;
            }
        }
    }

    private void clearTextFields(JTextField[] textFields) {
        for(JTextField field : textFields) {
            field.setText("");
        }
    }
}
