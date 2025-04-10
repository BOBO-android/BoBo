package android.example.bobo.ui.adapters;

import android.example.bobo.ui.view.Fragments.CurrentOrdersFragment;
import android.example.bobo.ui.view.Fragments.PreviousOrdersFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter {
    public OrdersPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new CurrentOrdersFragment();
        } else {
            return new PreviousOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
