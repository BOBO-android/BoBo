package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.FoodItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TrackingFoodItemAdapter extends RecyclerView.Adapter<TrackingFoodItemAdapter.FoodItemViewHolder> {

    private Context context;
    private List<FoodItem> foodItems; // Sử dụng FoodItem thay vì TrackingFoodItem

    public TrackingFoodItemAdapter(Context context, List<FoodItem> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tracking_food_item, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position); // Sử dụng FoodItem

        Glide.with(context)
                .load(foodItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.foodImage);

        holder.foodQuantity.setText("x" + foodItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        return foodItems != null ? foodItems.size() : 0;
    }

    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodQuantity;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.iv_food);
            foodQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
