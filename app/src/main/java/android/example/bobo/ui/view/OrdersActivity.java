package android.example.bobo.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.example.bobo.R;
import android.example.bobo.ui.adapters.OrdersPagerAdapter;

public class OrdersActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private View rootView;

    private String token;
    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // Lấy token và storeId từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        token = preferences.getString("access_token", "");
        storeId = preferences.getString("user_id", "");


        rootView = findViewById(R.id.ordersRootView);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Set up ViewPager Adapter
        OrdersPagerAdapter adapter = new OrdersPagerAdapter(this, token, storeId); // Pass activity
        viewPager.setAdapter(adapter);

        // Attach TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setCustomView(getTabView(position));
        }).attach();

        // Apply custom style on tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.tab_text);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.tab_text);
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setTextColor(getResources().getColor(R.color.gray));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // Animation methods for bottom sheet interaction
    public void zoomOutOrdersActivity() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rootView, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rootView, "scaleY", 1f, 0.9f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(rootView, "translationY", 0f, -50f);

        scaleX.setDuration(200);
        scaleY.setDuration(200);
        translationY.setDuration(200);

        scaleX.start();
        scaleY.start();
        translationY.start();
    }

    public void restoreOrdersActivity() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rootView, "scaleX", 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rootView, "scaleY", 0.9f, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(rootView, "translationY", -50f, 0f);

        scaleX.setDuration(200);
        scaleY.setDuration(200);
        translationY.setDuration(200);

        scaleX.start();
        scaleY.start();
        translationY.start();
    }

    // Custom Tab View with text and badge
    @SuppressLint("SetTextI18n")
    private View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textView = view.findViewById(R.id.tab_text);
        View badge = view.findViewById(R.id.badge);

        if (position == 0) {
            textView.setText("Pending approval");
            badge.setVisibility(View.VISIBLE); // Show badge
        } else {
            textView.setText("Processing");
            badge.setVisibility(View.GONE);
        }

        return view;
    }
}
