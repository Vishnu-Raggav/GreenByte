# Task 7 - Generate Monthly Sales & Inventory Reports (SA25610778)

## My files (Task 7)
- `Services/ReportService.java`      - builds the report text (sales summary, top products, stock value, low-stock alerts)
- `Views/ReportView.java`           - month/year selectors, Generate and Save buttons, report text area
- `Controllers/ReportController.java` - wires the view to the data; saves the report to a .txt file
- `Controllers/Validation.java`     - small dialog helper (also used by the other controllers; keep one copy)

## Shared classes (written by other members, included only so this compiles)
`Models/`: Product, ProductRepository, Client, DeliveryAgent, Order, OrderItem, OrderStatus, StockItem, StockMovement, StockRepository, OrderRepository, InMemoryRepository, Identifiable.
When merging into the main repo, do NOT overwrite the team's versions - the report only *reads* these.

## Try it alone
Run `ReportDemo.java` (sample data, not part of the final app).

## Join the main app
In `Views/MainFrame.java`:
```java
ReportView reportView = new ReportView();
ReportController reportController = new ReportController(reportView, orderRepository, productRepository, stockRepository);
addPage("Reports", "reports", reportView, reportController::refresh);
```
Revenue counts all orders except Cancelled. A product is "low stock" when quantity on hand <= its reorder level.
