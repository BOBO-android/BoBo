package android.example.bobo.ui.adapters;

import android.example.bobo.R;
import android.example.bobo.data.model.PromotionItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private List<PromotionItem> promotions;
    private OnPromotionClickListener listener;

    public interface OnPromotionClickListener {
        void onPromotionClick(PromotionItem promotion);
    }

    public PromotionAdapter(List<PromotionItem> promotions, OnPromotionClickListener listener) {
        this.promotions = promotions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promotion, parent, false);
        // Ensure the view takes up the full width and height of the ViewPager2
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(layoutParams);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        holder.bind(promotions.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    static class PromotionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView subtitleTextView;
        private Button buyNowButton;
        private ImageView promoImageView;
        private CardView cardView;

        PromotionViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_promo_title);
            subtitleTextView = itemView.findViewById(R.id.tv_promo_subtitle);
            buyNowButton = itemView.findViewById(R.id.btn_buy_now);
            promoImageView = itemView.findViewById(R.id.iv_promo_image);
            cardView = (CardView) itemView;
        }

        void bind(final PromotionItem promotion, final OnPromotionClickListener listener) {
            titleTextView.setText(promotion.getTitle());
            subtitleTextView.setText(promotion.getSubtitle());
            buyNowButton.setText(promotion.getButtonText());
            promoImageView.setImageResource(promotion.getImageRes());

            // Set background color for the promotion
            cardView.setCardBackgroundColor(promotion.getBackgroundColor());

            // Set click listeners
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPromotionClick(promotion);
                }
            });

            buyNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPromotionClick(promotion);
                }
            });
        }
    }
}