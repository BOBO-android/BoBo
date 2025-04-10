package android.example.bobo.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.example.bobo.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADDRESS = 1001;
    private static final int REQUEST_CODE_PAYMENT = 1002;

    private TextView txtSubtotal, txtCoupon, txtDeliveryCharges, txtTotal, tvLocation, tvPayment;
    private Button btnPlaceOrder;
    private ImageView btnCheckoutBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        txtSubtotal = findViewById(R.id.sub_total_checkout);
        txtCoupon = findViewById(R.id.coupon_checkout);
        txtDeliveryCharges = findViewById(R.id.delivery_price_checkout);
        txtTotal = findViewById(R.id.total_price_checkout);
        btnPlaceOrder = findViewById(R.id.btn_place_order);
        btnCheckoutBack = findViewById(R.id.btnCheckoutBack);

        Intent intentPull = getIntent();
        double subtotal = intentPull.getDoubleExtra("subtotal", 0);
        double coupon = intentPull.getDoubleExtra("coupon", 0);
        double deliveryCharges = intentPull.getDoubleExtra("delivery", 3.99);
        double total = subtotal - coupon + deliveryCharges;

        txtSubtotal.setText("$" + String.format("%.2f", subtotal));
        txtCoupon.setText("-" + String.format("%.2f", coupon));
        txtDeliveryCharges.setText("+" + String.format("%.2f", deliveryCharges));
        txtTotal.setText("$" + String.format("%.2f", total));

        btnCheckoutBack.setOnClickListener(v -> finish());

        tvLocation = findViewById(R.id.tvLocation);
        tvPayment = findViewById(R.id.tvPayment);

        findViewById(R.id.layoutDeliverTo).setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, SelectAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADDRESS);
        });

        findViewById(R.id.layoutPayment).setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, SelectPaymentActivity.class);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        });

        btnPlaceOrder.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(CheckoutActivity.this, OrderPlacedActivity.class);
            confirmIntent.putExtra("total", total);
            startActivity(confirmIntent);
        });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_ADDRESS) {
                String selectedAddress = data.getStringExtra("selectedAddress");
                tvLocation.setText(selectedAddress);
            } else if (requestCode == REQUEST_CODE_PAYMENT) {
                String selectedPayment = data.getStringExtra("selectedPaymentMethod");
                tvPayment.setText(selectedPayment);
            }
        }
    }
}
