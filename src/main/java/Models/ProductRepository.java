package Models;

import java.util.ArrayList;

public class ProductRepository {

    private ArrayList<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    public void removeProduct(int id) {
        products.removeIf(product -> product.getId() == id);
    }
}