package android.example.bobo.data.model;

public class FoodItem {
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;

    // Local properties for backward compatibility
    private int imageResId;

    // Default constructor for serialization
    public FoodItem() {
    }

    // Constructor with essential fields
    public FoodItem(String name, double price, String imageUrl, int quantity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // Legacy constructor for backward compatibility
    public FoodItem(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 1;
    }

    // Helper method to get formatted price as string with currency symbol
    public String getFormattedPrice() {
        return "$" + String.format("%.2f", price);
    }

    // Helper method to get total price for this item (price × quantity)
    public double getTotalPrice() {
        return price * quantity;
    }

    // Helper method to get formatted total price
    public String getFormattedTotalPrice() {
        return "$" + String.format("%.2f", getTotalPrice());
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
