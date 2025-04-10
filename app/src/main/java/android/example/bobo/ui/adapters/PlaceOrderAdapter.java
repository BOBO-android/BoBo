package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.data.model.PlaceOrderItem;
import android.example.bobo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PlaceOrderAdapter extends RecyclerView.Adapter<PlaceOrderAdapter.ViewHolder> {
    private List<PlaceOrderItem> orderItems;
    private Context context;

    public PlaceOrderAdapter(Context context, List<PlaceOrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_place_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceOrderItem item = orderItems.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("$" + item.getPrice());
        holder.quantity.setText("x" + item.getQuantity());

        // Hiển thị ảnh từ URL sử dụng Glide
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder) // Thay thế bằng hình placeholder của bạn
                            .error(R.drawable.placeholder)) // Thay thế bằng hình lỗi của bạn
                    .into(holder.image);
        } else {
            // Nếu không có URL, hiển thị hình mặc định
            holder.image.setImageResource(R.drawable.placeholder); // Thay thế bằng hình mặc định
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textProductName);
            price = itemView.findViewById(R.id.textProductPrice);
            quantity = itemView.findViewById(R.id.textProductQuantity);
            image = itemView.findViewById(R.id.imageProduct);
        }
    }
}