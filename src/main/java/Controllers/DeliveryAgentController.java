package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;

public class DeliveryAgentController {

    private static final String[] VEHICLE_TYPES = {"Motorbike", "Three-Wheeler", "Van", "Lorry"};
    private static final String PHONE_PATTERN = "\\d{10}";
    private static final String EMAIL_PATTERN = "[^@\\s]+@[^@\\s]+\\.[^@\\s]+";
    private static final String VEHICLE_NUMBER_PATTERN = "[A-Za-z0-9 \\-]{4,12}";

    public DeliveryAgentView view;
    public DeliveryAgentRepository repository;

    public DeliveryAgentController(DeliveryAgentView deliveryAgentView, DeliveryAgentRepository deliveryAgentRepository) {
        // Initialize
        view = deliveryAgentView;
        repository = deliveryAgentRepository;

        // Add Data (agents 1 and 2 match the agents used by the sample deliveries)
        repository.addDeliveryAgent(new DeliveryAgent(1, "Kamal Silva", "0771112233", "kamal.silva@econest.lk", "Motorbike", "BCD-4521"));
        repository.addDeliveryAgent(new DeliveryAgent(2, "Nuwan Fernando", "0712223344", "nuwan.fernando@econest.lk", "Van", "CAB-7788"));
        repository.addDeliveryAgent(new DeliveryAgent(3, "Saman Perera", "0753334455", "saman.perera@econest.lk", "Three-Wheeler", "QL-3345"));
        repository.addDeliveryAgent(new DeliveryAgent(4, "Dilani Jayawardena", "0764445566", "dilani.j@econest.lk", "Motorbike", "BDF-9012"));
        repository.addDeliveryAgent(new DeliveryAgent(5, "Ruwan Bandara", "0725556677", "ruwan.bandara@econest.lk", "Lorry", "NC-5566"));

        // Show Data
        view.displayDeliveryAgents(repository.getAllDeliveryAgents());

        view.addButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "add");
        });

        view.addDeliveryAgentPanel.submitButton.addActionListener(e1 -> {

            String id = view.addDeliveryAgentPanel.idTextField.getText().trim();
            String name = view.addDeliveryAgentPanel.nameTextField.getText().trim();
            String phone = view.addDeliveryAgentPanel.phoneTextField.getText().trim();
            String email = view.addDeliveryAgentPanel.emailTextField.getText().trim();
            String vehicleType = view.addDeliveryAgentPanel.vehicleTypeTextField.getText().trim();
            String vehicleNumber = view.addDeliveryAgentPanel.vehicleNumberTextField.getText().trim();

            if (
                    id.isEmpty() ||
                            name.isEmpty() ||
                            phone.isEmpty() ||
                            email.isEmpty() ||
                            vehicleType.isEmpty() ||
                            vehicleNumber.isEmpty()
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

            if (repository.getDeliveryAgentById(idInt) != null) {
                JOptionPane.showMessageDialog(null, "Id already exists", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String errorMessage = validateAgentRules(idInt, phone, email, vehicleType, vehicleNumber);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DeliveryAgent newDeliveryAgent = new DeliveryAgent(idInt, name, phone, email, formatVehicleType(vehicleType), vehicleNumber.toUpperCase());
            repository.addDeliveryAgent(newDeliveryAgent);
            view.displayDeliveryAgents(repository.getAllDeliveryAgents());
            JOptionPane.showMessageDialog(null, "Delivery Agent Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearTextFields(new JTextField[] {
                    view.addDeliveryAgentPanel.idTextField,
                    view.addDeliveryAgentPanel.nameTextField,
                    view.addDeliveryAgentPanel.phoneTextField,
                    view.addDeliveryAgentPanel.emailTextField,
                    view.addDeliveryAgentPanel.vehicleTypeTextField,
                    view.addDeliveryAgentPanel.vehicleNumberTextField
            });
        });

        view.updateButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "update");
        });

        view.updateDeliveryAgentPanel.submitButton.addActionListener(e1 -> {

            String id = view.updateDeliveryAgentPanel.idTextField.getText().trim();
            String name = view.updateDeliveryAgentPanel.nameTextField.getText().trim();
            String phone = view.updateDeliveryAgentPanel.phoneTextField.getText().trim();
            String email = view.updateDeliveryAgentPanel.emailTextField.getText().trim();
            String vehicleType = view.updateDeliveryAgentPanel.vehicleTypeTextField.getText().trim();
            String vehicleNumber = view.updateDeliveryAgentPanel.vehicleNumberTextField.getText().trim();

            if (
                    id.isEmpty() ||
                            name.isEmpty() ||
                            phone.isEmpty() ||
                            email.isEmpty() ||
                            vehicleType.isEmpty() ||
                            vehicleNumber.isEmpty()
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

            if (repository.getDeliveryAgentById(idInt) == null) {
                JOptionPane.showMessageDialog(null, "Id doesn't exist", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String errorMessage = validateAgentRules(idInt, phone, email, vehicleType, vehicleNumber);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DeliveryAgent updatedDeliveryAgent = new DeliveryAgent(idInt, name, phone, email, formatVehicleType(vehicleType), vehicleNumber.toUpperCase());
            repository.updateDeliveryAgent(updatedDeliveryAgent);
            view.displayDeliveryAgents(repository.getAllDeliveryAgents());
            JOptionPane.showMessageDialog(null, "Delivery Agent Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearTextFields(new JTextField[] {
                    view.updateDeliveryAgentPanel.idTextField,
                    view.updateDeliveryAgentPanel.nameTextField,
                    view.updateDeliveryAgentPanel.phoneTextField,
                    view.updateDeliveryAgentPanel.emailTextField,
                    view.updateDeliveryAgentPanel.vehicleTypeTextField,
                    view.updateDeliveryAgentPanel.vehicleNumberTextField
            });
        });

        view.removeButton.addActionListener(e -> {
            int selectedRow = view.table.getSelectedRow();

            if (selectedRow != -1) {
                int deliveryAgentId = (int) view.tableModel.getValueAt(selectedRow, 0);
                repository.removeDeliveryAgent(deliveryAgentId);
                view.displayDeliveryAgents(repository.getAllDeliveryAgents());
            } else {
                JOptionPane.showMessageDialog(null, "Please select a delivery agent!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.clearButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "empty");
        });
    }

    /**
     * Checks the agent-specific rules and returns an error message,
     * or null when every rule is satisfied.
     */
    private String validateAgentRules(int id, String phone, String email, String vehicleType, String vehicleNumber) {
        if (id <= 0) {
            return "Id must be greater than 0";
        }
        if (!phone.matches(PHONE_PATTERN)) {
            return "Phone number must be exactly 10 digits";
        }
        if (!email.matches(EMAIL_PATTERN)) {
            return "Please enter a valid email address (e.g. name@example.com)";
        }
        if (formatVehicleType(vehicleType) == null) {
            return "Vehicle type must be Motorbike, Three-Wheeler, Van or Lorry";
        }
        if (!vehicleNumber.matches(VEHICLE_NUMBER_PATTERN)) {
            return "Vehicle number must be 4 to 12 letters, digits, spaces or hyphens (e.g. CAB-1234)";
        }
        for (DeliveryAgent deliveryAgent : repository.getAllDeliveryAgents()) {
            if (deliveryAgent.getId() != id && deliveryAgent.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                return "Vehicle number already belongs to " + deliveryAgent.getName();
            }
        }
        return null;
    }

    /** Returns the vehicle type spelled the standard way, or null when it is not an allowed type. */
    private String formatVehicleType(String vehicleType) {
        for (String allowedType : VEHICLE_TYPES) {
            if (allowedType.equalsIgnoreCase(vehicleType)) {
                return allowedType;
            }
        }
        return null;
    }

    private void clearTextFields(JTextField[] textFields) {
        for (JTextField field : textFields) {
            field.setText("");
        }
    }
}
