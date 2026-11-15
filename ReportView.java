package Views;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class ReportView extends JPanel {

    public JComboBox<String> monthCombo = new JComboBox<>();
    public JSpinner yearSpinner;
    public JButton generateButton = new JButton("Generate Report");
    public JButton saveButton = new JButton("Save as Text File");
    public JTextArea reportArea = new JTextArea();

    public ReportView() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Month m : Month.values()) {
            monthCombo.addItem(m.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        int year = LocalDate.now().getYear();
        yearSpinner = new JSpinner(new SpinnerNumberModel(year, 2000, 2100, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        top.add(new JLabel("Month:"));
        top.add(monthCombo);
        top.add(new JLabel("Year:"));
        top.add(yearSpinner);
        top.add(generateButton);
        top.add(saveButton);
        add(top, BorderLayout.NORTH);

        reportArea.setEditable(false);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }
}
