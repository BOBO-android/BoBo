package android.example.bobo.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.example.bobo.data.model.SearchFoodItemModel;
import android.example.bobo.data.repository.FoodSearchRepository;
import android.example.bobo.data.repository.SearchResourceState;
import android.example.bobo.utils.TokenManager;

import java.util.List;

public class FoodExploreViewModel extends AndroidViewModel {
    private final FoodSearchRepository foodSearchRepository;
    private final TokenManager tokenManager;
    private final MutableLiveData<String> searchQueryText = new MutableLiveData<>();

    public FoodExploreViewModel(@NonNull Application application) {
        super(application);
        foodSearchRepository = new FoodSearchRepository();
        tokenManager = new TokenManager(application);
    }

    public LiveData<String> getSearchQueryText() {
        return searchQueryText;
    }

    public void setSearchQueryText(String query) {
        searchQueryText.setValue(query);
    }

    // Search for a single food item by slug
    public LiveData<SearchResourceState<SearchFoodItemModel>> searchFoodBySlug(String slug) {
        return foodSearchRepository.getFoodBySlug(slug);
    }

    // Search for multiple food items by query
    public LiveData<SearchResourceState<List<SearchFoodItemModel>>> searchFoods(String query) {
        return foodSearchRepository.searchFoods(query);
    }

    public boolean isUserLoggedIn() {
        return tokenManager.hasToken();
    }
}