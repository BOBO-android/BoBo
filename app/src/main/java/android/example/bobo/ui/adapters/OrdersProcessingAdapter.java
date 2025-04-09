package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.Order;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class OrdersProcessingAdapter extends RecyclerView.Adapter<OrdersProcessingAdapter.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private OrdersProcessingAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public OrdersProcessingAdapter(Context context, List<Order> orderList, OrdersProcessingAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public OrdersProcessingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrdersProcessingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersProcessingAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName, txtDishName, txtOrderId, txtOrderTime, txtPrice;
        private ShapeableImageView imgUserProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find all views by ID based on the new layout
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
            txtOrderId = itemView.findViewById(R.id.txt_order_id);
            txtOrderTime = itemView.findViewById(R.id.txt_order_time);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgUserProfile = itemView.findViewById(R.id.img_user_profile);
        }

        public void bind(final Order order, final OrdersProcessingAdapter.OnItemClickListener listener) {
            // Set user name (you may need to add this property to your Order class)
            // For now, we can use a default name or get it from the order if available
            txtUserName.setText(order.getUserName() != null ? order.getUserName() : "Cliff Rogers");

            // Set dish name with quantity
            txtDishName.setText(order.getMainDishName() + " x1");

            // Set order ID (you may need to add this property to your Order class)
            txtOrderId.setText(order.getOrderId() != null ? order.getOrderId() : "10195652");

            // Set order time (replacing delivery date)
            // You might want to format this based on your requirements
            txtOrderTime.setText(order.getOrderTime().toString() != null ? order.getOrderTime().toString() : "04/11/2022 17:22");

            // Set price
            txtPrice.setText(order.getFormattedPrice());

            // Set user profile image (you may need to add this property to your Order class)
            if (order.getUserImageResId() != 0) {
                imgUserProfile.setImageResource(order.getUserImageResId());
            } else {
                // Set a default user profile image
                imgUserProfile.setImageResource(R.drawable.avatars);
            }

            // Set click listener
            itemView.setOnClickListener(v -> listener.onItemClick(order));
        }
    }
}
