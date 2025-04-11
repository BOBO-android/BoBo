package android.example.bobo.ui.view;
import android.content.Intent;
import android.example.bobo.R;
import android.example.bobo.data.model.Coupon;
import android.example.bobo.data.model.PlaceOrderItem;
import android.example.bobo.ui.adapters.PlaceOrderAdapter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PlaceOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlaceOrderAdapter adapter;
    private List<PlaceOrderItem> orderList;
    private Double subTotal;
    private Double costCoupon = 0.0;
    private Double costDelivery = 3.77;
    private static final int COUPON_REQUEST_CODE = 100;
    private TextView couponText,costCouponTxt,totalPriceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        recyclerView = findViewById(R.id.recyclerViewPlaceOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = getIntent().getParcelableArrayListExtra("order_items");
        subTotal = getIntent().getDoubleExtra("total_price",0.0);

        if (orderList == null) {
            orderList = new ArrayList<>();
            Toast.makeText(this, "Không có sản phẩm nào!", Toast.LENGTH_SHORT).show();
        }

        TextView subTotalTextView = findViewById(R.id.txt_sub_total);
        subTotalTextView.setText(String.format("$%.2f", subTotal));

        TextView costDeliveryTxt = findViewById(R.id.txt_delivery);
        costDeliveryTxt.setText(String.format("$%.2f", costDelivery));

        costCouponTxt = findViewById(R.id.txt_coupon);
        costCouponTxt.setText(String.format("$%.2f", costCoupon));

        totalPriceView = findViewById(R.id.total_price);
        totalPriceView.setText(String.format("$%.2f",subTotal+costDelivery+costDelivery));

        couponText = findViewById(R.id.coupon_text_view);
        ImageView addCouponButton = findViewById(R.id.btn_add_coupon);

        addCouponButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCouponActivity.class);
            startActivityForResult(intent, COUPON_REQUEST_CODE);
        });

        Button continueButton = findViewById(R.id.btn_continue);

        continueButton.setOnClickListener(v ->{
            Intent intent = new Intent(PlaceOrderActivity.this, CheckoutActivity.class);
            intent.putExtra("subtotal", subTotal);
            intent.putExtra("coupon", costCoupon);
            intent.putExtra("delivery", costDelivery);
            startActivity(intent);
        });

        adapter = new PlaceOrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);

        ImageView btnBack = findViewById(R.id.btn_place_order_back);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUPON_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Coupon selectedCoupon = data.getParcelableExtra("SELECTED_COUPON");
            if (selectedCoupon != null) {
                couponText.setText(selectedCoupon.getName());
                if (selectedCoupon.getType().equals("ship")){
                    costCoupon = costDelivery*selectedCoupon.getValue();
                }
                else{
                   costCoupon = subTotal*selectedCoupon.getValue();
                }
                costCouponTxt.setText(String.format("$%.2f", (costCoupon)));
                totalPriceView.setText(String.format("$%.2f",subTotal-costCoupon+costDelivery));
            }
        }
    }
}

