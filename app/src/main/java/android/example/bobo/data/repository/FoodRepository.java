package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.UserFoodItem;
import android.example.bobo.data.model.UserFoodResponse;
import android.example.bobo.network.FoodService;
import android.example.bobo.network.RetrofitClient;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {
    private static final String TAG = "FoodRepository";

    private final FoodService foodService;
    private final MutableLiveData<List<UserFoodItem>> foodsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Dish> foodDetailLiveData = new MutableLiveData<>();

    private boolean hasMoreData = true;
    private String lastId = null;
    private boolean isFirstLoad = true;
    private String currentCategory = "all";

    public FoodRepository() {
        foodService = RetrofitClient.getInstance().create(FoodService.class);
    }

    public LiveData<List<UserFoodItem>> getFoods() {
        return foodsLiveData;
    }

    public LiveData<Dish> getFoodDetail() {
        return foodDetailLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public boolean hasMoreData() {
        return hasMoreData;
    }

    /**
     * Set current category for filtering foods
     * @param category Category to filter by (offered, burger, pizza, donut, or all)
     */
    public void setCategory(String category) {
        if (!this.currentCategory.equals(category)) {
            this.currentCategory = category;
            resetAndFetch(10); // Reset and fetch when category changes
        }
    }

    /**
     * Fetch foods based on current category with cursor-based pagination
     *
     * @param limit Number of items to fetch
     * @param lastId ID of the last item from previous fetch for pagination (can be null for first page)
     */
    public void fetchFoods(int limit, String lastId) {
        isLoading.postValue(true);

        Log.d(TAG, "Fetching foods with category: " + currentCategory + ", limit: " + limit + ", lastId: " + lastId);

        Call<ApiResponse<UserFoodResponse>> call;

        call = foodService.getPublicOfferedFoods(limit, lastId);

//        // Select API endpoint based on category
//        if ("offered".equals(currentCategory)) {
//            call = foodService.getPublicOfferedFoods(limit, lastId);
//        } else if ("all".equals(currentCategory)) {
////            call = foodService.getPublicFoods(limit, lastId, null);
//        } else {
//            // For specific categories: burger, pizza, donut, etc.
////            call = foodService.getPublicFoods(limit, lastId, currentCategory);
//        }

        call.enqueue(new Callback<ApiResponse<UserFoodResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserFoodResponse>> call, @NonNull Response<ApiResponse<UserFoodResponse>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserFoodResponse> apiResponse = response.body();
                    Log.d(TAG, "API response: " + apiResponse.getMessage());

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        UserFoodResponse userFoodResponse = apiResponse.getData();
                        List<UserFoodItem> foodItems = convertToUserFoodItems(userFoodResponse.getFoods());

                        // Update pagination data from response
                        FoodRepository.this.lastId = userFoodResponse.getLastId();
                        hasMoreData = userFoodResponse.getHasMore() != null ? userFoodResponse.getHasMore() : false;

                        Log.d(TAG, "Fetched " + foodItems.size() + " items, lastId: " + lastId + ", hasMore: " + hasMoreData);

                        // Update the LiveData
                        List<UserFoodItem> currentList = foodsLiveData.getValue();
                        if (currentList == null) {
                            currentList = new ArrayList<>();
                        }

                        if (isFirstLoad) {
                            // If it's the first load, clear the list
                            currentList = new ArrayList<>();
                            isFirstLoad = false;
                        }

                        // Add new items to the list
                        currentList.addAll(foodItems);
                        foodsLiveData.postValue(currentList);
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.postValue("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserFoodResponse>> call, @NonNull Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Network error: " + t.getLocalizedMessage());
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    /**
     * Fetch food detail by slug
     * @param slug The slug of the food item
     */
    public void fetchFoodDetailBySlug(String slug) {
        isLoading.postValue(true);
        errorMessage.postValue(null); // Clear previous error message

        Log.d(TAG, "Fetching food detail with slug: " + slug);

        foodService.getFoodBySlug(slug).enqueue(new Callback<ApiResponse<Dish>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Dish>> call, @NonNull Response<ApiResponse<Dish>> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Dish> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Dish dish = apiResponse.getData();
                        Log.d(TAG, "Fetched food detail: " + dish.getName());
                        foodDetailLiveData.postValue(dish);
                    } else {
                        errorMessage.postValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.postValue("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Dish>> call, @NonNull Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue("Network error: " + t.getLocalizedMessage());
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    /**
     * Fetch offered foods (special method maintained for backward compatibility)
     */
    public void fetchPublicOfferedFoods(int limit, String lastId) {
        currentCategory = "offered";
        fetchFoods(limit, lastId);
    }

    /**
     * Convert API food models to UserFoodItem models
     */
    private List<UserFoodItem> convertToUserFoodItems(List<?> apiFoods) {
        if (apiFoods == null) return new ArrayList<>();

        List<UserFoodItem> foodItems = new ArrayList<>();

        for (Object foodObj : apiFoods) {
            try {
                if (foodObj instanceof JsonObject) {
                    JsonObject food = (JsonObject) foodObj;

                    String id = getStringOrEmpty(food, "_id");
                    String name = getStringOrEmpty(food, "name");
                    String slug = getStringOrEmpty(food, "slug");
                    double price = food.has("finalPrice") ? food.get("finalPrice").getAsDouble() :
                            (food.has("price") ? food.get("price").getAsDouble() : 0);
                    float rating = food.has("rating") ? food.get("rating").getAsFloat() : 0f;
                    String thumbnail = getStringOrEmpty(food, "thumbnail");

                    UserFoodItem foodItem = new UserFoodItem(id, name, thumbnail, slug, price, rating);
                    foodItem.setImageUrl(thumbnail);

                    foodItems.add(foodItem);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing food item", e);
            }
        }

        return foodItems;
    }

    private String getStringOrEmpty(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : "";
    }

    /**
     * Reset pagination and fetch the first page
     */
    public void resetAndFetch(int limit) {
        lastId = null;
        hasMoreData = true;
        isFirstLoad = true;
        fetchFoods(limit, null);
    }

    /**
     * Load more items (next page)
     */
    public void loadMore(int limit) {
        if (hasMoreData && isLoading.getValue() != null && !isLoading.getValue()) {
            fetchFoods(limit, lastId);
        }
    }
}
