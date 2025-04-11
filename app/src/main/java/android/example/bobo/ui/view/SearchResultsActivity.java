package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.example.bobo.data.model.SearchFoodItemModel;
import android.example.bobo.data.repository.CartRepository;
import android.example.bobo.data.repository.SearchResourceState;
import android.example.bobo.databinding.ActivitySearchResultsBinding;
import android.example.bobo.ui.adapters.SearchFoodAdapter;
import android.example.bobo.ui.viewmodel.SearchViewModel;
import android.example.bobo.utils.TokenManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements SearchFoodAdapter.OnFoodItemClickListener {

    private ActivitySearchResultsBinding binding;
    private SearchViewModel viewModel;
    private SearchFoodAdapter adapter;
    private List<SearchFoodItemModel> foodItems = new ArrayList<>();
    private TokenManager tokenManager;
    private CartRepository cartRepository;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy query từ intent
        if (getIntent().hasExtra("SEARCH_QUERY")) {
            searchQuery = getIntent().getStringExtra("SEARCH_QUERY");
        } else {
            // Không có query, đóng activity
            finish();
            return;
        }

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tokenManager = new TokenManager(this);
        cartRepository = new CartRepository();

        // Setup UI
        setupUI();

        // Thực hiện tìm kiếm
        viewModel.setSearchQuery(searchQuery);

        // Observe search results
        observeSearchResults();
    }

    private void setupUI() {
        // Set up toolbar title
        binding.tvSearchQuery.setText("Search results for \"" + searchQuery + "\"");

        // Set up back button
        binding.btnBack.setOnClickListener(v -> finish());

        // Set up RecyclerView
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchFoodAdapter(this, foodItems, this);
        binding.rvSearchResults.setAdapter(adapter);
    }

    private void observeSearchResults() {
        viewModel.getSearchResults().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        showResults(resource.getData());
                    } else {
                        showNoResults();
                    }
                    break;

                case ERROR:
                    hideLoading();
                    showError(resource.getMessage());
                    break;
            }
        });
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvSearchResults.setVisibility(View.GONE);
        binding.tvNoResults.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showResults(List<SearchFoodItemModel> foods) {
        binding.rvSearchResults.setVisibility(View.VISIBLE);
        binding.tvNoResults.setVisibility(View.GONE);

        foodItems.clear();
        foodItems.addAll(foods);
        adapter.notifyDataSetChanged();
    }

    private void showNoResults() {
        binding.rvSearchResults.setVisibility(View.GONE);
        binding.tvNoResults.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        binding.tvNoResults.setText("Error loading results");
        binding.tvNoResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFoodItemClick(SearchFoodItemModel food) {
        // Navigate to food details
        if (food.getSlug() != null) {
            Intent intent = new Intent(this, FoodDetailActivity.class);
            intent.putExtra("FOOD_SLUG", food.getSlug());
            startActivity(intent);
        }
    }

    @Override
    public void onAddToCartClick(SearchFoodItemModel food) {
        if (tokenManager.hasToken()) {
            String token = tokenManager.getToken();
            cartRepository.addToCart(token, food.getId(), 1);
            Snackbar.make(binding.getRoot(), "Added " + food.getName() + " to cart", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(binding.getRoot(), "Please login to add items to cart", Snackbar.LENGTH_LONG)
                    .setAction("Login", v -> {
                        // Navigate to login screen
                        // TODO: Implement navigation to login screen
                    })
                    .show();
        }
    }
}
