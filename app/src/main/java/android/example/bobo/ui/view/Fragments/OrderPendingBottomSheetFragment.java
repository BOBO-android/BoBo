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
import android.widget.ImageButton;
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

public class OrderPendingBottomSheetFragment extends BottomSheetDialogFragment {
    private String storeId;
    private String orderId;
    private String token;
    private String customerName;
    private String customerImage;
    private String deliveryAddress;
    private String amountPaid;
    private List<FoodItem> foodItems;
    private Runnable onDismissCallback;
    private StoreOrderRepository storeOrderRepository;

    public OrderPendingBottomSheetFragment() {}

    public OrderPendingBottomSheetFragment(String storeId, String orderId, String customerName,
                                           String deliveryAddress, String amountPaid,
                                           List<FoodItem> foodItems, String token,
                                           Runnable onDismissCallback) {
        this.storeId = storeId;
        this.orderId = orderId;
        this.token = token;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.amountPaid = amountPaid;
        this.foodItems = foodItems;
        this.onDismissCallback = onDismissCallback;
        this.storeOrderRepository = new StoreOrderRepository();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_order_details_bottom_sheet, null);
        dialog.setContentView(view);

        // Initialize UI elements
        TextView tvCustomerName = view.findViewById(R.id.customerName);
        TextView tvDeliveryAddress = view.findViewById(R.id.deliveryAddress);
        TextView tvAmountPaid = view.findViewById(R.id.amountPaid);
        ImageView btnClose = view.findViewById(R.id.btnClose);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        RecyclerView recyclerView = view.findViewById(R.id.foodItemsRecyclerView);
        ImageButton btnDelete = view.findViewById(R.id.btnDelete);

        // Set data
        tvCustomerName.setText(customerName);
        tvDeliveryAddress.setText(deliveryAddress);
        tvAmountPaid.setText(amountPaid);

        // Setup RecyclerView for food items
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        FoodItemsAdapter adapter = new FoodItemsAdapter(getContext(), foodItems);
        recyclerView.setAdapter(adapter);

        // Close button click listener
        btnClose.setOnClickListener(v -> dismiss());

        // Confirm button click listener
        btnConfirm.setOnClickListener(v -> {
            try {
                // Show loading indicator if needed
                btnConfirm.setEnabled(false);
                btnConfirm.setText("Processing...");

                // Create LiveData object to observe
                MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

                // Observe the LiveData for changes
                resultLiveData.observe(this, success -> {
                    if (success) {
                        // Success case
                        Toast.makeText(getContext(), "Order confirmed successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        // Error case
                        Toast.makeText(getContext(), "Failed to confirm order", Toast.LENGTH_SHORT).show();
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Confirm");
                    }
                });

                // Call repository method
                storeOrderRepository.updateOrderStatus(storeId, orderId, "PREPARING", token, resultLiveData);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                btnConfirm.setEnabled(true);
                btnConfirm.setText("Confirm");
            }
        });

        // Delete button click listener
        btnDelete.setOnClickListener(v -> {
            // Handle delete action here
            dismiss();
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
}
