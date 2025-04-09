package android.example.bobo.ui.view.Fragments;

import android.example.bobo.data.model.FoodItem;
import android.example.bobo.data.model.Order;
import android.example.bobo.data.repository.StoreOrderRepository;
import android.example.bobo.ui.adapters.OrdersPendingApprovalAdapter;
import android.example.bobo.ui.view.OrdersActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.example.bobo.R;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PendingApprovalFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersPendingApprovalAdapter ordersPendingApprovalAdapter;
    private List<Order> orderList = new ArrayList<>();
    private OrdersActivity ordersActivity; // Reference to OrdersActivity
    private StoreOrderRepository storeOrderRepository;
    private ProgressBar progressBar;
    private View emptyStateContainer;
    private final String token;
    private final String storeId;

    // Status for pending approval orders
    private static final String STATUS_PENDING = "pending";

    public PendingApprovalFragment(OrdersActivity ordersActivity, String token, String storeId) {
        this.ordersActivity = ordersActivity;
        this.token = token;
        this.storeId = storeId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_pending_approval, container, false);

        // Initialize views
        recyclerView = mainView.findViewById(R.id.recyclerViewPendingOrders);
        progressBar = mainView.findViewById(R.id.progressBar);
        emptyStateContainer = mainView.findViewById(R.id.emptyStateContainer);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize repository
        storeOrderRepository = new StoreOrderRepository();

        // Initialize adapter with empty list
        ordersPendingApprovalAdapter = new OrdersPendingApprovalAdapter(
                getContext(),
                orderList,
                this::showOrderDetails
        );

        recyclerView.setAdapter(ordersPendingApprovalAdapter);

        // Load data
        loadPendingOrders();

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        loadPendingOrders();
    }

    /**
     * Load pending orders from the server
     */
    private void loadPendingOrders() {
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
                ordersPendingApprovalAdapter.notifyDataSetChanged();
                showEmptyState(false);
            } else {
                // Show empty state
                showEmptyState(true);
            }
        });

        // Fetch data from repository
        storeOrderRepository.getStoreOrdersByStatus(storeId, STATUS_PENDING, ordersLiveData, token);
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

    /**
     * Handle order click to show details
     */
    private void showOrderDetails(Order order) {
        if (order.getFoodItems() == null || order.getFoodItems().isEmpty()) {
            Toast.makeText(getContext(), "No items in this order", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show bottom sheet with order details
        showBottomSheet(order);
    }

    /**
     * Format date for display
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Show order details in bottom sheet
     */
    private void showBottomSheet(Order order) {
        if (ordersActivity == null) return;

        ordersActivity.zoomOutOrdersActivity(); // OrdersActivity zooms out

        OrderPendingBottomSheetFragment bottomSheet = new OrderPendingBottomSheetFragment(
                storeId,                     // Store ID
                order.getOrderId(),           // Order ID
                order.getUserName(),          // Customer name
                order.getDeliverTo(),         // Delivery address
                order.getFormattedPrice(),    // Amount paid
                order.getFoodItems(),         // Food items
                token,                        // Auth token
                () -> {
                    ordersActivity.restoreOrdersActivity(); // Restore activity on dismiss
                    // Refresh the list after dismissing the bottom sheet
                    loadPendingOrders();
                }
        );

        bottomSheet.show(getParentFragmentManager(), "OrderBottomSheet");
    }
}
