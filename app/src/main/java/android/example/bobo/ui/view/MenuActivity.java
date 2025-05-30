package android.example.bobo.ui.view;

import android.example.bobo.R;
import android.example.bobo.data.model.Dish;
import android.example.bobo.ui.adapters.DishAdapter;
import android.example.bobo.ui.viewmodel.MenuViewModel;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements DishAdapter.OnDishClickListener {

    private RecyclerView recyclerView;
    private DishAdapter adapter;
    Button btnAddDish;
    ImageView btnBack;
    private ProgressBar progressBar;
    private MenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddDish = findViewById(R.id.btn_add_dish);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);

        // Initialize ViewModel
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DishAdapter(this, new ArrayList<>(), this); // Initialize with empty list
        recyclerView.setAdapter(adapter);

        // Observe LiveData for loading state
        menuViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe LiveData for dishes
        menuViewModel.getDishes().observe(this, dishes -> {
            adapter.setDishList(dishes);
        });

        // Observe LiveData for error messages
        menuViewModel.getErrorMessage().observe(this, errorMsg -> {
            Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
        });

        // Initial data fetching
        String token = "YOUR_AUTH_TOKEN"; // Replace with your actual token
        String storeId = "YOUR_STORE_ID"; // Replace with your actual store ID
        menuViewModel.fetchDishes(storeId, 1, 10, token); // Fetch initial data

        btnBack.setOnClickListener(v -> finish());

        btnAddDish.setOnClickListener(v -> {
            // Handle adding a new dish
            Toast.makeText(this, "Add Dish Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onEditClicked(Dish dish) {
        Toast.makeText(this, "Edit: " + dish.getName(), Toast.LENGTH_SHORT).show();
        // Implement edit functionality
    }

    @Override
    public void onDeleteClicked(Dish dish) {
        Toast.makeText(this, "Delete: " + dish.getId(), Toast.LENGTH_SHORT).show();
        // Implement delete functionality
    }

    @Override
    public void onMarkAsOutOfStock(Dish dish) {
        Toast.makeText(this, "Out of Stock: " + dish.getId(), Toast.LENGTH_SHORT).show();
        // Implement out of stock functionality
    }
}