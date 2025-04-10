package android.example.bobo.ui.adapters;

import android.example.bobo.R;
import android.example.bobo.data.model.Order;
import android.example.bobo.ui.view.Fragments.RatingDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PreviousOrdersAdapter extends RecyclerView.Adapter<PreviousOrdersAdapter.ViewHolder> {
    private List<Order> orders;

    public PreviousOrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.txtOrderSummary.setText(order.getSummary());
        holder.txtDeliveredDate.setText(order.getEstDelivery());
        holder.txtTotalPrice.setText(order.getTotalPrice());
        holder.imgOrder.setImageResource(order.getImageResId());

        holder.btnReorder.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Reordering: " + order.getSummary(), Toast.LENGTH_SHORT).show();
        });

        holder.btnMoreOptions.setOnClickListener(v -> {
            RatingDialogFragment ratingDialog = new RatingDialogFragment();
            ratingDialog.setRatingListener(rating -> {
                Toast.makeText(v.getContext(), "You rated: " + rating + " stars", Toast.LENGTH_SHORT).show();
            });

            ratingDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "RatingDialog");
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderSummary, txtDeliveredDate, txtTotalPrice;
        ImageView imgOrder;
        Button btnReorder, btnMoreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderSummary = itemView.findViewById(R.id.txtOrderSummary);
            txtDeliveredDate = itemView.findViewById(R.id.txtDeliveredDate);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            imgOrder = itemView.findViewById(R.id.imgOrder);
            btnReorder = itemView.findViewById(R.id.btnReorder);
            btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
        }
    }
}
