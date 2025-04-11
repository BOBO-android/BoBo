package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.Dish;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {

    private Context context;
    private List<Dish> dishList;
    private OnDishClickListener listener;

    public interface OnDishClickListener {
        void onEditClicked(Dish dish);
        void onDeleteClicked(Dish dish);
        void onMarkAsOutOfStock(Dish dish);
    }

    public DishAdapter(Context context, List<Dish> dishList, OnDishClickListener listener) {
        this.context = context;
        this.dishList = dishList;
        this.listener = listener;
    }

    public void setDishList(List<Dish> newList) {
        this.dishList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.txtDishName.setText(dish.getName());
        holder.txtDishPrice.setText("$" + String.format("%.2f", dish.getPrice()));
        Glide.with(context)
                .load(dish.getThumbnail())
                .placeholder(R.drawable.background) // Default image while loading
                .error(R.drawable.ic_store_review) // Image to show if loading fails
                .into(holder.imgDish);

        holder.btnOptions.setOnClickListener(v -> {
            // Implement your options menu here (e.g., using a PopupMenu)
            // You can use the listener to communicate actions to the Activity/Fragment
        });
    }

    @Override
    public int getItemCount() {
        return dishList == null ? 0 : dishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView txtDishName;
        TextView txtDishPrice;
        ImageView btnOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
            txtDishPrice = itemView.findViewById(R.id.txt_dish_price);
            btnOptions = itemView.findViewById(R.id.btn_options);
        }
    }
}