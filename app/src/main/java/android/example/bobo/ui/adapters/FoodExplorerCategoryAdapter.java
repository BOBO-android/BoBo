package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FoodExplorerCategoryAdapter extends RecyclerView.Adapter<FoodExplorerCategoryAdapter.FoodExplorerCategoryViewHolder> {

    private final List<FoodExplorerCategory> foodExplorerCategories;
    private final Context context;
    private OnFoodExplorerCategoryClickListener listener;

    public FoodExplorerCategoryAdapter(Context context) {
        this.context = context;
        this.foodExplorerCategories = new ArrayList<>();
        // Add dummy categories
        foodExplorerCategories.add(new FoodExplorerCategory("Fast food", R.drawable.donut));
        foodExplorerCategories.add(new FoodExplorerCategory("Burger", R.drawable.burger));
        foodExplorerCategories.add(new FoodExplorerCategory("Beverages", R.drawable.soda));
    }

    public void setOnFoodExplorerCategoryClickListener(OnFoodExplorerCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodExplorerCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_explorer_category, parent, false);
        return new FoodExplorerCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodExplorerCategoryViewHolder holder, int position) {
        FoodExplorerCategory category = foodExplorerCategories.get(position);
        holder.categoryName.setText(category.getName());

        // Load image with Glide
        Glide.with(context)
                .load(category.getImageResourceId())
                .into(holder.categoryImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodExplorerCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodExplorerCategories.size();
    }

    public static class FoodExplorerCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public FoodExplorerCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.explorer_image_category);
            categoryName = itemView.findViewById(R.id.explorer_text_category_name);
        }
    }

    public static class FoodExplorerCategory {
        private final String name;
        private final int imageResourceId;

        public FoodExplorerCategory(String name, int imageResourceId) {
            this.name = name;
            this.imageResourceId = imageResourceId;
        }

        public String getName() {
            return name;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }
    }

    public interface OnFoodExplorerCategoryClickListener {
        void onFoodExplorerCategoryClick(FoodExplorerCategory category);
    }
}