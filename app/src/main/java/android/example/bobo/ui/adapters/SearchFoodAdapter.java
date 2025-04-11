package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.SearchFoodItemModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.ViewHolder> {

    private final List<SearchFoodItemModel> foodItems;
    private final Context context;
    private final OnFoodItemClickListener listener;

    public interface OnFoodItemClickListener {
        void onFoodItemClick(SearchFoodItemModel food);
        void onAddToCartClick(SearchFoodItemModel food);
    }

    public SearchFoodAdapter(Context context, List<SearchFoodItemModel> foodItems, OnFoodItemClickListener listener) {
        this.context = context;
        this.foodItems = foodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchFoodItemModel food = foodItems.get(position);
        holder.bind(food, listener);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView foodImage;
        private final TextView foodName;
        private final TextView foodPrice;
        private final TextView discount;
        private final TextView rating;
        private final ImageButton addButton;
        private final View ratingLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.iv_food_image);
            foodName = itemView.findViewById(R.id.tv_food_name);
            foodPrice = itemView.findViewById(R.id.tv_food_price);
            discount = itemView.findViewById(R.id.tv_discount);
            rating = itemView.findViewById(R.id.tv_rating);
            addButton = itemView.findViewById(R.id.btn_add);
            ratingLayout = itemView.findViewById(R.id.layout_rating);
        }

        public void bind(final SearchFoodItemModel food, final OnFoodItemClickListener listener) {
            // Set food name
            foodName.setText(food.getName());

            // Format and set price
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            foodPrice.setText(formatter.format(food.getPrice()));

            // Set discount if available
            if (food.hasDiscount()) {
                discount.setVisibility(View.VISIBLE);
                discount.setText(food.getDiscountText());
            } else {
                discount.setVisibility(View.GONE);
            }

            // Set rating
            rating.setText(String.valueOf(food.getRating()));

            // Load image using Glide
            Glide.with(itemView.getContext())
                    .load(food.getThumbnail())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(foodImage);

            // Set click listeners
            itemView.setOnClickListener(v -> listener.onFoodItemClick(food));
            addButton.setOnClickListener(v -> listener.onAddToCartClick(food));
        }
    }
}
