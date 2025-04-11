package android.example.bobo.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.EmailVerificationViewModel;

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText[] codeInputs;
    private TextView resendText;
    private Button completeButton;
    private EmailVerificationViewModel viewModel;

    private String storeId;
    private String codeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        // Nhận storeId và codeId từ Intent
        storeId = getIntent().getStringExtra("storeId");
        codeId = getIntent().getStringExtra("codeId");

        if (storeId == null || codeId == null) {
            Toast.makeText(this, "Thiếu thông tin xác thực!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(EmailVerificationViewModel.class);

        // Ánh xạ View
        codeInputs = new EditText[]{
                findViewById(R.id.et_TextOtp1),
                findViewById(R.id.et_TextOtp2),
                findViewById(R.id.et_TextOtp3),
                findViewById(R.id.et_TextOtp4),
                findViewById(R.id.et_TextOtp5),
                findViewById(R.id.et_TextOtp6)
        };
        resendText = findViewById(R.id.txt_ViewResend);
        completeButton = findViewById(R.id.btn_Continue_Very);

        // Quan sát LiveData từ ViewModel
        observeViewModel();

        // Tự động chuyển focus
        setupOtpAutoMove();

        // Sự kiện "Gửi lại mã"
        resendText.setOnClickListener(v -> {
            Toast.makeText(this, "Đang gửi lại mã...", Toast.LENGTH_SHORT).show();
            // Gọi API resend nếu có
        });

        // Sự kiện "Xác nhận"
        completeButton.setOnClickListener(v -> {
            StringBuilder code = new StringBuilder();
            for (EditText input : codeInputs) {
                code.append(input.getText().toString().trim());
            }

            if (code.length() == 6) {
                viewModel.verifyEmail(storeId, codeId);
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ 6 chữ số.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getVerifySuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                Toast.makeText(this, "Xác minh email thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, StoreReviewActivity.class));
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, err -> {
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
            clearOtpInputs();
        });
    }

    private void setupOtpAutoMove() {
        for (int i = 0; i < codeInputs.length; i++) {
            final int index = i;
            codeInputs[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < codeInputs.length - 1) {
                        codeInputs[index + 1].requestFocus();
                    }
                }
            });
        }
    }

    private void clearOtpInputs() {
        for (EditText input : codeInputs) {
            input.setText("");
        }
        codeInputs[0].requestFocus();
    }
}
