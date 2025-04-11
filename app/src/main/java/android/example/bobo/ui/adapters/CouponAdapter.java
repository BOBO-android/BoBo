package android.example.bobo.ui.adapters;

import android.example.bobo.R;
import android.example.bobo.data.model.Coupon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {
    private List<Coupon> couponList;
    private int selectedPosition = -1; // Chỉ có 1 mã được chọn
    private OnCouponSelectedListener listener;

    public interface OnCouponSelectedListener {
        void onCouponSelected(Coupon selectedCoupon);
    }

    public CouponAdapter(List<Coupon> couponList, OnCouponSelectedListener listener) {
        this.couponList = couponList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        holder.couponName.setText(coupon.getName());
        holder.couponDetails.setText(coupon.getDetails());

        // Chỉ cho phép chọn 1 mã giảm giá duy nhất
        holder.radioButton.setChecked(position == selectedPosition);

        holder.radioButton.setOnClickListener(v -> {
            if (selectedPosition != position) {
                selectedPosition = position;
                notifyDataSetChanged();
                listener.onCouponSelected(couponList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public static class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView couponName, couponDetails;
        RadioButton radioButton;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            couponName = itemView.findViewById(R.id.coupon_name);
            couponDetails = itemView.findViewById(R.id.coupon_description);
            radioButton = itemView.findViewById(R.id.btn_check_coupon);
        }
    }
}
