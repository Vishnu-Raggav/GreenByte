package Models;

public class Product implements Identifiable {
    private int id;
    private String name;
    private String category;
    private double price;
    private int ecoRating;

    public Product(int id, String name, String category, double price, int ecoRating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.ecoRating = ecoRating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getEcoRating() {
        return ecoRating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setEcoRating(int ecoRating) {
        this.ecoRating = ecoRating;
    }

    @Override
    public String toString() {
        return name + " (#" + id + ")";
    }
}