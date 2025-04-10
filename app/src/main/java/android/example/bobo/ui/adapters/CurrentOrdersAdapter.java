package android.example.bobo.ui.adapters;

import android.example.bobo.R;
import android.example.bobo.data.model.Order;
import android.example.bobo.ui.view.OrderTrackingBottomSheet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrentOrdersAdapter extends RecyclerView.Adapter<CurrentOrdersAdapter.ViewHolder> {
    private List<Order> orders;
    private FragmentActivity activity;

    public CurrentOrdersAdapter(FragmentActivity activity, List<Order> orders) {
        this.activity = activity;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.txtOrderSummary.setText(order.getSummary());
        holder.txtEstDelivery.setText(order.getEstDelivery());
        holder.txtTotalPrice.setText(order.getTotalPrice());
        holder.imgOrder.setImageResource(order.getImageResId());

        // Thêm xử lý sự kiện cho nút "Track Order"
        holder.btnTrackOrder.setOnClickListener(v -> {
            showOrderTracking(order.getId());
        });

        // Có thể thêm sự kiện click cho toàn bộ item nếu cần
        holder.itemView.setOnClickListener(v -> {
            // Có thể thực hiện hành động khác khi click vào item
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Phương thức để hiển thị OrderTrackingBottomSheet
    private void showOrderTracking(String orderId) {
        if (activity != null) {
            OrderTrackingBottomSheet bottomSheet = OrderTrackingBottomSheet.newInstance(orderId);
            bottomSheet.show(activity.getSupportFragmentManager());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderSummary, txtEstDelivery, txtTotalPrice;
        ImageView imgOrder;
        Button btnTrackOrder; // Thêm Button dành cho Track Order

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderSummary = itemView.findViewById(R.id.txtOrderSummary);
            txtEstDelivery = itemView.findViewById(R.id.txtEstDelivery);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            imgOrder = itemView.findViewById(R.id.imgOrder);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
        }
    }
}