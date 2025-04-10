package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Cart {
    @SerializedName("_id")
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("items")
    private List<CartItem> items;

    // Constructor
    public Cart() {
        // Empty constructor cho Gson
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    // Method tính tổng giá trị giỏ hàng
    public double getTotalAmount() {
        double total = 0;
        if (items != null) {
            for (CartItem item : items) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        return total;
    }
}