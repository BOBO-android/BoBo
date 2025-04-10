package android.example.bobo.data.model;

import android.example.bobo.ui.adapters.FoodIdTypeAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("_id")
    private String id;

    @SerializedName("foodId")
    @JsonAdapter(FoodIdTypeAdapter.class)
    private String foodId;

    private String name;
    private double price;
    private int quantity;

    @SerializedName("thumbnail")
    private String imageUrl;

    private int imageResId;

    public CartItem(String id, String foodId, String name, double price, int quantity, String imageUrl) {
        this.id = id;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public CartItem(String id, String foodId, String name, double price, int quantity, int imageResId) {
        this.id = id;
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    
    public String getItemId() {
        // Nếu foodId rỗng hoặc null, trả về id
        return (foodId == null || foodId.isEmpty()) ? id : foodId;
    }
}