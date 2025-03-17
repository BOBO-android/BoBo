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
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerView);
        Button btnAddDish = findViewById(R.id.btn_add_dish);
        ImageView btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);

        // Initialize ViewModel
        MenuViewModel menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Show ProgressBar while fetching data
        progressBar.setVisibility(View.VISIBLE);

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImxvdmFuYmFuZ2JveDk5QGdtYWlsLmNvbSIsInN1YiI6IjY3ZDc3NTdhMTA2ODdkNzQzNDkyNjk1NCIsImlhdCI6MTc0MjIyNTI2OSwiZXhwIjoxNzQyMjI3MDY5fQ.3fJvCeBSZyz3SoXPkkqIjdrTZSLSke80vfrsYNhde6w";

        // Call this to fetch data
        menuViewModel.fetchDishes("67d775af10687d743492695a", 1, 10, token);

        // Observe LiveData for dishes
        menuViewModel.getDishes().observe(this, dishes -> {
            progressBar.setVisibility(View.GONE); // Hide progress bar
            if (dishes != null && !dishes.isEmpty()) {
                adapter = new DishAdapter(this, dishes, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No dishes found", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe LiveData for loading state
        menuViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        // Observe LiveData for error messages
        menuViewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnAddDish.setOnClickListener(v -> {
            // Handle adding a new dish
            Toast.makeText(this, "Add Dish Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onEditClicked(Dish dish) {
        Toast.makeText(this, "Edit: " + dish.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClicked(Dish dish) {
        Toast.makeText(this, "Delete: " + dish.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkAsOutOfStock(Dish dish) {
        Toast.makeText(this, "Out of Stock: " + dish.getId(), Toast.LENGTH_SHORT).show();
    }
}