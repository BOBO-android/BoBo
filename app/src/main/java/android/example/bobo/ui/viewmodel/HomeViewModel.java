package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.BaseUserProfile;
import android.example.bobo.data.model.UserFoodItem;
import android.example.bobo.data.repository.CartRepository;
import android.example.bobo.data.repository.FoodRepository;
import android.example.bobo.data.repository.UserRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final CartRepository cartRepository;

    public HomeViewModel() {
        userRepository = new UserRepository();
        foodRepository = new FoodRepository();
        cartRepository = new CartRepository();
    }

    /**
     * Gets user profile LiveData
     * @return LiveData containing user profile
     */
    public LiveData<BaseUserProfile> getUserProfile() {
        return userRepository.getUserProfile();
    }

    /**
     * Gets foods LiveData
     * @return LiveData containing list of food items
     */
    public LiveData<List<UserFoodItem>> getFoods() {
        return foodRepository.getFoods();
    }

    /**
     * Gets loading state LiveData
     * @return LiveData for loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return foodRepository.getIsLoading();
    }

    /**
     * Gets food repository error message LiveData
     * @return LiveData for error messages
     */
    public LiveData<String> getFoodErrorMessage() {
        return foodRepository.getErrorMessage();
    }

    /**
     * Gets user repository error message LiveData
     * @return LiveData for error messages
     */
    public LiveData<String> getUserErrorMessage() {
        return userRepository.getErrorMessage();
    }

    /**
     * Fetches user profile data
     * @param token Authentication token
     */
    public void fetchUserProfile(String token) {
        userRepository.fetchUserProfile(token);
    }

    /**
     * Resets and fetches initial food data
     * @param itemsPerPage Number of items to fetch
     */
    public void loadFoods(int itemsPerPage) {
        foodRepository.resetAndFetch(itemsPerPage);
    }

    /**
     * Sets food category filter
     * @param category Category to filter by
     */
    public void setCategory(String category) {
        foodRepository.setCategory(category);
    }

    /**
     * Loads more food items for pagination
     * @param itemsPerPage Number of additional items to load
     */
    public void loadMoreFoods(int itemsPerPage) {
        foodRepository.loadMore(itemsPerPage);
    }

    /**
     * Checks if there is more food data to load
     * @return true if more data is available, false otherwise
     */
    public boolean hasMoreFoodData() {
        return foodRepository.hasMoreData();
    }

    /**
     * Adds an item to the cart
     * @param token Authentication token
     * @param foodId ID of the food item to add
     * @param quantity Quantity to add
     */
    public void addToCart(String token, String foodId, int quantity) {
        cartRepository.addToCart(token, foodId, quantity);
    }

    /**
     * Gets cart loading state LiveData
     * @return LiveData for cart loading state
     */
    public LiveData<Boolean> getCartLoading() {
        return cartRepository.isLoading();
    }

    /**
     * Gets cart response
     * @return LiveData<ApiResponse<Object>>
     */
    public LiveData<ApiResponse<Object>> getCartResponse() {
        return cartRepository.getCartResponse();
    }

    /**
     * Gets cart repository error message LiveData
     * @return LiveData for cart error messages
     */
    public LiveData<String> getCartErrorMessage() {
        return cartRepository.getErrorMessage();
    }
}
