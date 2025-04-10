package android.example.bobo.ui.view;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.ui.viewmodel.FoodDetailViewModel;
import android.example.bobo.utils.TokenManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.example.bobo.R;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class FoodDetailActivity extends AppCompatActivity {
    private static final String TAG = "FoodDetailActivity";

    private ImageView foodImageView;
    private TextView ratingTextView, cookingTimeTextView;
    private TextView foodNameTextView, descriptionTextView, priceTextView;
    private TextView quantityTextView;
    private ImageButton decreaseButton, increaseButton;
    private Button addToCartButton;
    private ProgressBar progressBar;
    private TextView readMoreButton;
    private ImageView shareButton;
    private Dish currentDish;
    private int quantity = 1;

    private FoodDetailViewModel viewModel;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(FoodDetailViewModel.class);

        // Initialize TokenManager
        tokenManager = new TokenManager(this);

        // Initialize views
        initViews();

        // Setup loading state - show loading initially
        showLoading(true);

        // Setup back button
        setupBackButton();

        // Setup click listeners
        setupClickListeners();

        // Observe data changes from ViewModel
        observeData();

        // Get the slug from intent and fetch data
        String foodSlug = getIntent().getStringExtra("FOOD_SLUG");
        if (foodSlug != null && !foodSlug.isEmpty()) {
            Log.d(TAG, "Fetching details for food with slug: " + foodSlug);
            // Fetch detailed information from API
            viewModel.fetchFoodDetailBySlug(foodSlug);
        } else {
            // Handle error - no slug provided
            Toast.makeText(this, "Error: Food information not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.btn_back);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed(); // Go back to previous screen
                }
            });
        }
    }

    private void initViews() {
        foodImageView = findViewById(R.id.iv_food_detail);
        ratingTextView = findViewById(R.id.tv_rating);
        cookingTimeTextView = findViewById(R.id.tv_cooking_time);
        foodNameTextView = findViewById(R.id.tv_food_name);
        descriptionTextView = findViewById(R.id.tv_description);
        priceTextView = findViewById(R.id.tv_price);
        quantityTextView = findViewById(R.id.tv_quantity);
        decreaseButton = findViewById(R.id.btn_decrease);
        increaseButton = findViewById(R.id.btn_increase);
        addToCartButton = findViewById(R.id.btn_add_to_cart);
        readMoreButton = findViewById(R.id.btn_read_more);
        shareButton = findViewById(R.id.btn_share);

        // Add progress bar if not already in layout
        progressBar = findViewById(R.id.progressBar);
        if (progressBar == null) {
            progressBar = new ProgressBar(this);
            progressBar.setId(View.generateViewId());
            progressBar.setVisibility(View.GONE);
        }

        // Set initial quantity
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void displayFoodItemDetails(Dish dish) {
        if (dish == null) return;

        // Store the current dish
        this.currentDish = dish;

        // Set food name
        foodNameTextView.setText(dish.getName());

        // Set food image using Glide
        if (dish.getThumbnail() != null && !dish.getThumbnail().isEmpty()) {
            Glide.with(this)
                    .load(dish.getThumbnail())
                    .placeholder(R.drawable.food_placeholder)
                    .error(R.drawable.food_placeholder)
                    .into(foodImageView);
        } else {
            // Set a default image
            foodImageView.setImageResource(R.drawable.food_placeholder);
        }

        // Set rating
        ratingTextView.setText(String.valueOf(dish.getRating()));

        // Set cooking time
        String cookingTimeText = dish.getPreparationTime() + " mins";
        cookingTimeTextView.setText(cookingTimeText);

        // Get description
        final String description;
        if (dish.getDescription() != null && !dish.getDescription().isEmpty()) {
            description = dish.getDescription();
        } else {
            description = "A delicious food item prepared with the finest ingredients.";
        }

        // Set description and enable truncation
        descriptionTextView.setText(description);
        descriptionTextView.setMaxLines(3);
        descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);

        // Check if description is long enough to need "Read more"
        if (description.length() > 120) { // Adjust this threshold as needed
            readMoreButton.setVisibility(View.VISIBLE);
        } else {
            readMoreButton.setVisibility(View.GONE);
        }

        // Set price (using final price if available)
        updatePriceDisplay(dish.getFinalPrice());
    }

    private void setupClickListeners() {
        // Quantity decrease button
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                    updateTotalPrice();
                }
            }
        });

        // Quantity increase button
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Read more button
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between truncated and full description
                if (descriptionTextView.getMaxLines() == 3) {
                    descriptionTextView.setMaxLines(Integer.MAX_VALUE);
                    readMoreButton.setText("Read less");
                } else {
                    descriptionTextView.setMaxLines(3);
                    readMoreButton.setText("Read more...");
                }
            }
        });

        // Add to cart button
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDish != null) {
                    addItemToCart();
                }
            }
        });

        // Share button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDish != null) {
                    Toast.makeText(FoodDetailActivity.this,
                            "Sharing " + currentDish.getName(),
                            Toast.LENGTH_SHORT).show();
                    // In a real app, you would implement sharing functionality here
                }
            }
        });
    }

    private void addItemToCart() {
        if (currentDish == null) return;

        // Check if user is logged in
        if (!tokenManager.hasToken()) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get token
        String token = tokenManager.getToken();

        // Get food ID and quantity
        String foodId = currentDish.getId();

        // Call API to add to cart
        viewModel.addToCart(token, foodId, quantity);

        // Show loading or temporary message
        Toast.makeText(this, "Đang thêm vào giỏ hàng...", Toast.LENGTH_SHORT).show();
    }

    private void updateTotalPrice() {
        if (currentDish != null) {
            double totalPrice = currentDish.getFinalPrice() * quantity;
            updatePriceDisplay(totalPrice);
        }
    }

    private void updatePriceDisplay(double price) {
        String priceText = "$" + String.format("%.2f", price);
        priceTextView.setText(priceText);
    }

    private void observeData() {
        // Observe food detail
        viewModel.getFoodDetail().observe(this, new Observer<Dish>() {
            @Override
            public void onChanged(Dish dish) {
                if (dish != null) {
                    displayFoodItemDetails(dish);
                }
                showLoading(false);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                showLoading(isLoading);
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Toast.makeText(FoodDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }
        });

        // Observe cart response
        viewModel.getCartResponse().observe(this, new Observer<ApiResponse<Object>>() {
            @Override
            public void onChanged(ApiResponse<Object> response) {
                if (response != null && response.isSuccess()) {
                    Toast.makeText(FoodDetailActivity.this,
                            quantity + " " + currentDish.getName() + " " + response.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observe cart errors
        viewModel.getCartErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Toast.makeText(FoodDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("quantity", quantity);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            quantity = savedInstanceState.getInt("quantity", 1);
            quantityTextView.setText(String.valueOf(quantity));
            updateTotalPrice();
        }
    }
}
