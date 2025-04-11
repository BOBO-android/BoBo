package android.example.bobo.ui.view.Fragments;

import android.example.bobo.R;
import android.example.bobo.data.model.UserOrder;
import android.example.bobo.ui.adapters.UserOrderAdapter;
import android.example.bobo.ui.viewmodel.UserOrderViewModel;
import android.example.bobo.utils.TokenManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersFragment extends Fragment implements UserOrderAdapter.OnUserOrderClickListener {

    private static final String TAG = "OrderFragment";

    private UserOrderViewModel viewModel;
    private RecyclerView recyclerViewUserOrders;
    private UserOrderAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvError;
    private TokenManager tokenManager;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize TokenManager
        tokenManager = new TokenManager(requireContext());

        // Initialize views
        recyclerViewUserOrders = view.findViewById(R.id.recyclerViewUserOrders);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);

        // Setup RecyclerView
        recyclerViewUserOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UserOrderAdapter(requireContext(), this);
        recyclerViewUserOrders.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(UserOrderViewModel.class);

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                tvError.setText(errorMsg);
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvError.setVisibility(View.GONE);
            }
        });

        // Load user orders
        loadUserOrders();
    }

    private void loadUserOrders() {
        // Get user token and ID from TokenManager
        String token = tokenManager.getToken();
        String userId = tokenManager.getUserId();

        // Check if we have user credentials
        if (!tokenManager.hasToken()) {
            tvError.setText("User not logged in");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        // For testing, you can use hardcoded ID that matches your API documentation
        // Uncomment line below if needed for testing
        // String testUserId = "67f6b2b9fd2761287eb311ac"; // Use the ID from your screenshot

        // Load orders from ViewModel using the user's ID
        viewModel.getUserOrders(token, userId).observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<UserOrder> userOrders) {
        if (userOrders != null && !userOrders.isEmpty()) {
            adapter.updateUserOrders(userOrders);
            recyclerViewUserOrders.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        } else {
            recyclerViewUserOrders.setVisibility(View.GONE);
            tvError.setText("No orders found");
            tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrackOrderClick(UserOrder userOrder) {
        // Handle the track order click
        Toast.makeText(requireContext(), "Tracking order: " + userOrder.getId(), Toast.LENGTH_SHORT).show();

        // Here you could navigate to an order tracking screen
        // For example:
        // NavDirections action = OrderFragmentDirections.actionOrderFragmentToOrderTrackingFragment(userOrder.getId());
        // Navigation.findNavController(requireView()).navigate(action);
    }
}