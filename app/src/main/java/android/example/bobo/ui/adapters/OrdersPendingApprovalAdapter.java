package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.Order;
import android.example.bobo.utils.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrdersPendingApprovalAdapter extends RecyclerView.Adapter<OrdersPendingApprovalAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public OrdersPendingApprovalAdapter(Context context, List<Order> orderList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName, txtDishName, txtOrderId, txtOrderTime, txtPrice;
        private ShapeableImageView imgUserProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find all views by ID based on the layout
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
            txtOrderId = itemView.findViewById(R.id.txt_order_id);
            txtOrderTime = itemView.findViewById(R.id.txt_order_time);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgUserProfile = itemView.findViewById(R.id.img_user_profile);
        }

        public void bind(final Order order, final OnItemClickListener listener) {
            // Set user name
            txtUserName.setText(order.getUserName() != null ? order.getUserName() : "Customer");

            // Set dish name with quantity (first item in foodItems list)
            String dishText = "No items";
            int quantity = 0;

            if (order.getFoodItems() != null && !order.getFoodItems().isEmpty()) {
                String dishName = order.getFoodItems().get(0).getName();
                quantity = order.getFoodItems().get(0).getQuantity();

                // Create dish display text
                dishText = dishName + " x" + quantity;

                // If there are more items, add indication
                if (order.getFoodItems().size() > 1) {
                    dishText += " + " + (order.getFoodItems().size() - 1) + " more";
                }
            }
            txtDishName.setText(dishText);

            // Set order ID
            txtOrderId.setText(order.getOrderId());

            // Format and set order time
            String orderTimeText = "N/A";
            if (order.getOrderTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                orderTimeText = sdf.format(order.getOrderTime());
            }
            txtOrderTime.setText(orderTimeText);

            // Set price
            txtPrice.setText(order.getFormattedPrice());

            // Load user profile image
            if (order.getUserImageUrl() != null && !order.getUserImageUrl().isEmpty()) {
                // Use the ImageLoader utility to load the image from URL
                ImageLoader.loadImage(itemView.getContext(), order.getUserImageUrl(), imgUserProfile);
            } else {
                // Set a default user profile image
                imgUserProfile.setImageResource(R.drawable.avatars);
            }

            // Set click listener
            itemView.setOnClickListener(v -> listener.onItemClick(order));
        }
    }

    // Method to update the order list
    public void updateOrders(List<Order> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }
}
