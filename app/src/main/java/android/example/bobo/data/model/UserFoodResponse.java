package android.example.bobo.data.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserFoodResponse {
    @SerializedName("foods")
    private List<JsonElement> foods;

    @SerializedName("lastId")
    private String lastId;

    @SerializedName("hasMore")
    private Boolean hasMore;

    // Constructors
    public UserFoodResponse() {
    }

    public UserFoodResponse(List<JsonElement> foods, String lastId, Boolean hasMore) {
        this.foods = foods;
        this.lastId = lastId;
        this.hasMore = hasMore;
    }

    // Getters and Setters
    public List<JsonElement> getFoods() {
        return foods;
    }

    public void setFoods(List<JsonElement> foods) {
        this.foods = foods;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    @Override
    public String toString() {
        return "FoodResponse{" +
                "foods=" + (foods != null ? foods.size() : 0) + " items" +
                ", lastId='" + lastId + '\'' +
                ", hasMore=" + hasMore +
                '}';
    }
}
