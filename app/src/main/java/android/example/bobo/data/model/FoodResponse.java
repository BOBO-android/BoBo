package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodResponse {

    @SerializedName("foods")
    private List<Dish> foods; // List of dishes

    @SerializedName("totalPage")
    private int totalPage;

    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("current")
    private int current;

    // Getters
    public List<Dish> getFoods() {
        return foods;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getCurrent() {
        return current;
    }
}
