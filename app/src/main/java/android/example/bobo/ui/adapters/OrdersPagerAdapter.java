package android.example.bobo.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import android.example.bobo.ui.view.Fragments.PendingApprovalFragment;
import android.example.bobo.ui.view.Fragments.ProcessingFragment;
import android.example.bobo.ui.view.OrdersActivity;

public class OrdersPagerAdapter extends FragmentStateAdapter {
    private final OrdersActivity activity; // Store reference to OrdersActivity
    private final String token;
    private final String storeId;

    public OrdersPagerAdapter(@NonNull OrdersActivity activity, String token, String storeId) {
        super(activity);
        this.activity = activity;
        this.token = token;
        this.storeId = storeId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new PendingApprovalFragment(activity, token, storeId);
        } else {
            return new ProcessingFragment(activity, token, storeId);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
