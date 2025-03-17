package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.FoodResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishRepository {
    private final ApiService apiService;
    private final MutableLiveData<List<Dish>> dishesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public DishRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<Dish>> getDishes() {
        return dishesLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchDishes(String storeId, int currentPage, int pageSize, String token) {
        isLoading.postValue(true);

        apiService.getDishes("Bearer " + token, storeId, currentPage, pageSize)
                .enqueue(new Callback<ApiResponse<FoodResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<FoodResponse>> call, @NonNull Response<ApiResponse<FoodResponse>> response) {
                        isLoading.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<FoodResponse> apiResponse = response.body();

                            if (apiResponse.isSuccess()) {
                                dishesLiveData.postValue(apiResponse.getData().getFoods());
                            } else {
                                errorMessage.postValue(apiResponse.getMessage());
                            }
                        } else {
                            errorMessage.postValue("Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<FoodResponse>> call, @NonNull Throwable t) {
                        isLoading.postValue(false);
                        errorMessage.postValue("Network error: " + t.getLocalizedMessage());
                    }
                });
    }
}
