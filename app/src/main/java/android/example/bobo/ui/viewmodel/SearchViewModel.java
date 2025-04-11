package android.example.bobo.ui.viewmodel;

import android.app.Application;
import android.example.bobo.data.model.SearchFoodItemModel;
import android.example.bobo.data.repository.FoodSearchRepository;
import android.example.bobo.data.repository.SearchResourceState;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final FoodSearchRepository searchRepository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private LiveData<SearchResourceState<List<SearchFoodItemModel>>> searchResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchRepository = new FoodSearchRepository();
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
        searchResults = searchRepository.searchFoods(query);
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public LiveData<SearchResourceState<List<SearchFoodItemModel>>> getSearchResults() {
        return searchResults;
    }

    public boolean hasSearchResults() {
        return searchResults != null;
    }
}
