package android.example.bobo.ui.view;

import android.app.Dialog;
import android.example.bobo.R;
import android.example.bobo.data.model.TrackingOrderModel;
import android.example.bobo.ui.adapters.TrackingFoodItemAdapter;
import android.example.bobo.ui.viewmodel.TrackingViewModel;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

/**
 * BottomSheet fragment để hiển thị thông tin theo dõi đơn hàng
 */
public class TrackingBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "TrackingBottomSheet";
    private static final String ARG_CUSTOMER_ID = "customerId";

    private TrackingViewModel viewModel;

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
    private String customerId;

    /**
     * Tạo instance mới của TrackingBottomSheet
     * @param customerId ID của khách hàng để lấy đơn hàng
     * @return Instance của TrackingBottomSheet
     */
    public static TrackingBottomSheet newInstance(String customerId) {
        TrackingBottomSheet fragment = new TrackingBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CUSTOMER_ID, customerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        if (getArguments() != null) {
            customerId = getArguments().getString(ARG_CUSTOMER_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

                // Thiết lập chiều cao tối đa (80% màn hình)
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                behavior.setPeekHeight((int) (height * 0.8));
            }
        });

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_order_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(TrackingViewModel.class);

        // Khởi tạo UI Elements
        initViews(view);

        // Thiết lập RecyclerView
        rvFoodItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Đặt click listener cho nút đóng
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> dismiss());

        // Click listener cho các nút khác
        View ivMessage = view.findViewById(R.id.iv_message);
        View ivCall = view.findViewById(R.id.iv_call);

        ivMessage.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sending message to delivery person", Toast.LENGTH_SHORT).show();
            // Thêm code để mở màn hình nhắn tin
        });

        ivCall.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Calling delivery person", Toast.LENGTH_SHORT).show();
            // Thêm code để gọi điện
        });

        // Theo dõi LiveData
        observeViewModel();

        // Tải dữ liệu đơn hàng
        if (customerId != null && !customerId.isEmpty()) {
            viewModel.loadOrdersByCustomerId(customerId);
        } else {
            Toast.makeText(getContext(), "Không thể theo dõi: Thiếu ID khách hàng", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Customer ID is missing");
            dismiss();
        }
    }

    private void initViews(View view) {
        ivDeliveryPerson = view.findViewById(R.id.iv_delivery_person);
        tvDeliveryPersonName = view.findViewById(R.id.tv_delivery_person_name);
        tvEstimatedTime = view.findViewById(R.id.tv_estimated_time);
        tvStatus = view.findViewById(R.id.tv_status);
        tvStatusDetail = view.findViewById(R.id.tv_status_detail);
        tvTimeRemaining = view.findViewById(R.id.tv_time_remaining);
        tvOrderOnWay = view.findViewById(R.id.tv_order_on_way);
        tvOrderDelivered = view.findViewById(R.id.tv_order_delivered);
        tvDeliverTo = view.findViewById(R.id.tv_deliver_to);
        tvAmountPaid = view.findViewById(R.id.tv_amount_paid);
        rvFoodItems = view.findViewById(R.id.rv_food_items);
    }

    private void observeViewModel() {
        // Theo dõi trạng thái tải
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Hiện loading indicator nếu cần
            // Ví dụ: progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Theo dõi thông báo lỗi
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error: " + errorMessage);
            }
        });

        // Theo dõi dữ liệu đơn hàng
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null && !orders.isEmpty()) {
                // Có dữ liệu đơn hàng, cập nhật UI
                updateBottomSheetWithOrder(orders.get(0)); // Hiển thị đơn hàng đầu tiên
            } else {
                Toast.makeText(getContext(), "Không tìm thấy đơn hàng nào", Toast.LENGTH_LONG).show();
                Log.w(TAG, "No orders found for customer ID: " + customerId);
            }
        });
    }

    private void updateBottomSheetWithOrder(TrackingOrderModel order) {
        try {
            // Cập nhật thông tin người giao hàng
            tvDeliveryPersonName.setText(order.getUserName());
            if (order.getUserImageUrl() != null && !order.getUserImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(order.getUserImageUrl())
                        .placeholder(R.drawable.delivery_person)
                        .error(R.drawable.delivery_person)
                        .into(ivDeliveryPerson);
            }

            // Cập nhật thời gian dự kiến (mặc định 30 phút từ ảnh của bạn)
            tvEstimatedTime.setText("30mins");

            // Cập nhật trạng thái đơn hàng
            String status = order.getStatus();
            updateOrderStatus(status);

            // Cập nhật địa điểm giao hàng
            tvDeliverTo.setText(order.getDeliverTo() != null && !order.getDeliverTo().equals("N/A")
                    ? order.getDeliverTo() : "Home");

            // Cập nhật số tiền đã thanh toán
            tvAmountPaid.setText("$" + String.format(Locale.US, "%.2f", order.getTotalPrice()));

            // Cập nhật danh sách món ăn
            if (order.getFoodItems() != null && !order.getFoodItems().isEmpty()) {
                foodItemAdapter = new TrackingFoodItemAdapter(getContext(), order.getFoodItems());
                rvFoodItems.setAdapter(foodItemAdapter);
            } else {
                Log.w(TAG, "Order has no food items");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating UI with order data", e);
            Toast.makeText(getContext(), "Lỗi hiển thị thông tin đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOrderStatus(String status) {
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
    }
}