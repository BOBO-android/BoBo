package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.example.bobo.data.model.Coupon;
import android.example.bobo.ui.adapters.CouponAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AddCouponActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CouponAdapter adapter;
    private List<Coupon> couponList;
    private Coupon selectedCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        recyclerView = findViewById(R.id.recyclerViewCoupons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        couponList = getCoupons();
        adapter = new CouponAdapter(couponList, coupon -> selectedCoupon = coupon);
        recyclerView.setAdapter(adapter);

        ImageView btnClose = findViewById(R.id.btn_close_coupon);
        btnClose.setOnClickListener(v -> finish());

        Button btnSave = findViewById(R.id.btn_save_coupon);
        btnSave.setOnClickListener(v -> {
            if (selectedCoupon != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_COUPON", selectedCoupon);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng chọn một mã giảm giá!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Coupon> getCoupons() {
        List<Coupon> list = new ArrayList<>();
        list.add(new Coupon("WELCOME50", "Giảm 50% cho đơn hàng đầu tiên","food", 0.5, 1));
        list.add(new Coupon("FREESHIP", "Miễn phí vận chuyển", "ship", 1.0, 0));
        list.add(new Coupon("SALE20", "Giảm 20% cho đơn hàng trên 200K", "food",0.2,200));
        return list;
    }
}