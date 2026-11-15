package Services;

import Models.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** Builds the monthly sales and inventory report as plain text. */
public class ReportService {

    public String monthlyReport(YearMonth month, List<Order> orders, List<Product> products, StockRepository stock) {
        StringBuilder sb = new StringBuilder();
        String title = month.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        sb.append("ECONEST - MONTHLY SALES & INVENTORY REPORT\n");
        sb.append("Period: ").append(title).append("\n");
        sb.append("=".repeat(60)).append("\n\n");

        // ---- Sales ----
        List<Order> monthOrders = new ArrayList<>();
        for (Order o : orders) {
            if (YearMonth.from(o.getOrderDate()).equals(month)) {
                monthOrders.add(o);
            }
        }

        double revenue = 0;
        int valid = 0;
        int cancelled = 0;
        Map<OrderStatus, Integer> byStatus = new EnumMap<>(OrderStatus.class);
        Map<String, double[]> perProduct = new HashMap<>(); // name -> {qty, revenue}

        for (Order o : monthOrders) {
            byStatus.merge(o.getStatus(), 1, Integer::sum);
            if (o.getStatus() == OrderStatus.CANCELLED) {
                cancelled++;
                continue;
            }
            valid++;
            revenue += o.getTotal();
            for (OrderItem item : o.getItems()) {
                double[] t = perProduct.computeIfAbsent(item.getProductName(), k -> new double[2]);
                t[0] += item.getQuantity();
                t[1] += item.getSubtotal();
            }
        }

        sb.append("SALES SUMMARY\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append(String.format("Orders placed          : %d%n", monthOrders.size()));
        sb.append(String.format("Cancelled orders       : %d%n", cancelled));
        sb.append(String.format("Revenue (excl. cancelled): Rs. %,.2f%n", revenue));
        sb.append(String.format("Average order value    : Rs. %,.2f%n", valid == 0 ? 0 : revenue / valid));
        sb.append("\nOrders by status:\n");
        for (OrderStatus s : OrderStatus.values()) {
            sb.append(String.format("  %-16s %d%n", s, byStatus.getOrDefault(s, 0)));
        }

        sb.append("\nTop-selling products:\n");
        if (perProduct.isEmpty()) {
            sb.append("  (no sales this month)\n");
        } else {
            List<Map.Entry<String, double[]>> ranked = new ArrayList<>(perProduct.entrySet());
            ranked.sort((a, b) -> Double.compare(b.getValue()[1], a.getValue()[1]));
            sb.append(String.format("  %-28s %6s %16s%n", "Product", "Qty", "Revenue"));
            for (Map.Entry<String, double[]> e : ranked) {
                sb.append(String.format("  %-28s %6d %,16.2f%n", e.getKey(), (int) e.getValue()[0], e.getValue()[1]));
            }
        }

        // ---- Inventory ----
        int totalUnits = 0;
        double stockValue = 0;
        List<Product> low = new ArrayList<>();
        for (Product p : products) {
            StockItem item = stock.getOrCreate(p.getId());
            totalUnits += item.getQuantityOnHand();
            stockValue += item.getQuantityOnHand() * p.getPrice();
            if (item.isLow()) {
                low.add(p);
            }
        }

        int received = 0;
        for (StockMovement m : stock.getMovements()) {
            if (YearMonth.from(m.getDate()).equals(month)) {
                received += m.getQuantity();
            }
        }

        sb.append("\nINVENTORY SUMMARY (current position)\n");
        sb.append("-".repeat(60)).append("\n");
        sb.append(String.format("Products in catalogue  : %d%n", products.size()));
        sb.append(String.format("Total units on hand    : %d%n", totalUnits));
        sb.append(String.format("Stock value (at price) : Rs. %,.2f%n", stockValue));
        sb.append(String.format("Units received (stock-in) this month: %d%n", received));

        sb.append("\nLOW-STOCK ALERTS\n");
        if (low.isEmpty()) {
            sb.append("  All products are above their reorder level.\n");
        } else {
            sb.append(String.format("  %-28s %8s %9s%n", "Product", "On hand", "Reorder"));
            for (Product p : low) {
                StockItem item = stock.getOrCreate(p.getId());
                sb.append(String.format("  %-28s %8d %9d%s%n", p.getName(), item.getQuantityOnHand(),
                        item.getReorderLevel(), item.getQuantityOnHand() == 0 ? "  OUT OF STOCK" : ""));
            }
        }
        return sb.toString();
    }
}
