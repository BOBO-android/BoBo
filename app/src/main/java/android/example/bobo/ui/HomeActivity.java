package android.example.bobo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.example.bobo.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Default Selected Item
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Badge for Notifications (Red Dot)
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.nav_noti);
        badge.setVisible(true);
        badge.setBackgroundColor(getResources().getColor(R.color.red));

        // Initialize custom views for navigation items
        setupCustomNavItems();

        // Handle Navigation Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_explore) {
                selectedFragment = new ExploreFragment();
            } else if (itemId == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.nav_order) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.nav_noti) {
                selectedFragment = new NotificationsFragment();
            }

            // Update icon rotation & arrow visibility
            updateNavIcons(itemId);

            // Load the selected fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        // Load Default Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new HomeFragment())
                .commit();
    }

    /**
     * Apply custom views for navigation items.
     */
    private void setupCustomNavItems() {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = getLayoutInflater().inflate(R.layout.custom_nav_item, null);

            ImageView icon = view.findViewById(R.id.nav_icon);
            ImageView arrow = view.findViewById(R.id.nav_arrow);

            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_home) {
                icon.setImageResource(R.drawable.ic_home);
                icon.setRotation(-35); // Default rotation for selected item
                arrow.setVisibility(View.VISIBLE);
            } else if (itemId == R.id.nav_explore) {
                icon.setImageResource(R.drawable.ic_explore);
                arrow.setVisibility(View.GONE);
            } else if (itemId == R.id.nav_cart) {
                icon.setImageResource(R.drawable.ic_cart);
                arrow.setVisibility(View.GONE);
            } else if (itemId == R.id.nav_order) {
                icon.setImageResource(R.drawable.ic_order);
                arrow.setVisibility(View.GONE);
            } else if (itemId == R.id.nav_noti) {
                icon.setImageResource(R.drawable.ic_noti);
                arrow.setVisibility(View.GONE);
            }

            menuItem.setActionView(view);
        }
    }

    /**
     * Update the icon rotation and arrow visibility when an item is selected.
     */
    private void updateNavIcons(int selectedItemId) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = menuItem.getActionView();

            if (view != null) {
                ImageView icon = view.findViewById(R.id.nav_icon);
                ImageView arrow = view.findViewById(R.id.nav_arrow);

                if (menuItem.getItemId() == selectedItemId) {
                    icon.animate().rotation(-12).setDuration(300).start(); // Rotate selected item
                    arrow.setVisibility(View.VISIBLE); // Show arrow
                } else {
                    icon.animate().rotation(0).setDuration(300).start(); // Reset rotation
                    arrow.setVisibility(View.GONE); // Hide arrow
                }
            }
        }
    }
}
