package android.example.bobo.ui.view;

import android.example.bobo.R;
import android.example.bobo.data.model.TrackingOrderModel;
import android.example.bobo.ui.adapters.TrackingFoodItemAdapter;
import android.example.bobo.ui.viewmodel.TrackingViewModel;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TrackingActivity extends AppCompatActivity {

    private TrackingViewModel viewModel;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private View bottomSheetView;

    // UI Elements
    private ImageView ivDeliveryPerson;
    private TextView tvDeliveryPersonName;
    private TextView tvEstimatedTime;
    private TextView tvStatus;
    private TextView tvStatusDetail;
    private TextView tvTimeRemaining;
    private TextView tvOrderOnWay;
    private TextView tvOrderDelivered;
    private TextView tvDeliverTo;
    private TextView tvAmountPaid;
    private RecyclerView rvFoodItems;

    private TrackingFoodItemAdapter foodItemAdapter;

    // Constant for customer ID
    private static final String CUSTOMER_ID = "67f6b2b9fd2761287eb311ac";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(TrackingViewModel.class);

        // Initialize Bottom Sheet
        bottomSheetView = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Initialize UI Elements
        Button btnTrackOrder = findViewById(R.id.btn_track_order);
        ivDeliveryPerson = bottomSheetView.findViewById(R.id.iv_delivery_person);
        tvDeliveryPersonName = bottomSheetView.findViewById(R.id.tv_delivery_person_name);
        tvEstimatedTime = bottomSheetView.findViewById(R.id.tv_estimated_time);
        tvStatus = bottomSheetView.findViewById(R.id.tv_status);
        tvStatusDetail = bottomSheetView.findViewById(R.id.tv_status_detail);
        tvTimeRemaining = bottomSheetView.findViewById(R.id.tv_time_remaining);
        tvOrderOnWay = bottomSheetView.findViewById(R.id.tv_order_on_way);
        tvOrderDelivered = bottomSheetView.findViewById(R.id.tv_order_delivered);
        tvDeliverTo = bottomSheetView.findViewById(R.id.tv_deliver_to);
        tvAmountPaid = bottomSheetView.findViewById(R.id.tv_amount_paid);
        rvFoodItems = bottomSheetView.findViewById(R.id.rv_food_items);

        ImageView ivClose = bottomSheetView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));

        // Set click listener for track order button
        btnTrackOrder.setOnClickListener(v -> {
            // Load order data when button is clicked
            viewModel.loadOrdersByCustomerId(CUSTOMER_ID);
        });

        // Observe LiveData
        observeViewModel();
    }

    private void observeViewModel() {
        // Observe loading state
        viewModel.getIsLoading().observe(this, isLoading -> {
            // Show loading indicator if needed
            // For example: progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Observe orders data
        viewModel.getOrders().observe(this, orders -> {
            if (orders != null && !orders.isEmpty()) {
                // We have order data, update UI and show bottom sheet
                updateBottomSheetWithOrder(orders.get(0)); // Assuming we're showing the first order
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void updateBottomSheetWithOrder(TrackingOrderModel order) {
        // Update delivery person
        tvDeliveryPersonName.setText(order.getUserName());
        if (order.getUserImageUrl() != null && !order.getUserImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(order.getUserImageUrl())
                    .placeholder(R.drawable.delivery_person)
                    .error(R.drawable.delivery_person)
                    .into(ivDeliveryPerson);
        }

        // Update estimated time (assuming 30 mins as default from your screenshot)
        tvEstimatedTime.setText("30mins");

        // Update order status
        String status = order.getStatus();
        switch (status) {
            case "PENDING":
                tvStatus.setText("Preparing your order");
                tvStatusDetail.setText("We're preparing your food with magic and care");
                tvTimeRemaining.setText("Time left: 30mins");
                tvOrderOnWay.setTextColor(getResources().getColor(R.color.light_gray));
                tvOrderDelivered.setTextColor(getResources().getColor(R.color.light_gray));
                break;
            case "ON_THE_WAY":
                tvStatus.setText("On the way");
                tvStatusDetail.setText("Your order is on its way to you");
                tvTimeRemaining.setText("Time left: 15mins");
                tvOrderOnWay.setTextColor(getResources().getColor(R.color.black));
                tvOrderDelivered.setTextColor(getResources().getColor(R.color.light_gray));
                break;
            case "DELIVERED":
                tvStatus.setText("Delivered");
                tvStatusDetail.setText("Your order has been delivered");
                tvTimeRemaining.setText("Delivered");
                tvOrderOnWay.setTextColor(getResources().getColor(R.color.black));
                tvOrderDelivered.setTextColor(getResources().getColor(R.color.black));
                break;
            default:
                tvStatus.setText("Processing");
                tvStatusDetail.setText("Your order is being processed");
                tvTimeRemaining.setText("Please wait");
                break;
        }

        // Update delivery location
        tvDeliverTo.setText(order.getDeliverTo() != null && !order.getDeliverTo().equals("N/A")
                ? order.getDeliverTo() : "Home");

        // Update amount paid
        tvAmountPaid.setText("$" + String.format(Locale.US, "%.2f", order.getTotalPrice()));

        // Update food items
        if (order.getFoodItems() != null && !order.getFoodItems().isEmpty()) {
            foodItemAdapter = new TrackingFoodItemAdapter(this, order.getFoodItems());
            rvFoodItems.setAdapter(foodItemAdapter);
        }
    }
}