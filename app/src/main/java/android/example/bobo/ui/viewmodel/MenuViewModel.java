package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.Dish;
import android.example.bobo.data.repository.DishRepository;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MenuViewModel extends ViewModel {
    private final DishRepository dishRepository;
    private final LiveData<List<Dish>> dishes;
    private final LiveData<Boolean> isLoading;
    private final LiveData<String> errorMessage;

    public MenuViewModel() {
        dishRepository = new DishRepository();
        dishes = dishRepository.getDishes();
        isLoading = dishRepository.getIsLoading();
        errorMessage = dishRepository.getErrorMessage();
    }

    public LiveData<List<Dish>> getDishes() {
        return dishes;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchDishes(String storeId, int currentPage, int pageSize, String token) {
        dishRepository.fetchDishes(storeId, currentPage, pageSize, token);
    }
}
