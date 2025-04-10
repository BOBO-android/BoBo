package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.FoodItem;
import android.example.bobo.data.model.UserFoodItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserFoodAdapter extends RecyclerView.Adapter<UserFoodAdapter.FoodViewHolder> {
    private List<UserFoodItem> userFoodItems;
    private Context context;
    private OnFoodItemClickListener listener;

    public interface OnFoodItemClickListener {
        void onFoodItemClick(UserFoodItem userFoodItem);
        void onAddButtonClick(UserFoodItem userFoodItem);
    }

    public UserFoodAdapter(Context context, List<UserFoodItem> userFoodItems, OnFoodItemClickListener listener) {
        this.context = context;
        this.userFoodItems = userFoodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        UserFoodItem food = userFoodItems.get(position);

        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(food.getFormattedPrice());
        holder.foodRating.setText(String.valueOf(food.getRating()));

        // Sử dụng Glide để load hình ảnh
        if (food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(food.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.foodImage);
        } else {
            // Nếu không có URL, sử dụng hình ảnh local
            holder.foodImage.setImageResource(R.drawable.placeholder);
        }

        // Setup click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodItemClick(food);
            }
        });

        holder.addButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddButtonClick(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userFoodItems != null ? userFoodItems.size() : 0;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodPrice;
        TextView foodRating;
        FrameLayout addButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.iv_food_image);
            foodName = itemView.findViewById(R.id.tv_food_name);
            foodPrice = itemView.findViewById(R.id.tv_price);
            foodRating = itemView.findViewById(R.id.tv_rating);
            addButton = itemView.findViewById(R.id.btn_add);
        }
    }
}
