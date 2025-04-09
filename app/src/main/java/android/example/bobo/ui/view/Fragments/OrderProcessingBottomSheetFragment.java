package android.example.bobo.ui.view.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.example.bobo.R;
import android.example.bobo.data.model.FoodItem;
import android.example.bobo.data.repository.StoreOrderRepository;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class OrderProcessingBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String STATUS_IN_DELIVERY = "INSHIPPING";
    public static final String STATUS_DELIVERED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELED";

    private String storeId;           // Add storeId
    private String token;             // Add token
    private String orderId;
    private String customerName;
    private int customerImage;
    private String customerType;
    private String deliveryAddress;
    private String amountPaid;
    private String estimatedTime;
    private String preparationTime;
    private List<FoodItem> foodItems;
    private Runnable onDismissCallback;
    private Runnable onUpdateCallback;
    private StoreOrderRepository storeOrderRepository; // Add repository

    public OrderProcessingBottomSheetFragment() {}

    public OrderProcessingBottomSheetFragment(String storeId, String token, String orderId,
                                              String customerName, String customerType, int customerImage,
                                              String deliveryAddress, String amountPaid, String estimatedTime,
                                              String preparationTime, List<FoodItem> foodItems,
                                              Runnable onDismissCallback, Runnable onUpdateCallback) {
        this.storeId = storeId;
        this.token = token;
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerType = customerType;
        this.customerImage = customerImage;
        this.deliveryAddress = deliveryAddress;
        this.amountPaid = amountPaid;
        this.estimatedTime = estimatedTime;
        this.preparationTime = preparationTime;
        this.foodItems = foodItems;
        this.onDismissCallback = onDismissCallback;
        this.onUpdateCallback = onUpdateCallback;
        this.storeOrderRepository = new StoreOrderRepository();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order_processing_details_bottom_sheet, null);
        dialog.setContentView(view);

        // Initialize UI elements
        ImageView btnClose = view.findViewById(R.id.btnClose);
        ImageView imgCustomer = view.findViewById(R.id.customerImage);
        TextView tvCustomerName = view.findViewById(R.id.customerName);
        TextView tvCustomerType = view.findViewById(R.id.customerType);
        TextView tvEstimatedTime = view.findViewById(R.id.estimatedTime);
        TextView tvTimeRequired = view.findViewById(R.id.timeRequired);
        TextView tvDeliveryAddress = view.findViewById(R.id.deliveryAddress);
        TextView tvAmountPaid = view.findViewById(R.id.amountPaid);
        RecyclerView recyclerView = view.findViewById(R.id.foodItemsRecyclerView);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        // Set message and call button listeners
        ImageView btnMessage = view.findViewById(R.id.btnMessage);
        ImageView btnCall = view.findViewById(R.id.btnCall);

        btnMessage.setOnClickListener(v -> {
            // Xử lý khi nhấn nút tin nhắn
            // Implement later
        });

        btnCall.setOnClickListener(v -> {
            // Xử lý khi nhấn nút gọi điện
            // Implement later
        });

        // Set data
        imgCustomer.setImageResource(customerImage);
        tvCustomerName.setText(customerName);
        tvCustomerType.setText(customerType != null ? customerType : "Customer");
        tvEstimatedTime.setText(estimatedTime != null ? estimatedTime : "30mins");
        tvTimeRequired.setText("Time Req. " + (preparationTime != null ? preparationTime : "20mins"));
        tvDeliveryAddress.setText(deliveryAddress);
        tvAmountPaid.setText(amountPaid);

        // Load customer image (placeholder for now)
        // customerImage.setImageResource(R.drawable.default_avatar);

        // Setup RecyclerView for food items
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        FoodItemsAdapter adapter = new FoodItemsAdapter(getContext(), foodItems);
        recyclerView.setAdapter(adapter);

        // Close button click listener
        btnClose.setOnClickListener(v -> dismiss());

        // Update button click listener
        btnUpdate.setOnClickListener(v -> {
            showStatusSelectionDialog();
        });

        // Expand to full height
        dialog.setOnShowListener(dialogInterface -> setupBottomSheetHeight(dialog));

        return dialog;
    }

    private void setupBottomSheetHeight(BottomSheetDialog dialog) {
        // Get the BottomSheet view
        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            // Get BottomSheetBehavior
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);

            // Set the peak height to 90% of screen height
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            int peekHeight = (int) (screenHeight * 0.89);
            behavior.setPeekHeight(peekHeight);

            // Set the height of the BottomSheet
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = peekHeight;
            bottomSheet.setLayoutParams(layoutParams);

            // Expand to the set height
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            // Skip collapsed state (so it's either expanded or hidden)
            behavior.setSkipCollapsed(true);

            // Add elevation to prevent clipping issues
            ViewCompat.setElevation(bottomSheet, 16);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissCallback != null) {
            onDismissCallback.run(); // Run callback when dismissed
        }
    }

    private void showStatusSelectionDialog() {
        // Create and configure the bottom sheet dialog
        BottomSheetDialog statusDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View statusView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_order_status_update, null);
        statusDialog.setContentView(statusView);

        // Get references to the status buttons
        View btnInDelivery = statusView.findViewById(R.id.btnInDelivery);
        View btnDelivered = statusView.findViewById(R.id.btnDelivered);
        View btnCancelled = statusView.findViewById(R.id.btnOrderCancelled);
        View btnCancel = statusView.findViewById(R.id.btnCancel);

        // Set click listeners for each status option
        btnInDelivery.setOnClickListener(v -> {
            updateOrderStatus(STATUS_IN_DELIVERY);
            statusDialog.dismiss();
        });

        btnDelivered.setOnClickListener(v -> {
            updateOrderStatus(STATUS_DELIVERED);
            statusDialog.dismiss();
        });

        btnCancelled.setOnClickListener(v -> {
            updateOrderStatus(STATUS_CANCELLED);
            statusDialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            statusDialog.dismiss();
        });

        // Show the dialog
        statusDialog.show();
    }

    // Update updateOrderStatus method to call API
    private void updateOrderStatus(String newStatus) {
        // Show loading indicator nếu cần
        Toast.makeText(getContext(), "Updating order status...", Toast.LENGTH_SHORT).show();

        // Create LiveData to track results
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        // Monitor LiveData
        resultLiveData.observe(this, success -> {
            if (success) {
                // Success case
                Toast.makeText(getContext(), "Order status updated to: " + newStatus, Toast.LENGTH_SHORT).show();

                // Gọi callback để thông báo cập nhật
                if (onUpdateCallback != null) {
                    onUpdateCallback.run();
                }

                // Close bottom sheet after successful update
                dismiss();
            } else {
                // Failure case
                Toast.makeText(getContext(), "Failed to update order status", Toast.LENGTH_SHORT).show();
            }
        });

        // Call API to update status
        Log.d("OrderSheet", "Updating status: storeId=" + storeId + ", orderId=" + orderId + ", status=" + newStatus);
        storeOrderRepository.updateOrderStatus(storeId, orderId, newStatus, token, resultLiveData);
    }

    // Builder pattern để dễ dàng khởi tạo fragment
    public static class Builder {
        private String storeId;       // Add storeId
        private String token;         // Add token
        private String customerName;
        private String customerType = "Customer";
        private int customerImage = R.drawable.avatars;
        private String deliveryAddress = "Home";
        private String amountPaid = "$0.00";
        private String estimatedTime = "30mins";
        private String preparationTime = "20mins";
        private String orderId;
        private List<FoodItem> foodItems;
        private Runnable onDismissCallback;
        private Runnable onUpdateCallback;

        public Builder setStoreId(String storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder setCustomerType(String customerType) {
            this.customerType = customerType;
            return this;
        }

        public Builder setCustomerImage(int customerImage) {
            this.customerImage = customerImage;
            return this;
        }

        public Builder setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder setAmountPaid(String amountPaid) {
            this.amountPaid = amountPaid;
            return this;
        }

        public Builder setEstimatedTime(String estimatedTime) {
            this.estimatedTime = estimatedTime;
            return this;
        }

        public Builder setPreparationTime(String preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }

        public Builder setOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setFoodItems(List<FoodItem> foodItems) {
            this.foodItems = foodItems;
            return this;
        }

        public Builder setOnDismissCallback(Runnable onDismissCallback) {
            this.onDismissCallback = onDismissCallback;
            return this;
        }

        public Builder setOnUpdateCallback(Runnable onUpdateCallback) {
            this.onUpdateCallback = onUpdateCallback;
            return this;
        }

        public OrderProcessingBottomSheetFragment build() {
            return new OrderProcessingBottomSheetFragment(
                    storeId, token, orderId, customerName, customerType, customerImage,
                    deliveryAddress, amountPaid, estimatedTime, preparationTime,
                    foodItems, onDismissCallback, onUpdateCallback
            );
        }
    }
}
