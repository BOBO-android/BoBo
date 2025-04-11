package android.example.bobo.ui.view.Fragments;

import android.example.bobo.R;
import android.example.bobo.data.model.SearchFoodItemModel;
import android.example.bobo.data.repository.CartRepository;
import android.example.bobo.data.repository.SearchResourceState;
import android.example.bobo.ui.adapters.FoodExplorerCategoryAdapter;
import android.example.bobo.ui.adapters.FoodExplorerSearchAdapter;
import android.example.bobo.utils.NetworkUtils;
import android.example.bobo.utils.TokenManager;
import android.example.bobo.ui.viewmodel.FoodExploreViewModel;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ExploreFragment extends Fragment {

    private FoodExploreViewModel viewModel;
    private TokenManager tokenManager;

    private EditText searchEditText;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView searchResultsRecyclerView;
    private ProgressBar progressBar;
    private TextView noResultsTextView;
    private TextView categoryHeaderTextView;
    private TextView searchResultsHeaderTextView;

    private CartRepository cartRepository;

    private FoodExplorerCategoryAdapter categoryAdapter;
    private FoodExplorerSearchAdapter foodAdapter;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FoodExploreViewModel.class);
        tokenManager = new TokenManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        initViews(view);

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup search
        setupSearch();

        // Observe search query text changes
        observeSearchQueryText();
    }

    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.explorer_edit_search);
        categoriesRecyclerView = view.findViewById(R.id.explorer_recycler_categories);
        searchResultsRecyclerView = view.findViewById(R.id.explorer_recycler_search_results);
        progressBar = view.findViewById(R.id.explorer_progress_bar);
        noResultsTextView = view.findViewById(R.id.explorer_text_no_results);
        categoryHeaderTextView = view.findViewById(R.id.explorer_text_category_header);
        searchResultsHeaderTextView = view.findViewById(R.id.explorer_text_search_results_header);
    }

    private void setupRecyclerViews() {
        // Setup category recycler view
        categoryAdapter = new FoodExplorerCategoryAdapter(requireContext());
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        categoriesRecyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnFoodExplorerCategoryClickListener(category -> {
            // Handle category click
            Toast.makeText(requireContext(), "Selected: " + category.getName(), Toast.LENGTH_SHORT).show();
            // Search using the category name
            searchEditText.setText(category.getName());
            performSearch();
        });

        // Setup food search results recycler view
        foodAdapter = new FoodExplorerSearchAdapter(requireContext());
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchResultsRecyclerView.setAdapter(foodAdapter);

        foodAdapter.setOnSearchFoodClickListener(food -> {
            // Handle food item click
            Toast.makeText(requireContext(), "Selected: " + food.getName(), Toast.LENGTH_SHORT).show();
        });

        foodAdapter.setOnAddToCartClickListener(food -> {
            if (tokenManager.hasToken()) {
                cartRepository = new CartRepository();
                cartRepository.addToCart(tokenManager.getToken(),food.getId(),1);
                // Add to cart
                Snackbar.make(requireView(),
                        "Added " + food.getName() + " to cart",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                // Prompt to login
                Snackbar.make(requireView(),
                                "Please login to add items to cart",
                                Snackbar.LENGTH_LONG)
                        .setAction("Login", v -> {
                            // Navigate to login screen
                            // TODO: implement navigation to login screen
                        })
                        .show();
            }
        });
    }

    private void setupSearch() {
        // Handle search on keyboard action
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });

        // Clear button functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    // Clear search and show categories
                    showCategories();
                }
            }
        });
    }

    private void observeSearchQueryText() {
        viewModel.getSearchQueryText().observe(getViewLifecycleOwner(), query -> {
            if (query != null && !query.isEmpty()) {
                searchResultsHeaderTextView.setText("Search results for \"" + query + "\"");
            }
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();

        if (query.isEmpty()) {
            // Show categories, hide search results
            showCategories();
            return;
        }

        // Check network connectivity first
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Snackbar.make(requireView(), "No internet connection available", Snackbar.LENGTH_LONG)
                    .setAction("Retry", v -> performSearch())
                    .show();
            return;
        }

        // Update ViewModel
        viewModel.setSearchQueryText(query);

        // Show loading
        showLoading();

        // First, try to search foods by query (gives multiple results)
        viewModel.searchFoods(query).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        // Show search results
                        showSearchResults(resource.getData());
                    } else {
                        // If no foods found by query, try searching by slug (exact match)
                        searchBySlug(query);
                    }
                    break;
                case ERROR:
                    // If error, try searching by slug
                    searchBySlug(query);
                    break;
            }
        });
    }

    private void searchBySlug(String slug) {
        viewModel.searchFoodBySlug(slug).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        // Show a single search result
                        showSingleSearchResult(resource.getData());
                    } else {
                        // Show no results
                        showNoResults();
                    }
                    break;
                case ERROR:
                    hideLoading();
                    showNoResults();
                    // Show error message
                    Snackbar.make(requireView(),
                            "Error fetching data: " + resource.getMessage(),
                            Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        categoryHeaderTextView.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.GONE);
        searchResultsHeaderTextView.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showCategories() {
        categoryHeaderTextView.setVisibility(View.VISIBLE);
        categoriesRecyclerView.setVisibility(View.VISIBLE);
        searchResultsHeaderTextView.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.GONE);
    }

    private void showSearchResults(List<SearchFoodItemModel> foods) {
        categoryHeaderTextView.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.GONE);
        searchResultsHeaderTextView.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        noResultsTextView.setVisibility(View.GONE);

        // Clear previous results
        foodAdapter.clearList();

        // Add all foods found
        foodAdapter.setFoodList(foods);
    }

    private void showSingleSearchResult(SearchFoodItemModel food) {
        categoryHeaderTextView.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.GONE);
        searchResultsHeaderTextView.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        noResultsTextView.setVisibility(View.GONE);

        // Clear previous results
        foodAdapter.clearList();

        // Add the found food
        foodAdapter.addFood(food);
    }

    private void showNoResults() {
        categoryHeaderTextView.setVisibility(View.GONE);
        categoriesRecyclerView.setVisibility(View.GONE);
        searchResultsHeaderTextView.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.VISIBLE);
    }
}