package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.repository.CartRepository;
import android.example.bobo.data.repository.FoodRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FoodDetailViewModel extends ViewModel {
    private final FoodRepository foodRepository;
    private final CartRepository cartRepository;

    public FoodDetailViewModel() {
        foodRepository = new FoodRepository();
        cartRepository = new CartRepository();
    }

    // Food methods
    public void fetchFoodDetailBySlug(String slug) {
        foodRepository.fetchFoodDetailBySlug(slug);
    }

    public LiveData<Dish> getFoodDetail() {
        return foodRepository.getFoodDetail();
    }

    public LiveData<Boolean> getIsLoading() {
        return foodRepository.getIsLoading();
    }

    public LiveData<String> getErrorMessage() {
        return foodRepository.getErrorMessage();
    }

    // Cart methods
    public void addToCart(String token, String foodId, int quantity) {
        cartRepository.addToCart(token, foodId, quantity);
    }

    public LiveData<Boolean> getCartLoading() {
        return cartRepository.isLoading();
    }

    public LiveData<String> getCartErrorMessage() {
        return cartRepository.getErrorMessage();
    }

    public LiveData<ApiResponse<Object>> getCartResponse() {
        return cartRepository.getCartResponse();
    }
}
