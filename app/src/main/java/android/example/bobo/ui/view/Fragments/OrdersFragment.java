package android.example.bobo.ui.view.Fragments;

import android.example.bobo.ui.adapters.OrdersPagerAdapter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.example.bobo.R;

public class OrdersFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrdersPagerAdapter ordersPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        ordersPagerAdapter = new OrdersPagerAdapter(this);
        viewPager.setAdapter(ordersPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Current");
            } else {
                tab.setText("Previous");
            }
        }).attach();

        return view;
    }
}
