import Controllers.ReportController;
import Models.*;
import Views.ReportView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Stand-alone launcher for Task 7 (Monthly Sales & Inventory Reports).
 * It fills the shared repositories with sample data so the report screen can be tried
 * without the other members' modules. In the full GreenByte app, MainFrame creates
 * ReportView + ReportController with the real repositories instead.
 */
public class ReportDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductRepository products = new ProductRepository();
            products.addProduct(new Product(1, "Bamboo Planter", "Planters", 1500.00, 5));
            products.addProduct(new Product(2, "Snake Plant", "Indoor Plants", 2500.00, 4));
            products.addProduct(new Product(3, "Coconut Coir Pot", "Planters", 850.00, 5));

            StockRepository stock = new StockRepository();
            stock.setStock(1, 25, 10);
            stock.setStock(2, 8, 10);   // low stock
            stock.setStock(3, 0, 15);   // out of stock
            stock.stockIn(products.getById(1), 30, "Green Supplies");

            OrderRepository orders = new OrderRepository();
            Client client = new Client(1, "Nimal Perera", Client.HOUSEHOLD, "nimal@example.com", "0771234567", "Colombo");
            List<OrderItem> a = new ArrayList<>();
            a.add(new OrderItem(1, "Bamboo Planter", 1500.00, 2));
            a.add(new OrderItem(2, "Snake Plant", 2500.00, 1));
            orders.add(new Order(orders.nextId(), client, LocalDate.now(), a));
            List<OrderItem> b = new ArrayList<>();
            b.add(new OrderItem(3, "Coconut Coir Pot", 850.00, 4));
            Order cancelled = new Order(orders.nextId(), client, LocalDate.now(), b);
            cancelled.setStatus(OrderStatus.CANCELLED);
            orders.add(cancelled);

            ReportView view = new ReportView();
            new ReportController(view, orders, products, stock);

            JFrame frame = new JFrame("EcoNest - Reports (Task 7 demo)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 650);
            frame.setLocationRelativeTo(null);
            frame.add(view);
            frame.setVisible(true);
        });
    }
}
