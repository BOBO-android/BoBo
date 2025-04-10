package android.example.bobo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.example.bobo.R;
import android.widget.ImageButton;

public class OrderPlacedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        ImageButton btnClose = findViewById(R.id.btn_close);
        Button btnTrackOrder = findViewById(R.id.btn_track_order);

        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(OrderPlacedActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        });
//        btnTrackOrder.setOnClickListener(v -> {
//            OrderTrackingBottomSheet bottomSheet = OrderTrackingBottomSheet.newInstance("1");
//            bottomSheet.show(getSupportFragmentManager());
//        });
    }
}
