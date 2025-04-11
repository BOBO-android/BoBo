package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.Dish;
import android.example.bobo.data.repository.DishRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MenuViewModel extends ViewModel {

    private DishRepository dishRepository;
    private LiveData<List<Dish>> dishes;
    private LiveData<Boolean> isLoading;
    private LiveData<String> errorMessage;

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

    public void setUseFakeData(boolean useFakeData) {
        dishRepository.setUseFakeData(useFakeData);
    }

    public void fetchDishes(String storeId, int currentPage, int pageSize, String token) {
        dishRepository.fetchDishes(storeId, currentPage, pageSize, token);
    }
}