package android.example.bobo.ui.view.Fragments;

import android.content.Intent;
import android.example.bobo.ui.view.CancelOrderActivity;
import android.example.bobo.ui.view.OrderTrackingBottomSheet;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.example.bobo.R;
import android.example.bobo.data.model.FoodItem;
import android.example.bobo.data.model.OrderStatus;
import android.example.bobo.ui.adapters.FoodItemAdapter;
import android.example.bobo.ui.adapters.OrderStatusAdapter;

public class OrderStatusFragment extends Fragment {

    public static final String ARG_STATUS = "status_position";

    private static final int STATUS_PREPARING = 0;
    private static final int STATUS_ON_WAY = 1;
    private static final int STATUS_DELIVERED = 2;

    private RecyclerView recyclerOrderStatus;
    private RecyclerView recyclerFoodItems;
    private TextView tvRateFood;

    private int currentStatus;

    public static OrderStatusFragment newInstance(int statusPosition) {
        OrderStatusFragment fragment = new OrderStatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATUS, statusPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            currentStatus = getArguments().getInt(ARG_STATUS, 0);
        }

        ImageButton btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            if (getParentFragment() instanceof OrderTrackingBottomSheet) {
                ((OrderTrackingBottomSheet) getParentFragment()).dismiss();
            }
        });

        ImageButton btnChat = view.findViewById(R.id.btnChat);
        btnChat.setOnClickListener(v -> openChat());

        ImageButton btnCall = view.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(v -> makeCall());

        recyclerOrderStatus = view.findViewById(R.id.recyclerOrderStatus);
        recyclerFoodItems = view.findViewById(R.id.recyclerFoodItems);
        tvRateFood = view.findViewById(R.id.tvRateFood);

        setupOrderStatusList();
        setupFoodItemsList();

        // Hiển thị "Rate the food!" chỉ khi ở trang đơn hàng đã giao
        if (currentStatus == STATUS_DELIVERED) {
            tvRateFood.setVisibility(View.VISIBLE);
        } else {
            tvRateFood.setVisibility(View.GONE);
        }
    }

    private void setupOrderStatusList() {
        List<OrderStatus> statuses = new ArrayList<>();

        // Luôn hiển thị cả ba trạng thái, nhưng với trạng thái hoạt động khác nhau dựa trên trang hiện tại
        statuses.add(new OrderStatus(
                "Preparing your order",
                "We are preparing your food with magic and care",
                "Time Req. 20mins",
                currentStatus >= STATUS_PREPARING
        ));

        statuses.add(new OrderStatus(
                "Your order is on the way",
                "",
                "Est. time 10mins",
                currentStatus >= STATUS_ON_WAY
        ));

        statuses.add(new OrderStatus(
                "Your order has been delivered",
                "",
                "",
                currentStatus >= STATUS_DELIVERED
        ));

        OrderStatusAdapter adapter = new OrderStatusAdapter(statuses);
        recyclerOrderStatus.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerOrderStatus.setAdapter(adapter);
    }

    private void setupFoodItemsList() {
        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem(R.drawable.burger, 2));
        foodItems.add(new FoodItem(R.drawable.pizza, 1));
        foodItems.add(new FoodItem(R.drawable.donut, 1));
        foodItems.add(new FoodItem(R.drawable.soda, 1));

        FoodItemAdapter adapter = new FoodItemAdapter(foodItems);
        recyclerFoodItems.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerFoodItems.setAdapter(adapter);
    }

    private void openChat() {
        // Launch the CancelOrderActivity
        Intent intent = new Intent(requireActivity(), CancelOrderActivity.class);
        // Optionally pass any order data needed
        // intent.putExtra("ORDER_ID", orderId);
        startActivity(intent);
    }

    private void makeCall() {
        // Triển khai chức năng gọi
    }
}
