package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FoodSearchResponse {
    @SerializedName("foods")
    private List<SearchFoodItemModel> foods;

    @SerializedName("total")
    private int total;

    public List<SearchFoodItemModel> getFoods() {
        return foods;
    }

    public void setFoods(List<SearchFoodItemModel> foods) {
        this.foods = foods;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}