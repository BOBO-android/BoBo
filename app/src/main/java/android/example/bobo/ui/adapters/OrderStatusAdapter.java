package android.example.bobo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.example.bobo.R;
import android.example.bobo.data.model.OrderStatus;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.StatusViewHolder> {

    private List<OrderStatus> statusList;

    public OrderStatusAdapter(List<OrderStatus> statusList) {
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        OrderStatus status = statusList.get(position);

        holder.tvStatusTitle.setText(status.getTitle());

        if (status.getDescription() != null && !status.getDescription().isEmpty()) {
            holder.tvStatusDescription.setVisibility(View.VISIBLE);
            holder.tvStatusDescription.setText(status.getDescription());
        } else {
            holder.tvStatusDescription.setVisibility(View.GONE);
        }

        if (status.getTime() != null && !status.getTime().isEmpty()) {
            holder.tvStatusTime.setVisibility(View.VISIBLE);
            holder.tvStatusTime.setText(status.getTime());
        } else {
            holder.tvStatusTime.setVisibility(View.GONE);
        }

        // Hiển thị đường kẻ phía trên, ngoại trừ mục đầu tiên
        if (position == 0) {
            holder.statusLineTop.setVisibility(View.INVISIBLE);
        } else {
            holder.statusLineTop.setVisibility(View.VISIBLE);

            // Nếu mục trước đó đã hoàn thành, hiển thị đường kẻ là màu xanh lá
            if (statusList.get(position - 1).isActive()) {
                holder.statusLineTop.setBackgroundResource(R.color.green);
            } else {
                holder.statusLineTop.setBackgroundResource(R.color.gray_light);
            }
        }

        // Hiển thị đường kẻ phía dưới, ngoại trừ mục cuối cùng
        if (position == getItemCount() - 1) {
            holder.statusLineBottom.setVisibility(View.INVISIBLE);
        } else {
            holder.statusLineBottom.setVisibility(View.VISIBLE);

            // Nếu mục hiện tại đã hoàn thành, hiển thị đường kẻ phía dưới là màu xanh lá
            if (status.isActive()) {
                holder.statusLineBottom.setBackgroundResource(R.color.green);
            } else {
                holder.statusLineBottom.setBackgroundResource(R.color.gray_light);
            }
        }

        // Đặt trạng thái hoạt động hoặc không hoạt động
        if (status.isActive()) {
            holder.statusCircle.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.green));
            holder.imgStatusIcon.setVisibility(View.VISIBLE);
        } else {
            holder.statusCircle.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.gray_light));
            holder.imgStatusIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatusTitle;
        TextView tvStatusDescription;
        TextView tvStatusTime;
        View statusLineTop;
        View statusLineBottom;
        CardView statusCircle;
        ImageView imgStatusIcon;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatusTitle = itemView.findViewById(R.id.tvStatusTitle);
            tvStatusDescription = itemView.findViewById(R.id.tvStatusDescription);
            tvStatusTime = itemView.findViewById(R.id.tvStatusTime);
            statusLineTop = itemView.findViewById(R.id.statusLineTop);
            statusLineBottom = itemView.findViewById(R.id.statusLineBottom);
            statusCircle = itemView.findViewById(R.id.statusCircle);
            imgStatusIcon = itemView.findViewById(R.id.imgStatusIcon);
        }
    }
}