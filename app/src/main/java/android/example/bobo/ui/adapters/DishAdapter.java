package android.example.bobo.ui.adapters;

import android.content.Context;
import android.example.bobo.R;
import android.example.bobo.data.model.Dish;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {

    private final List<Dish> dishList;
    private final Context context;
    private final OnDishClickListener listener;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.txtDishName.setText(dish.getName());
        holder.txtDishPrice.setText("$" + dish.getPrice());

        // Load image using Glide for better performance
        Glide.with(context)
                .load(dish.getThumbnail())
                .placeholder(R.drawable.background)
                .into(holder.imgDish);

        holder.imgMoreOptions.setOnClickListener(v -> showBottomSheetDialog(dish));
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgDish;
        TextView txtDishName, txtDishPrice;
        ImageView imgMoreOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtDishName = itemView.findViewById(R.id.txt_dish_name);
            txtDishPrice = itemView.findViewById(R.id.txt_dish_price);
            imgMoreOptions = itemView.findViewById(R.id.btn_options);
        }
    }

    private void showBottomSheetDialog(Dish dish) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu, null);
        bottomSheetDialog.setContentView(view);

        View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetInternal != null) {
            bottomSheetInternal.setBackgroundResource(android.R.color.transparent);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        TextView editOption = view.findViewById(R.id.option_edit);
        TextView deleteOption = view.findViewById(R.id.option_delete);
        TextView markDishOption = view.findViewById(R.id.option_mark_dish);
        TextView cancelOption = view.findViewById(R.id.option_cancel);

        editOption.setOnClickListener(v -> {
            listener.onEditClicked(dish);
            bottomSheetDialog.dismiss();
        });

        deleteOption.setOnClickListener(v -> {
            listener.onDeleteClicked(dish);
            bottomSheetDialog.dismiss();
        });

        markDishOption.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showMarkAsOutOfStockDialog(dish);
        });

        cancelOption.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    private void showMarkAsOutOfStockDialog(Dish dish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View markView = LayoutInflater.from(context).inflate(R.layout.dialog_mark_out_of_stock, null);
        builder.setView(markView);

        AlertDialog markDialog = builder.create();
        if (markDialog.getWindow() != null) {
            markDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button cancelButton = markView.findViewById(R.id.btn_cancel);
        Button saveButton = markView.findViewById(R.id.btn_save);
        TextView itemName = markView.findViewById(R.id.tv_item_name);
        itemName.setText(String.format("%s - %s", dish.getName(), dish.getPrice()));

        cancelButton.setOnClickListener(v -> markDialog.dismiss());

        saveButton.setOnClickListener(v -> {
            listener.onMarkAsOutOfStock(dish);
            Toast.makeText(context, "Item marked as out of stock", Toast.LENGTH_SHORT).show();
            markDialog.dismiss();
        });

        markDialog.show();
    }
}
