package android.example.bobo.ui.view;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.example.bobo.R;

public class StoreReviewActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription;
    private Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_review);

        // Ánh xạ View
        btnComplete = findViewById(R.id.btn_complete_finish);

        // Làm đậm phần "Store info" trong tiêu đề
        String titleText = "Store info under review";
        SpannableString spannableString = new SpannableString(titleText);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, "Store info".length(), 0);
        tvTitle.setText(spannableString);

        // Xử lý sự kiện bấm nút "Complete"
        btnComplete.setOnClickListener(v -> {
            Toast.makeText(this, "Registration process completed!", Toast.LENGTH_LONG).show();
            // TODO: Chuyển sang màn hình tiếp theo (ví dụ: màn hình chính)
            finish();
        });
    }
}
