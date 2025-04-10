package android.example.bobo.ui.view.Fragments;

import android.content.Intent;
import android.example.bobo.R;
import android.example.bobo.data.model.BaseUserProfile;
import android.example.bobo.data.model.PromotionItem;
import android.example.bobo.data.model.UserFoodItem;
import android.example.bobo.databinding.FragmentHomeBinding;
import android.example.bobo.ui.adapters.PromotionAdapter;
import android.example.bobo.ui.adapters.UserFoodAdapter;
import android.example.bobo.ui.view.FoodDetailActivity;
import android.example.bobo.ui.viewmodel.HomeViewModel;
import android.example.bobo.utils.TokenManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final int ITEMS_PER_PAGE = 10;

    // View binding
    private FragmentHomeBinding binding;

    // ViewModel
    private HomeViewModel viewModel;

    // Adapters
    private UserFoodAdapter foodAdapter;
    private List<UserFoodItem> foodItems = new ArrayList<>();

    // Utilities
    private TokenManager tokenManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout using view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel using ViewModelProvider
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Initialize TokenManager
        tokenManager = new TokenManager(requireContext());

        // Setup UI components
        setupUI();

        // Observe data changes
        observeData();

        // Load initial data
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }

    /**
     * Sets up all UI components
     */
    private void setupUI() {
        setupCategoryTabs();
        setupFoodRecyclerView();
        setupPromotionBanners();
    }

    /**
     * Loads initial data
     */
    private void loadData() {
        // Fetch user profile if token exists
        if (tokenManager.hasToken()) {
            String token = tokenManager.getToken();
            Log.d(TAG, "Fetching user profile with token: " + token);
            viewModel.fetchUserProfile(token);
        } else {
            // No token available, show guest greeting
            Log.e(TAG, "No token available to fetch user profile");
            binding.tvGreeting.setText("Hi Guest");
        }

        // Load food data
        viewModel.loadFoods(ITEMS_PER_PAGE);
    }

    /**
     * Sets up promotion banners in ViewPager
     */
    private void setupPromotionBanners() {
        // Create sample data for promotions
        List<PromotionItem> promotions = createPromotionItems();

        // Create and set up adapter for ViewPager
        PromotionAdapter promotionAdapter = new PromotionAdapter(promotions,
                promotion -> {
                    // Handle promotion click
                    Toast.makeText(
                            requireContext(),
                            "Promotion: " + promotion.getTitle() + " " + promotion.getSubtitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        // Set up ViewPager
        binding.viewpagerPromo.setAdapter(promotionAdapter);
        binding.viewpagerPromo.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // Add page transition effect
        binding.viewpagerPromo.setPageTransformer(new PromotionPageTransformer());
    }

    /**
     * Creates promotional items for the carousel
     */
    private List<PromotionItem> createPromotionItems() {
        List<PromotionItem> promotions = new ArrayList<>();

        promotions.add(new PromotionItem(
                1,
                "35% OFF on",
                "Burgers!",
                "Buy now",
                R.drawable.burger_exploded,
                Color.parseColor("#F2F7E8")
        ));

        promotions.add(new PromotionItem(
                2,
                "20% OFF on",
                "Pizza!",
                "Order now",
                R.drawable.pizza_promo,
                Color.parseColor("#FFF3E0")
        ));

        return promotions;
    }

    /**
     * Sets up category tabs with click listeners
     */
    private void setupCategoryTabs() {
        // Set up click listener for the tabs
        View.OnClickListener tabClickListener = this::handleCategoryTabClick;

        binding.tabOffers.setOnClickListener(tabClickListener);
        binding.tabBurger.setOnClickListener(tabClickListener);
        binding.tabPizza.setOnClickListener(tabClickListener);
        binding.tabDonuts.setOnClickListener(tabClickListener);

        // Set offers as default selected tab
        binding.tabOffers.performClick();
    }

    /**
     * Handles tab click events and updates UI
     */
    private void handleCategoryTabClick(View v) {
        updateSelectedTab(v);

        // Update category based on selected tab
        int viewId = v.getId();
        if (viewId == R.id.tab_offers) {
            viewModel.setCategory("offered");
        } else if (viewId == R.id.tab_burger) {
            viewModel.setCategory("burger");
        } else if (viewId == R.id.tab_pizza) {
            viewModel.setCategory("pizza");
        } else if (viewId == R.id.tab_donuts) {
            viewModel.setCategory("donut");
        }
    }

    /**
     * Updates visual state of selected tab
     */
    private void updateSelectedTab(View selectedTab) {
        // Reset all tabs to normal background
        binding.tabOffers.setBackground(getResources().getDrawable(R.drawable.category_normal_bg, null));
        binding.tabBurger.setBackground(getResources().getDrawable(R.drawable.category_normal_bg, null));
        binding.tabPizza.setBackground(getResources().getDrawable(R.drawable.category_normal_bg, null));
        binding.tabDonuts.setBackground(getResources().getDrawable(R.drawable.category_normal_bg, null));

        // Set background for the selected tab
        selectedTab.setBackground(getResources().getDrawable(R.drawable.category_selected_bg, null));
    }

    /**
     * Sets up food RecyclerView with adapter and pagination
     */
    private void setupFoodRecyclerView() {
        // Set up layout manager (Grid or Linear)
        int spanCount = 2; // Number of columns in the grid
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        binding.rvPopularFoods.setLayoutManager(layoutManager);

        // Set up adapter
        foodAdapter = new UserFoodAdapter(getContext(), foodItems, new UserFoodAdapter.OnFoodItemClickListener() {
            @Override
            public void onFoodItemClick(UserFoodItem foodItem) {
                navigateToFoodDetail(foodItem);
            }

            @Override
            public void onAddButtonClick(UserFoodItem foodItem) {
                // Default quantity - có thể thay đổi hoặc lấy từ UI nếu cần
                int quantity = 1;

                // Lấy token từ TokenManager
                if (tokenManager.hasToken()) {
                    String token = tokenManager.getToken();

                    // Gọi ViewModel để thêm vào giỏ hàng
                    viewModel.addToCart(token, foodItem.getId(), quantity);

                    // Toast tạm thời để thông báo cho người dùng
                    Toast.makeText(getContext(), "Adding to cart...", Toast.LENGTH_SHORT).show();
                } else {
                    // Người dùng chưa đăng nhập
                    Toast.makeText(getContext(), "Please login to add to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.rvPopularFoods.setAdapter(foodAdapter);

        // Add ItemDecoration to create spacing between items
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        binding.rvPopularFoods.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));

        // Set up scroll listener for pagination
        binding.rvPopularFoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if we need to load more items
                if (viewModel.hasMoreFoodData() &&
                        viewModel.getIsLoading().getValue() != null &&
                        !viewModel.getIsLoading().getValue()) {

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    // If we're close to the end of the list, load more items
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5
                            && firstVisibleItemPosition >= 0) {
                        viewModel.loadMoreFoods(ITEMS_PER_PAGE);
                    }
                }
            }
        });
    }

    /**
     * Navigates to food detail screen
     */
    private void navigateToFoodDetail(UserFoodItem foodItem) {
        // Create an intent to start the FoodDetailActivity
        Intent intent = new Intent(getContext(), FoodDetailActivity.class);

        // Store the slug for the FoodDetailActivity to use to fetch the detailed data
        intent.putExtra("FOOD_SLUG", foodItem.getSlug());

        // Start the activity
        startActivity(intent);
    }

    /**
     * Sets up observers for all LiveData objects
     */
    private void observeData() {
        // Observe food items
        viewModel.getFoods().observe(getViewLifecycleOwner(), items -> {
            foodItems.clear();
            if (items != null) {
                foodItems.addAll(items);
            }
            foodAdapter.notifyDataSetChanged();
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // You can show/hide a loading indicator here if needed
            // For example:
            // binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe food error messages
        viewModel.getFoodErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe user profile data
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                updateUIWithProfile(profile);
            }
        });

        // Observe user profile error messages
        viewModel.getUserErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Log.e(TAG, "User profile error: " + error);
                // We might not want to show this error to user directly
                // Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe add to cart button
        viewModel.getCartResponse().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates UI with user profile data
     */
    private void updateUIWithProfile(BaseUserProfile profile) {
        // Update greeting text with user's name
        if (profile.getFullName() != null && !profile.getFullName().isEmpty()) {
            binding.tvGreeting.setText("Hi " + profile.getFullName());
        } else if (profile.getUsername() != null && !profile.getUsername().isEmpty()) {
            binding.tvGreeting.setText("Hi " + profile.getUsername());
        }

        // Update profile image if available
        if (profile.getImage() != null && !profile.getImage().isEmpty()) {
            Glide.with(this)
                    .load(profile.getImage())
                    .placeholder(R.drawable.avatars)
                    .error(R.drawable.avatars)
                    .circleCrop()
                    .into(binding.ivProfile);
        }
    }

    /**
     * Custom page transformer for promotion ViewPager
     */
    private static class PromotionPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // Page outside the screen on the left
                page.setAlpha(0f);
            } else if (position <= 1) { // [-1,1]
                // Modify the default page transition effect to scale down the page
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageWidth * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale down the page (between MIN_SCALE and 1)
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                // Fade the page proportional to its size
                page.setAlpha(MIN_SCALE + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_SCALE));
            } else { // (1,+Infinity]
                // Page outside the screen on the right
                page.setAlpha(0f);
            }
        }
    }

    /**
     * Grid spacing decoration for RecyclerView
     */
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
