package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.FoodSearchResponse;
import android.example.bobo.data.model.SearchFoodItemModel;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSearchRepository {
    private final ApiService apiService;

    public FoodSearchRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get food by slug (single item)
    public LiveData<SearchResourceState<SearchFoodItemModel>> getFoodBySlug(String slug) {
        MutableLiveData<SearchResourceState<SearchFoodItemModel>> foodData = new MutableLiveData<>();
        foodData.setValue(SearchResourceState.loading(null));

        Call<ApiResponse<SearchFoodItemModel>> call = apiService.getFoodBySlug(slug);
        call.enqueue(new Callback<ApiResponse<SearchFoodItemModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchFoodItemModel>> call, Response<ApiResponse<SearchFoodItemModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null) {
                        foodData.setValue(SearchResourceState.success(response.body().getData()));
                    } else {
                        foodData.setValue(SearchResourceState.error("Food not found", null));
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        foodData.setValue(SearchResourceState.error("Error: " + errorBody, null));
                    } catch (IOException e) {
                        foodData.setValue(SearchResourceState.error("Error: " + response.message(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchFoodItemModel>> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    foodData.setValue(SearchResourceState.error("Request timed out. Please try again.", null));
                } else {
                    foodData.setValue(SearchResourceState.error("Network error: " + t.getMessage(), null));
                }
            }
        });

        return foodData;
    }

    // Search foods by query (multiple items)
    public LiveData<SearchResourceState<List<SearchFoodItemModel>>> searchFoods(String query) {
        MutableLiveData<SearchResourceState<List<SearchFoodItemModel>>> foodsData = new MutableLiveData<>();
        foodsData.setValue(SearchResourceState.loading(null));

        Call<ApiResponse<FoodSearchResponse>> call = apiService.searchFoods(query);
        call.enqueue(new Callback<ApiResponse<FoodSearchResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<FoodSearchResponse>> call, Response<ApiResponse<FoodSearchResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodSearchResponse foodSearchResponse = response.body().getData();
                    if (foodSearchResponse != null && foodSearchResponse.getFoods() != null) {
                        foodsData.setValue(SearchResourceState.success(foodSearchResponse.getFoods()));
                    } else {
                        foodsData.setValue(SearchResourceState.error("No foods found", null));
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        foodsData.setValue(SearchResourceState.error("Error: " + errorBody, null));
                    } catch (IOException e) {
                        foodsData.setValue(SearchResourceState.error("Error: " + response.message(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<FoodSearchResponse>> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    foodsData.setValue(SearchResourceState.error("Request timed out. Please try again.", null));
                } else {
                    foodsData.setValue(SearchResourceState.error("Network error: " + t.getMessage(), null));
                }
            }
        });

        return foodsData;
    }
}