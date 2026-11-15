package Models;

public class Client implements Identifiable {
    public static final String HOUSEHOLD = "Household";
    public static final String OFFICE = "Office";

    private int id;
    private String name;
    private String type;
    private String email;
    private String phone;
    private String address;

    public Client(int id, String name, String type, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
