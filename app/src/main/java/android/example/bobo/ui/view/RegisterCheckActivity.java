package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterCheckActivity extends AppCompatActivity {

    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_check);

        btnContinue = findViewById(R.id.btn_Continue);

        // Xử lý sự kiện click của nút "Continue"
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterCheckActivity.this, RegisterInputInfoActivity.class);
                startActivity(intent);
            }
        });

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
