package android.example.bobo.data.model;

public class FoodItem {
    private int imageResource;
    private int quantity;

    public FoodItem(int imageResource, int quantity) {
        this.imageResource = imageResource;
        this.quantity = quantity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getQuantity() {
        return quantity;
    }
}