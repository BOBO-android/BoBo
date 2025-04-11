package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.FoodResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishRepository {

    private ApiService apiService;
    private MutableLiveData<List<Dish>> dishesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private boolean useFakeData = false; // Control fake data here

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

    public void setUseFakeData(boolean useFakeData) {
        this.useFakeData = useFakeData;
    }

    public void fetchDishes(String storeId, int currentPage, int pageSize, String token) {
        isLoading.postValue(true);

        if (useFakeData) {
            List<Dish> fakeDishes = createFakeDishList();
            dishesLiveData.postValue(fakeDishes);
            isLoading.postValue(false);
        } else {
            apiService.getDishes("Bearer " + token, storeId, currentPage, pageSize)
                    .enqueue(new Callback<ApiResponse<FoodResponse>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<FoodResponse>> call, Response<ApiResponse<FoodResponse>> response) {
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
                        public void onFailure(Call<ApiResponse<FoodResponse>> call, Throwable t) {
                            isLoading.postValue(false);
                            errorMessage.postValue("Network error: " + t.getMessage());
                        }
                    });
        }
    }

    private List<Dish> createFakeDishList() {
        List<Dish> fakeList = new ArrayList<>();
        fakeList.add(new Dish("1", "Pepperoni Cheese Pizza", 12.99, "Delicious pepperoni pizza", "store1", true, 15, 4, "https://via.placeholder.com/150/FFC107/000000?Text=Pizza", false, null, true, 0, "", ""));
        fakeList.add(new Dish("2", "Classic Burger", 12.75, "Juicy classic burger", "store1", true, 10, 5, "https://via.placeholder.com/150/4CAF50/FFFFFF?Text=Burger", false, null, true, 0.1, "", ""));
        fakeList.add(new Dish("3", "Donut Box", 13.45, "Assorted delicious donuts", "store1", true, 5, 4, "https://via.placeholder.com/150/F44336/FFFFFF?Text=Donuts", false, null, true, 0.2, "", ""));
        fakeList.add(new Dish("4", "Donut Box", 13.45, "Assorted delicious donuts", "store1", true, 5, 4, "https://via.placeholder.com/150/F44336/FFFFFF?Text=Donuts", false, null, true, 0.2, "", ""));
        fakeList.add(new Dish("5", "Donut Box", 13.45, "Assorted delicious donuts", "store1", true, 5, 4, "https://via.placeholder.com/150/F44336/FFFFFF?Text=Donuts", false, null, true, 0.2, "", ""));
        // Add more fake dishes as needed
        return fakeList;
    }
}