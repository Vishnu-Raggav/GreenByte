package Controllers;

import Views.*;
import Models.*;

import javax.swing.*;

public class ProductController {

    public ProductView view;
    public ProductRepository repository;

    public ProductController(ProductView productView, ProductRepository productRepository) {
        // Initialize
        view = productView;
        repository = productRepository;

        // Add Data
        repository.addProduct(new Product(1, "Bamboo Planter", "Planters", 1500.00, 5));
        repository.addProduct(new Product(2, "Snake Plant", "Indoor Plants", 2500.00, 4));
        repository.addProduct(new Product(3, "Coconut Coir Pot", "Planters", 850.00, 5));
        repository.addProduct(new Product(4, "Organic Soil Mix", "Gardening", 1200.00, 4));
        repository.addProduct(new Product(5, "Peace Lily", "Indoor Plants", 3200.00, 5));

        // Show Data
        view.displayProducts(repository.getAllProducts());

        view.addButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "add");
        });

        view.addProductPanel.submitButton.addActionListener(e1 -> {

            String id = view.addProductPanel.idTextField.getText().trim();
            String name = view.addProductPanel.nameTextField.getText().trim();
            String category = view.addProductPanel.categoryTextField.getText().trim();
            String price = view.addProductPanel.priceTextField.getText().trim();
            String rating = view.addProductPanel.ratingTextField.getText().trim();

            if (
                    id.isEmpty() ||
                            name.isEmpty() ||
                            category.isEmpty() ||
                            price.isEmpty() ||
                            rating.isEmpty()
            ) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idInt;
            double priceDouble;
            int ratingInt;

            try {
                idInt = Integer.parseInt(id);
                priceDouble = Double.parseDouble(price);
                ratingInt = Integer.parseInt(rating);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Number required for id, price and rating", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Product product : repository.getAllProducts()) {
                if (product.getId() == idInt) {
                    JOptionPane.showMessageDialog(null, "Id already exists", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            Product newProduct = new Product(idInt, name, category, priceDouble, ratingInt);
            repository.addProduct(newProduct);
            view.displayProducts(repository.getAllProducts());
            JOptionPane.showMessageDialog(null, "Product Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearTextFields(new JTextField[] {
                    view.addProductPanel.idTextField,
                    view.addProductPanel.nameTextField,
                    view.addProductPanel.categoryTextField,
                    view.addProductPanel.priceTextField,
                    view.addProductPanel.ratingTextField
            });
        });

        view.updateButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "update");
        });

        view.updateProductPanel.submitButton.addActionListener(e1-> {

            String id = view.updateProductPanel.idTextField.getText().trim();
            String name = view.updateProductPanel.nameTextField.getText().trim();
            String category = view.updateProductPanel.categoryTextField.getText().trim();
            String price = view.updateProductPanel.priceTextField.getText().trim();
            String rating = view.updateProductPanel.ratingTextField.getText().trim();

            if (
                    id.isEmpty() ||
                            name.isEmpty() ||
                            category.isEmpty() ||
                            price.isEmpty() ||
                            rating.isEmpty()
            ) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idInt;
            double priceDouble;
            int ratingInt;

            try {
                idInt = Integer.parseInt(id);
                priceDouble = Double.parseDouble(price);
                ratingInt = Integer.parseInt(rating);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Number required for id, price and rating", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean idExists = false;
            for (Product product : repository.getAllProducts()) {
                if (product.getId() == idInt) {
                    idExists = true;
                    break;
                }
            }

            if(!idExists) {
                JOptionPane.showMessageDialog(null, "Id doesn't exist", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product newProduct = new Product(idInt, name, category, priceDouble, ratingInt);
            repository.updateProduct(newProduct);
            view.displayProducts(repository.getAllProducts());
            JOptionPane.showMessageDialog(null, "Product Updated Successfully", "Succes", JOptionPane.INFORMATION_MESSAGE);
            clearTextFields(new JTextField[] {
                    view.updateProductPanel.idTextField,
                    view.updateProductPanel.nameTextField,
                    view.updateProductPanel.categoryTextField,
                    view.updateProductPanel.priceTextField,
                    view.updateProductPanel.ratingTextField
            });
        });

        view.removeButton.addActionListener(e -> {
            int selectedRow = view.table.getSelectedRow();

            if (selectedRow != -1) {
                int productId = (int) view.tableModel.getValueAt(selectedRow, 0);
                repository.removeProduct(productId);
                view.displayProducts(repository.getAllProducts());
            } else {
                JOptionPane.showMessageDialog(null, "Please select a product!", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        });

        view.clearButton.addActionListener(e -> {
            view.formLayout.show(view.formPanel, "empty");
        });
    }

    private void clearTextFields(JTextField[] textFields) {
        for(JTextField field : textFields) {
            field.setText("");
        }
    }
}
