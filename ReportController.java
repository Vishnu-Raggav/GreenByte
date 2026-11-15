package Controllers;

import Models.*;
import Services.ReportService;
import Views.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.YearMonth;

public class ReportController {

    private final ReportView view;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ReportService reportService = new ReportService();

    public ReportController(ReportView view, OrderRepository orderRepository,
                            ProductRepository productRepository, StockRepository stockRepository) {
        this.view = view;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;

        view.generateButton.addActionListener(e -> generate());
        view.saveButton.addActionListener(e -> save());
        generate();
    }

    public void refresh() {
        generate();
    }

    private void generate() {
        YearMonth month = YearMonth.of((int) view.yearSpinner.getValue(), view.monthCombo.getSelectedIndex() + 1);
        view.reportArea.setText(reportService.monthlyReport(month, orderRepository.getAll(),
                productRepository.getAllProducts(), stockRepository));
        view.reportArea.setCaretPosition(0);
    }

    private void save() {
        generate();
        JFileChooser chooser = new JFileChooser();
        int year = (int) view.yearSpinner.getValue();
        chooser.setSelectedFile(new File(String.format("EcoNest-report-%d-%02d.txt", year,
                view.monthCombo.getSelectedIndex() + 1)));
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(chooser.getSelectedFile().toPath(), view.reportArea.getText(), StandardCharsets.UTF_8);
                Validation.info("Report saved to " + chooser.getSelectedFile().getAbsolutePath());
            } catch (IOException ex) {
                Validation.error("Could not save the report: " + ex.getMessage());
            }
        }
    }
}
