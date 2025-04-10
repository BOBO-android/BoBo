package android.example.bobo.ui.adapters;

import android.example.bobo.R;
import android.example.bobo.data.model.CartItem;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private OnQuantityChangeListener listener;

    public CartAdapter(List<CartItem> cartItems, OnQuantityChangeListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Hiển thị tên món
        holder.itemName.setText(item.getName());


        holder.itemPrice.setText("$" + String.format("%.2f", item.getPrice()));

        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));



        if (!TextUtils.isEmpty(item.getImageUrl())) {
            Glide.with(holder.itemImage.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.itemImage);
        } else if (item.getImageResId() != 0) {

            holder.itemImage.setImageResource(item.getImageResId());
        } else {

            holder.itemImage.setImageResource(R.drawable.placeholder);
        }


        if (item.getQuantity() > 1) {
            holder.btnDecrease.setImageResource(R.drawable.ic_minus);
        } else {
            holder.btnDecrease.setImageResource(R.drawable.ic_trash);
        }

        // Trong onBindViewHolder của CartAdapter
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            if (listener != null) {
                // Đảm bảo sử dụng đúng foodId
                String foodId = item.getFoodId();
                Log.d("CartAdapter", "Increasing quantity for foodId: " + foodId + ", New quantity: " + newQuantity);
                listener.onQuantityChanged(foodId, newQuantity);
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                if (listener != null) {
                    String foodId = item.getFoodId();
                    Log.d("CartAdapter", "Decreasing quantity for foodId: " + foodId + ", New quantity: " + newQuantity);
                    listener.onQuantityChanged(foodId, newQuantity);
                }
            } else {
                if (listener != null) {
                    String foodId = item.getFoodId();
                    Log.d("CartAdapter", "Removing item with foodId: " + foodId);
                    listener.onQuantityChanged(foodId, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(String foodId, int newQuantity);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;
        ImageButton btnIncrease;
        ImageButton btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }
}