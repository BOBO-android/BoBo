package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.UserFoodItem;
import android.example.bobo.data.model.UserOrder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHolder> {

    private final List<UserOrder> userOrders;
    private final Context context;
    private final OnUserOrderClickListener listener;

    public interface OnUserOrderClickListener {
        void onTrackOrderClick(UserOrder userOrder);
    }

    public UserOrderAdapter(Context context, OnUserOrderClickListener listener) {
        this.context = context;
        this.userOrders = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_order, parent, false);
        return new UserOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderViewHolder holder, int position) {
        UserOrder userOrder = userOrders.get(position);
        holder.bind(userOrder);
    }

    @Override
    public int getItemCount() {
        return userOrders != null ? userOrders.size() : 0;
    }

    public void updateUserOrders(List<UserOrder> newUserOrders) {
        userOrders.clear();
        if (newUserOrders != null) {
            userOrders.addAll(newUserOrders);
        }
        notifyDataSetChanged();
    }

    class UserOrderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivFoodImage;
        private final TextView tvOrderStatus;
        private final TextView tvEstDeliveryTime;
        private final TextView tvFoodName;
        private final TextView tvTotalPrice;
        private final TextView tvItemCount;
        private final Button btnTrackOrder;

        public UserOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvEstDeliveryTime = itemView.findViewById(R.id.tvEstDeliveryTime);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
        }

        public void bind(UserOrder userOrder) {
            // Set order status
            String status = userOrder.getStatus();
            tvOrderStatus.setText(getFormattedStatus(status));

            // Set delivery time estimation
            if ("PENDING".equals(status)) {
                tvEstDeliveryTime.setText("Est. delivery: 30mins");
            } else if ("OUT_FOR_DELIVERY".equals(status)) {
                tvEstDeliveryTime.setText("Est. delivery: 10mins");
            } else {
                tvEstDeliveryTime.setText("Delivered");
            }

            // Set food name from the first food item
            List<UserFoodItem> foodItems = userOrder.getFoodItems();
            if (foodItems != null && !foodItems.isEmpty()) {
                UserFoodItem firstItem = foodItems.get(0);
                tvFoodName.setText(firstItem.getName());

                // Load image for the first food item
                String imageUrl = firstItem.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(ivFoodImage);
                } else {
                    ivFoodImage.setImageResource(R.drawable.placeholder);
                }

                // Set item count if there are more than one food items
                if (foodItems.size() > 1) {
                    tvItemCount.setVisibility(View.VISIBLE);
                    tvItemCount.setText("+" + (foodItems.size() - 1));
                } else {
                    tvItemCount.setVisibility(View.GONE);
                }
            } else {
                tvFoodName.setText("No items");
                ivFoodImage.setImageResource(R.drawable.placeholder);
                tvItemCount.setVisibility(View.GONE);
            }

            // Set total price
            tvTotalPrice.setText("$" + String.format(Locale.US, "%.2f", userOrder.getTotalPrice()));

            // Set track order button click listener
            btnTrackOrder.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTrackOrderClick(userOrder);
                }
            });
        }

        private String getFormattedStatus(String status) {
            if (status == null) return "Unknown";

            switch (status) {
                case "PENDING":
                    return "Pending";
                case "PROCESSING":
                    return "Processing";
                case "OUT_FOR_DELIVERY":
                    return "Out for delivery";
                case "DELIVERED":
                    return "Delivered";
                case "CANCELLED":
                    return "Cancelled";
                default:
                    return status;
            }
        }
    }
}