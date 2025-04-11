package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.SearchFoodItemModel;
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

public class FoodExplorerSearchAdapter extends RecyclerView.Adapter<FoodExplorerSearchAdapter.FoodExplorerSearchViewHolder> {

    private final List<SearchFoodItemModel> foodList;
    private final Context context;
    private OnSearchFoodClickListener listener;
    private OnAddToCartClickListener addToCartListener;

    public FoodExplorerSearchAdapter(Context context) {
        this.context = context;
        this.foodList = new ArrayList<>();
    }

    public void setFoodList(List<SearchFoodItemModel> foodList) {
        this.foodList.clear();
        if (foodList != null) {
            this.foodList.addAll(foodList);
        }
        notifyDataSetChanged();
    }

    public void addFood(SearchFoodItemModel food) {
        this.foodList.add(food);
        notifyItemInserted(foodList.size() - 1);
    }

    public void clearList() {
        this.foodList.clear();
        notifyDataSetChanged();
    }

    public void setOnSearchFoodClickListener(OnSearchFoodClickListener listener) {
        this.listener = listener;
    }

    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.addToCartListener = listener;
    }

    @NonNull
    @Override
    public FoodExplorerSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_explorer_food, parent, false);
        return new FoodExplorerSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodExplorerSearchViewHolder holder, int position) {
        SearchFoodItemModel food = foodList.get(position);

        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(formatPrice(food.getPrice()));

        // Set rating
        holder.foodRating.setText(String.valueOf(food.getRating()));

        // Set discount if available
        if (food.isOffered() && food.getDiscount() > 0) {
            holder.foodDiscount.setVisibility(View.VISIBLE);
            holder.foodDiscount.setText(food.getDiscount() + "% OFF");
        } else {
            holder.foodDiscount.setVisibility(View.GONE);
        }

        // Load image with Glide
        Glide.with(context)
                .load(food.getThumbnail())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.foodImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSearchFoodClick(food);
            }
        });

        holder.addButton.setOnClickListener(v -> {
            if (addToCartListener != null) {
                addToCartListener.onAddToCartClick(food);
            }
        });
    }

    private String formatPrice(double price) {
        // Format price in USD
        return String.format("$%.2f", price);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodExplorerSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        TextView foodRating;
        TextView foodPrice;
        TextView foodDiscount;
        Button addButton;

        public FoodExplorerSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.explorer_image_food);
            foodName = itemView.findViewById(R.id.explorer_text_food_name);
            foodRating = itemView.findViewById(R.id.explorer_text_rating);
            foodPrice = itemView.findViewById(R.id.explorer_text_price);
            foodDiscount = itemView.findViewById(R.id.explorer_text_discount);
            addButton = itemView.findViewById(R.id.explorer_button_add);
        }
    }

    public interface OnSearchFoodClickListener {
        void onSearchFoodClick(SearchFoodItemModel food);
    }

    public interface OnAddToCartClickListener {
        void onAddToCartClick(SearchFoodItemModel food);
    }
}