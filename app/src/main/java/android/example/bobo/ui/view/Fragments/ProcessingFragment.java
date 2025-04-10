package android.example.bobo.ui.view.Fragments;

import android.example.bobo.data.model.FoodItem;
import android.example.bobo.data.model.Order;
import android.example.bobo.data.repository.StoreOrderRepository;
import android.example.bobo.ui.adapters.OrdersPendingApprovalAdapter;
import android.example.bobo.ui.adapters.OrdersProcessingAdapter;
import android.example.bobo.ui.view.OrdersActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.bobo.R;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProcessingFragment extends Fragment {
    private static final String TAG = "ProcessingFragment";
    private RecyclerView recyclerView;
    private OrdersProcessingAdapter ordersProcessingAdapter;
    private List<Order> orderList = new ArrayList<>();
    private OrdersActivity ordersActivity; // Reference to OrdersActivity
    private StoreOrderRepository storeOrderRepository;
    private ProgressBar progressBar;
    private View emptyStateContainer;
    private final String token;
    private final String storeId;

    // Status for processing orders
    private static final String STATUS_PROCESSING = "processing";

    public ProcessingFragment(OrdersActivity ordersActivity, String token, String storeId) {
        this.ordersActivity = ordersActivity;
        this.token = token;
        this.storeId = storeId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_processing, container, false);

        // Initialize views
        recyclerView = mainView.findViewById(R.id.recyclerViewProcessingOrders);
        progressBar = mainView.findViewById(R.id.progressBar);
        emptyStateContainer = mainView.findViewById(R.id.emptyStateContainer);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize repository
        storeOrderRepository = new StoreOrderRepository();

        // Initialize adapter with empty list
        ordersProcessingAdapter = new OrdersProcessingAdapter(
                getContext(),
                orderList,
                this::showBottomSheet
        );

        recyclerView.setAdapter(ordersProcessingAdapter);

        // Load data
        loadProcessingOrders();

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        loadProcessingOrders();
    }

    /**
     * Load processing orders from the server
     */
    private void loadProcessingOrders() {
        showLoading(true);

        // Create LiveData object to observe
        MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();

        // Observe the LiveData for changes
        ordersLiveData.observe(getViewLifecycleOwner(), orders -> {
            showLoading(false);

            if (orders != null && !orders.isEmpty()) {
                // Update the adapter with new data
                orderList.clear();
                orderList.addAll(orders);
                ordersProcessingAdapter.notifyDataSetChanged();
                showEmptyState(false);

                Log.d(TAG, "Loaded " + orders.size() + " processing orders");
            } else {
                // Show empty state
                showEmptyState(true);
                Log.d(TAG, "No processing orders found");
            }
        });

        // Fetch data from repository
        storeOrderRepository.getStoreOrdersByStatus(storeId, STATUS_PROCESSING, ordersLiveData, token);
    }

    /**
     * Show loading indicator
     */
    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }

        if (recyclerView != null) {
            recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }

        // Hide empty state during loading
        if (isLoading && emptyStateContainer != null) {
            emptyStateContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Show empty state message
     */
    private void showEmptyState(boolean isEmpty) {
        if (emptyStateContainer != null) {
            emptyStateContainer.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }

        if (recyclerView != null) {
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
    }

    private void showBottomSheet(Order order) {
        if (ordersActivity == null) return;

        ordersActivity.zoomOutOrdersActivity(); // OrdersActivity zooms out

        // Get food items from the order
        List<FoodItem> foodItems = order.getFoodItems() != null ? order.getFoodItems() : new ArrayList<>();

        // If there are no food items, add a placeholder
        if (foodItems.isEmpty()) {
            foodItems.add(new FoodItem("No items available", 0.0, R.drawable.placeholder));
        }

        // Format date for display
        String formattedTime = formatDate(order.getOrderTime());

        // Build and show the bottom sheet
        OrderProcessingBottomSheetFragment bottomSheet = new OrderProcessingBottomSheetFragment.Builder()
                .setStoreId(storeId)  // Add storeId
                .setToken(token)      // Add token
                .setCustomerName(order.getUserName())
                .setCustomerType("Customer")
                .setCustomerImage(R.drawable.avatars) // Use default image or load from URL if available
                .setDeliveryAddress(order.getDeliverTo() != null ? order.getDeliverTo() : "N/A")
                .setAmountPaid(order.getFormattedPrice())
                .setEstimatedTime(formattedTime)
                .setPreparationTime("20mins") // Default preparation time
                .setFoodItems(foodItems)
                .setOrderId(order.getOrderId())
                .setOnDismissCallback(() -> {
                    ordersActivity.restoreOrdersActivity(); // Restore activity on dismiss
                })
                .setOnUpdateCallback(() -> {
                    // Handle update button click
                    updateOrderStatus(order);
                })
                .build();

        bottomSheet.show(getParentFragmentManager(), "orderProcessingSheet");
    }

    /**
     * Format date for display
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    private void updateOrderStatus(Order order) {
        // For now, just show a message
        if (getContext() != null) {
            String message = "Order #" + order.getOrderId() + " status updated";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            // Refresh the data to show updated status
            loadProcessingOrders();
        }
    }
}
