package android.example.bobo.ui.view.Fragments;

import android.app.Dialog;
import android.example.bobo.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RatingDialogFragment extends DialogFragment {

    private RatingBar ratingBar;
    private Button btnRate;

    public interface RatingListener {
        void onRatingSubmitted(float rating);
    }

    private RatingListener listener;

    public void setRatingListener(RatingListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rating, null);
        dialog.setContentView(view);

        // Ánh xạ view
        ratingBar = view.findViewById(R.id.ratingBar);
        btnRate = view.findViewById(R.id.btnRate);

        // Disable button khi chưa chọn rating
        btnRate.setEnabled(false);
        btnRate.setAlpha(0.5f);

        // Lắng nghe sự kiện thay đổi rating
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating > 0) {
                btnRate.setEnabled(true);
                btnRate.setAlpha(1.0f);
            } else {
                btnRate.setEnabled(false);
                btnRate.setAlpha(0.5f);
            }
        });

        // Xử lý khi nhấn nút Rate
        btnRate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRatingSubmitted(ratingBar.getRating());
            }
            dismiss();
        });

        return dialog;
    }
}
