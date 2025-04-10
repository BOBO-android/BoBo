package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class QuantityBody {
    @SerializedName("quantity")
    private int quantity;

    public QuantityBody(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}