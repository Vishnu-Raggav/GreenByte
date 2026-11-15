package Views;

import Models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DeliveryAgentView extends JPanel {

    public JButton addButton;
    public JButton updateButton;
    public JButton removeButton;
    public JButton clearButton;

    public JTable table;
    public DefaultTableModel tableModel;

    public JPanel formPanel;
    public CardLayout formLayout;
    public AddDeliveryAgentPanel addDeliveryAgentPanel;
    public UpdateDeliveryAgentPanel updateDeliveryAgentPanel;

    public DeliveryAgentView() {

        String[] columns = {
                "ID", "Name", "Phone", "Email", "Vehicle Type", "Vehicle Number"
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
        addDeliveryAgentPanel = new AddDeliveryAgentPanel();
        updateDeliveryAgentPanel = new UpdateDeliveryAgentPanel();
        JPanel emptyPanel = new JPanel();

        formPanel.add(addDeliveryAgentPanel, "add");
        formPanel.add(updateDeliveryAgentPanel, "update");
        formPanel.add(emptyPanel, "empty");
        formLayout.show(formPanel, "empty");

        add(formPanel, BorderLayout.SOUTH);
    }

    public void displayDeliveryAgents(ArrayList<DeliveryAgent> deliveryAgents) {

        tableModel.setRowCount(0);

        for (DeliveryAgent deliveryAgent : deliveryAgents) {
            tableModel.addRow(new Object[]{
                    deliveryAgent.getId(),
                    deliveryAgent.getName(),
                    deliveryAgent.getPhone(),
                    deliveryAgent.getEmail(),
                    deliveryAgent.getVehicleType(),
                    deliveryAgent.getVehicleNumber()
            });
        }

    }

    public class AddDeliveryAgentPanel extends JPanel {

        public JTextField idTextField;
        public JTextField nameTextField;
        public JTextField phoneTextField;
        public JTextField emailTextField;
        public JTextField vehicleTypeTextField;
        public JTextField vehicleNumberTextField;
        public JButton submitButton;

        public AddDeliveryAgentPanel() {
            GridLayout gridLayout = new GridLayout(7, 2, 10, 10);
            setLayout(gridLayout);

            JLabel idLabel = new JLabel("Agent ID:");
            idTextField = new JTextField();

            add(idLabel);
            add(idTextField);

            JLabel nameLabel = new JLabel("Agent Name:");
            nameTextField = new JTextField();

            add(nameLabel);
            add(nameTextField);

            JLabel phoneLabel = new JLabel("Phone:");
            phoneTextField = new JTextField();

            add(phoneLabel);
            add(phoneTextField);

            JLabel emailLabel = new JLabel("Email:");
            emailTextField = new JTextField();

            add(emailLabel);
            add(emailTextField);

            JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
            vehicleTypeTextField = new JTextField();

            add(vehicleTypeLabel);
            add(vehicleTypeTextField);

            JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");
            vehicleNumberTextField = new JTextField();

            add(vehicleNumberLabel);
            add(vehicleNumberTextField);

            submitButton = new JButton("Add Agent");
            add(new JLabel(""));
            add(submitButton);
        }
    }

    public class UpdateDeliveryAgentPanel extends JPanel {

        public JTextField idTextField;
        public JTextField nameTextField;
        public JTextField phoneTextField;
        public JTextField emailTextField;
        public JTextField vehicleTypeTextField;
        public JTextField vehicleNumberTextField;
        public JButton submitButton;

        public UpdateDeliveryAgentPanel() {
            GridLayout gridLayout = new GridLayout(7, 2, 10, 10);
            setLayout(gridLayout);

            JLabel idLabel = new JLabel("Agent ID:");
            idTextField = new JTextField();

            add(idLabel);
            add(idTextField);

            JLabel nameLabel = new JLabel("Agent Name:");
            nameTextField = new JTextField();

            add(nameLabel);
            add(nameTextField);

            JLabel phoneLabel = new JLabel("Phone:");
            phoneTextField = new JTextField();

            add(phoneLabel);
            add(phoneTextField);

            JLabel emailLabel = new JLabel("Email:");
            emailTextField = new JTextField();

            add(emailLabel);
            add(emailTextField);

            JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
            vehicleTypeTextField = new JTextField();

            add(vehicleTypeLabel);
            add(vehicleTypeTextField);

            JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");
            vehicleNumberTextField = new JTextField();

            add(vehicleNumberLabel);
            add(vehicleNumberTextField);

            submitButton = new JButton("Update Agent");
            add(new JLabel(""));
            add(submitButton);
        }
    }
}
