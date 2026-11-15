package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;

public class ClientController {

    private static final String TYPE_HOUSEHOLD = "Household";
    private static final String TYPE_OFFICE = "Office";
    private static final String PHONE_PATTERN = "\\d{10}";
    private static final String EMAIL_PATTERN = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+";

    public ClientView view;
    public ClientRepository repository;

    public ClientController(ClientView clientView, ClientRepository clientRepository) {
        // Initialize
        view = clientView;
        repository = clientRepository;

        // Add Data
        repository.addClient(new Client(1, "Nimal Perera", "Household", "0771234567", "nimal.perera@gmail.com", "12 Galle Road, Colombo 03"));
        repository.addClient(new Client(2, "Green Leaf Offices", "Office", "0112345678", "info@greenleaf.lk", "45 Union Place, Colombo 02"));
        repository.addClient(new Client(3, "Kamani Fernando", "Household", "0712345678", "kamani.f@yahoo.com", "8 Temple Road, Kandy"));
        repository.addClient(new Client(4, "Lanka Tech Solutions", "Office", "0117654321", "admin@lankatech.lk", "21 Duplication Road, Colombo 04"));
        repository.addClient(new Client(5, "Sunil Jayasinghe", "Household", "0759876543", "sunil.j@gmail.com", "3 Beach Road, Galle"));

        // Show Data
        view.displayClients(repository.getAllClients());

        view.addButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "add");
        });

        view.addClientPanel.submitButton.addActionListener(e1 -> {

            String id = view.addClientPanel.idTextField.getText().trim();
            String name = view.addClientPanel.nameTextField.getText().trim();
            String type = view.addClientPanel.typeTextField.getText().trim();
            String phone = view.addClientPanel.phoneTextField.getText().trim();
            String email = view.addClientPanel.emailTextField.getText().trim();
            String address = view.addClientPanel.addressTextField.getText().trim();

            if (
                    id.isEmpty() ||
                            name.isEmpty() ||
                            type.isEmpty() ||
                            phone.isEmpty() ||
                            email.isEmpty() ||
                            address.isEmpty()
            ) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idInt;

            try {
                idInt = Integer.parseInt(id);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Number required for id", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Client client : repository.getAllClients()) {
                if (client.getId() == idInt) {
                    JOptionPane.showMessageDialog(null, "Id already exists", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            String errorMessage = validateClientRules(idInt, type, phone, email);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Client newClient = new Client(idInt, name, formatType(type), phone, email, address);
            repository.addClient(newClient);
            view.displayClients(repository.getAllClients());
            JOptionPane.showMessageDialog(null, "Client Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearTextFields(new JTextField[] {
                    view.addClientPanel.idTextField,
                    view.addClientPanel.nameTextField,
                    view.addClientPanel.typeTextField,
                    view.addClientPanel.phoneTextField,
                    view.addClientPanel.emailTextField,
                    view.addClientPanel.addressTextField
            });
        });

        view.updateButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "update");
        });

        view.updateClientPanel.submitButton.addActionListener(e1 -> {

            String id = view.updateClientPanel.idTextField.getText().trim();
            String name = view.updateClientPanel.nameTextField.getText().trim();
            String type = view.updateClientPanel.typeTextField.getText().trim();
            String phone = view.updateClientPanel.phoneTextField.getText().trim();
            String email = view.updateClientPanel.emailTextField.getText().trim();
            String address = view.updateClientPanel.addressTextField.getText().trim();

            if (
                    id.isEmpty() ||
                            name.isEmpty() ||
                            type.isEmpty() ||
                            phone.isEmpty() ||
                            email.isEmpty() ||
                            address.isEmpty()
            ) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idInt;

            try {
                idInt = Integer.parseInt(id);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Number required for id", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean idExists = false;
            for (Client client : repository.getAllClients()) {
                if (client.getId() == idInt) {
                    idExists = true;
                    break;
                }
            }

            if (!idExists) {
                JOptionPane.showMessageDialog(null, "Id doesn't exist", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String errorMessage = validateClientRules(idInt, type, phone, email);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Client updatedClient = new Client(idInt, name, formatType(type), phone, email, address);
            repository.updateClient(updatedClient);
            view.displayClients(repository.getAllClients());
            JOptionPane.showMessageDialog(null, "Client Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearTextFields(new JTextField[] {
                    view.updateClientPanel.idTextField,
                    view.updateClientPanel.nameTextField,
                    view.updateClientPanel.typeTextField,
                    view.updateClientPanel.phoneTextField,
                    view.updateClientPanel.emailTextField,
                    view.updateClientPanel.addressTextField
            });
        });

        view.removeButton.addActionListener(e -> {
            int selectedRow = view.table.getSelectedRow();

            if (selectedRow != -1) {
                int clientId = (int) view.tableModel.getValueAt(selectedRow, 0);
                repository.removeClient(clientId);
                view.displayClients(repository.getAllClients());
            } else {
                JOptionPane.showMessageDialog(null, "Please select a client!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.clearButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "empty");
        });
    }

    /**
     * Checks the client-specific rules and returns an error message,
     * or null when every rule is satisfied.
     */
    private String validateClientRules(int id, String type, String phone, String email) {
        if (id <= 0) {
            return "Id must be greater than 0";
        }
        if (!type.equalsIgnoreCase(TYPE_HOUSEHOLD) && !type.equalsIgnoreCase(TYPE_OFFICE)) {
            return "Client type must be Household or Office";
        }
        if (!phone.matches(PHONE_PATTERN)) {
            return "Phone number must be exactly 10 digits";
        }
        if (!email.matches(EMAIL_PATTERN)) {
            return "Please enter a valid email address (e.g. name@example.com)";
        }
        return null;
    }

    private String formatType(String type) {
        return type.equalsIgnoreCase(TYPE_OFFICE) ? TYPE_OFFICE : TYPE_HOUSEHOLD;
    }

    private void clearTextFields(JTextField[] textFields) {
        for (JTextField field : textFields) {
            field.setText("");
        }
    }
}
