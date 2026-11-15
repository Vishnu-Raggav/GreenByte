package Views;

import Models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ClientView extends JPanel {

    public JButton addButton;
    public JButton updateButton;
    public JButton removeButton;
    public JButton clearButton;

    public JTable table;
    public DefaultTableModel tableModel;

    public JPanel formPanel;
    public CardLayout formLayout;
    public AddClientPanel addClientPanel;
    public UpdateClientPanel updateClientPanel;

    public ClientView() {

        String[] columns = {
                "ID", "Name", "Type", "Phone", "Email", "Address"
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
        addClientPanel = new AddClientPanel();
        updateClientPanel = new UpdateClientPanel();
        JPanel emptyPanel = new JPanel();

        formPanel.add(addClientPanel, "add");
        formPanel.add(updateClientPanel, "update");
        formPanel.add(emptyPanel, "empty");
        formLayout.show(formPanel, "empty");

        add(formPanel, BorderLayout.SOUTH);
    }

    public void displayClients(ArrayList<Client> clients) {

        tableModel.setRowCount(0);

        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                    client.getId(),
                    client.getName(),
                    client.getType(),
                    client.getPhone(),
                    client.getEmail(),
                    client.getAddress()
            });
        }

    }

    public class AddClientPanel extends JPanel {

        public JTextField idTextField;
        public JTextField nameTextField;
        public JTextField typeTextField;
        public JTextField phoneTextField;
        public JTextField emailTextField;
        public JTextField addressTextField;
        public JButton submitButton;

        public AddClientPanel() {
            GridLayout gridLayout = new GridLayout(7, 2, 10, 10);
            setLayout(gridLayout);

            JLabel idLabel = new JLabel("Client ID:");
            idTextField = new JTextField();

            add(idLabel);
            add(idTextField);

            JLabel nameLabel = new JLabel("Client Name:");
            nameTextField = new JTextField();

            add(nameLabel);
            add(nameTextField);

            JLabel typeLabel = new JLabel("Client Type (Household/Office):");
            typeTextField = new JTextField();

            add(typeLabel);
            add(typeTextField);

            JLabel phoneLabel = new JLabel("Phone (10 digits):");
            phoneTextField = new JTextField();

            add(phoneLabel);
            add(phoneTextField);

            JLabel emailLabel = new JLabel("Email:");
            emailTextField = new JTextField();

            add(emailLabel);
            add(emailTextField);

            JLabel addressLabel = new JLabel("Address:");
            addressTextField = new JTextField();

            add(addressLabel);
            add(addressTextField);

            submitButton = new JButton("Add Client");
            add(new JLabel(""));
            add(submitButton);
        }
    }

    public class UpdateClientPanel extends JPanel {

        public JTextField idTextField;
        public JTextField nameTextField;
        public JTextField typeTextField;
        public JTextField phoneTextField;
        public JTextField emailTextField;
        public JTextField addressTextField;
        public JButton submitButton;

        public UpdateClientPanel() {
            GridLayout gridLayout = new GridLayout(7, 2, 10, 10);
            setLayout(gridLayout);

            JLabel idLabel = new JLabel("Client ID:");
            idTextField = new JTextField();

            add(idLabel);
            add(idTextField);

            JLabel nameLabel = new JLabel("Client Name:");
            nameTextField = new JTextField();

            add(nameLabel);
            add(nameTextField);

            JLabel typeLabel = new JLabel("Client Type (Household/Office):");
            typeTextField = new JTextField();

            add(typeLabel);
            add(typeTextField);

            JLabel phoneLabel = new JLabel("Phone (10 digits):");
            phoneTextField = new JTextField();

            add(phoneLabel);
            add(phoneTextField);

            JLabel emailLabel = new JLabel("Email:");
            emailTextField = new JTextField();

            add(emailLabel);
            add(emailTextField);

            JLabel addressLabel = new JLabel("Address:");
            addressTextField = new JTextField();

            add(addressLabel);
            add(addressTextField);

            submitButton = new JButton("Update Client");
            add(new JLabel(""));
            add(submitButton);
        }
    }
}
