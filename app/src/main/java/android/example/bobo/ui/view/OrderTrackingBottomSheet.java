package android.example.bobo.ui.view;

import android.example.bobo.ui.view.Fragments.OrderStatusFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.example.bobo.R;

public class OrderTrackingBottomSheet extends BottomSheetDialogFragment {

    private ViewPager2 viewPager;
    private String orderId;

    public static OrderTrackingBottomSheet newInstance(String orderId) {
        OrderTrackingBottomSheet fragment = new OrderTrackingBottomSheet();
        Bundle args = new Bundle();
        args.putString("ORDER_ID", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);

        if (getArguments() != null) {
            orderId = getArguments().getString("ORDER_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_order_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager();

        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        view.post(() -> {
            behavior.setPeekHeight(view.getMeasuredHeight());
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    private void setupViewPager() {
        OrderStatusPagerAdapter adapter = new OrderStatusPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);
    }

    // Adapter cho ViewPager2 để hiển thị các trạng thái đơn hàng
    private class OrderStatusPagerAdapter extends FragmentStateAdapter {

        public OrderStatusPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull androidx.lifecycle.Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return OrderStatusFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return 3; // Ba trạng thái: Preparing, On Way, Delivered
        }
    }

    // Hiển thị bottom sheet từ một Activity hoặc Fragment
    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "OrderTrackingBottomSheet");
    }
}