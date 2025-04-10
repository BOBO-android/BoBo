package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {
    @SerializedName("foodId")
    private String foodId;

    @SerializedName("quantity")
    private int quantity;

    public AddToCartRequest(String foodId, int quantity) {
        this.foodId = foodId;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
