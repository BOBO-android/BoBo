package android.example.bobo.ui.view.Fragments;

import android.content.Intent;
import android.example.bobo.R;
import android.example.bobo.data.model.CartItem;
import android.example.bobo.data.model.PlaceOrderItem;
import android.example.bobo.data.repository.CartRepository;
import android.example.bobo.ui.adapters.CartAdapter;
//import android.example.bobo.ui.view.LoginActivity;
import android.example.bobo.ui.view.PlaceOrderActivity;
import android.example.bobo.utils.TokenManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnQuantityChangeListener {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView totalPrice;
    private Double totalP = 0.0;
    private Button btnProceed;
    private TextView tvEmpty;
    private ProgressBar progressBar;

    // Cart Empty View
    private View emptyCartView;
    private Button btnExplore;

    // Main Cart View
    private View cartContentView;

    // Repository
    private CartRepository cartRepository;
    private TokenManager tokenManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartRepository = new CartRepository();
        tokenManager = new TokenManager(getContext());

        cartRepository.setAuthToken(tokenManager.getToken());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Ánh xạ view chính
        recyclerView = view.findViewById(R.id.recycler_cart);
        totalPrice = view.findViewById(R.id.total_price);
        btnProceed = view.findViewById(R.id.btn_proceed);
        progressBar = view.findViewById(R.id.progress_bar);
        cartContentView = view.findViewById(R.id.cart_content_layout);

        // Inflate và thiết lập view giỏ hàng trống
        emptyCartView = inflater.inflate(R.layout.layout_empty_cart, container, false);
        ((ViewGroup) view).addView(emptyCartView);
        emptyCartView.setVisibility(View.GONE);

        btnExplore = emptyCartView.findViewById(R.id.btn_explore);
        btnExplore.setOnClickListener(v -> {
            // Chuyển đến trang Explore/Home
            navigateToExplore();
        });

        // Thiết lập RecyclerView
        cartAdapter = new CartAdapter(cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartAdapter);

        // Xử lý sự kiện khi nhấn nút thanh toán
        btnProceed.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                proceedToCheckout();
            } else {
                Toast.makeText(getContext(), "Giỏ hàng trống, không thể thanh toán", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Quan sát dữ liệu từ repository
        observeCartData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Làm mới giỏ hàng mỗi khi quay lại fragment
        refreshCart();
    }

    // Phương thức làm mới giỏ hàng
    private void refreshCart() {
        cartRepository.refreshCart();
    }

    private void observeCartData() {
        // Quan sát danh sách giỏ hàng
        cartRepository.getCartItems().observe(getViewLifecycleOwner(), items -> {
            cartItems.clear();
            if (items != null) {
                cartItems.addAll(items);
                cartAdapter.notifyDataSetChanged();
            }
        });

        // Quan sát tổng giá
        cartRepository.getTotalPrice().observe(getViewLifecycleOwner(), price -> {
            totalP = price;
            totalPrice.setText("$" + String.format("%.2f", price));
        });

        // Quan sát trạng thái loading
        cartRepository.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Quan sát trạng thái giỏ hàng trống
        cartRepository.isEmpty().observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty) {
                showEmptyCartView();
            } else {
                showCartContentView();
            }
        });
    }

    private void showEmptyCartView() {
        if (emptyCartView != null && cartContentView != null) {
            emptyCartView.setVisibility(View.VISIBLE);
            cartContentView.setVisibility(View.GONE);
        }
    }

    private void showCartContentView() {
        if (emptyCartView != null && cartContentView != null) {
            emptyCartView.setVisibility(View.GONE);
            cartContentView.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToExplore() {
        if (getActivity() != null) {
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_explore);
        }
    }

    @Override
    public void onQuantityChanged(String foodId, int newQuantity) {
        if (newQuantity == 0) {
            cartRepository.removeItem(foodId);
        } else {
            cartRepository.updateQuantity(foodId, newQuantity);
        }
    }

    private void proceedToCheckout() {
        cartRepository.proceedToCheckout(new CartRepository.OnCheckoutListener() {
            @Override
            public void onSuccess(String message) {
                // Tạo danh sách PlaceOrderItem từ CartItem
                ArrayList<PlaceOrderItem> orderItems = new ArrayList<>();
                for (CartItem cartItem : cartItems) {
                    PlaceOrderItem orderItem = new PlaceOrderItem(
                            cartItem.getId(),
                            cartItem.getName(),
                            cartItem.getPrice(),
                            cartItem.getQuantity(),
                            cartItem.getImageUrl()
                    );
                    orderItems.add(orderItem);
                }

                Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
                intent.putParcelableArrayListExtra("order_items", orderItems);
                intent.putExtra("total_price", totalP);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}