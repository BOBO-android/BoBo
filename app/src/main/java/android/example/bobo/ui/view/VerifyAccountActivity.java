package android.example.bobo.ui.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.CheckValidCodeViewModel;
import android.example.bobo.ui.viewmodel.ResendCodeViewModel;
import android.example.bobo.ui.viewmodel.VerifyAccountViewModel;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

public class VerifyAccountActivity extends AppCompatActivity {
    private TextInputEditText code1, code2, code3, code4, code5, code6;
    private TextInputEditText [] codeInputs;
    private LinearLayout backContainer;
    private TextView descriptionEmailTV, resendTimeTV, resendTextTV, errorMessageTV;
    private String email, userName;
    private CountDownTimer countDownTimer;
    private View resendLine;
    private Button continueButton, errorbutton;
    private ResendCodeViewModel resendCodeViewModel;
    private VerifyAccountViewModel verifyAccountViewModel;

    @SuppressLint("LabelFor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        // anh xa
        descriptionEmailTV = findViewById(R.id.description_email);
        resendTimeTV = findViewById(R.id.resend_time);
        code1 = findViewById(R.id.code1);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);
        backContainer = findViewById(R.id.back_container);
        resendTextTV = findViewById(R.id.resend_text_TextView);
        resendLine = findViewById(R.id.resend_line_View);
        continueButton = findViewById(R.id.btn_continue);

        // tat button
        disableButtonContinue();
        //xu ly hien ten email
        Intent intent1 = getIntent();
        email = intent1.getStringExtra("email");

        if (email != null && !email.isEmpty()) {
            descriptionEmailTV.setText("Enter the verification code send to your email " + email);
        } else {
            descriptionEmailTV.setText("error");
        }
        codeInputs = new TextInputEditText[]{code1, code2, code3, code4, code5, code6};

         SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
         userName = prefs.getString("userName","");

        resendCodeViewModel = new ViewModelProvider(this).get(ResendCodeViewModel.class);
        verifyAccountViewModel = new ViewModelProvider(this).get(VerifyAccountViewModel.class);

        resendCodeViewModel.getResendCodeResponseLiveData().observe(this, response -> {
            showDiaLogSuccess(response.getMessage());
        });
        resendCodeViewModel.getErrorLiveData().observe(this, error -> {
            showDiaLog(error);
        });

        verifyAccountViewModel.getVerifyResponseMutableLiveData().observe(this , response -> {
            Intent intent = new Intent(VerifyAccountActivity.this, FinishCreateAccountActivity.class);
            startActivity(intent);
            finish();
        });
        verifyAccountViewModel.getErrorLiveData().observe(this, error -> {
            showDiaLog(error);
        });

        // xu ly text
        setupCodeInputs();

        // khoi tao dem thoi gian
        startCountdownTimer(20 * 1000);

        // xu ly back
        backContainer.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyAccountActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
        // xu ly resend
        resendTextTV.setOnClickListener( v -> {
            if (resendTextTV.isClickable())
            {
                startCountdownTimer(20  * 1000);
                resendCodeViewModel.resendCode(email);
            }

        });
        // xu ly continue
        continueButton.setOnClickListener(v -> {
            boolean isCode1Filled = code1.getText().toString().length() == 1;
            boolean isCode2Filled = code2.getText().toString().length() == 1;
            boolean isCode3Filled = code3.getText().toString().length() == 1;
            boolean isCode4Filled = code4.getText().toString().length() == 1;
            boolean isCode5Filled = code5.getText().toString().length() == 1;
            boolean isCode6Filled = code6.getText().toString().length() == 1;
            if (isCode1Filled && isCode2Filled && isCode3Filled && isCode4Filled && isCode5Filled && isCode6Filled) {
                StringBuilder codeBuilder = new StringBuilder();
                for (TextInputEditText codeInput : codeInputs) {
                    codeBuilder.append(codeInput.getText().toString().trim());
                }
                String codeVery = codeBuilder.toString();
                verifyAccountViewModel.verify(userName,codeVery);
            }
        });
    }

    // xu ly dem thoi gian
    private void startCountdownTimer(long millisInFuture){
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                resendTimeTV.setText(timeFormatted);
                resendTextTV.setClickable(false);
                resendTextTV.setFocusable(false);
                resendLine.setBackgroundResource(R.drawable.line_disable);
                resendTextTV.setTextColor(ContextCompat.getColor(VerifyAccountActivity.this, R.color.resend));
            }
            @Override
            public void onFinish() {
                resendTextTV.setClickable(true);
                resendTextTV.setFocusable(true);
                resendTextTV.setTextColor(ContextCompat.getColor(VerifyAccountActivity.this, R.color.olive_gray));
                resendLine.setBackgroundResource(R.drawable.line);
            }
        }.start();
    };

    private void enableButtonContinue(){
        continueButton.setClickable(true);
        continueButton.setFocusable(true);
        continueButton.setBackgroundResource(R.drawable.btn_next_background);
        continueButton.setTextColor(ContextCompat.getColor(VerifyAccountActivity.this, R.color.white));
    };
    private void disableButtonContinue(){
        continueButton.setClickable(false);
        continueButton.setFocusable(false);
        continueButton.setBackgroundResource(R.drawable.custom_button_disable);
        continueButton.setTextColor(ContextCompat.getColor(VerifyAccountActivity.this, R.color.light_gray));
    }
    // check full code
    private void checkCodeInput(){
        boolean isCode1Filled = code1.getText().toString().length() == 1;
        boolean isCode2Filled = code2.getText().toString().length() == 1;
        boolean isCode3Filled = code3.getText().toString().length() == 1;
        boolean isCode4Filled = code4.getText().toString().length() == 1;
        boolean isCode5Filled = code5.getText().toString().length() == 1;
        boolean isCode6Filled = code6.getText().toString().length() == 1;
        if (isCode1Filled && isCode2Filled && isCode3Filled && isCode4Filled && isCode5Filled && isCode6Filled) {
            enableButtonContinue();
        }else {
            disableButtonContinue();
        }
    };

    private void setupCodeInputs() {
        for (int i = 0; i < codeInputs.length; i++) {
            final int currentIndex = i;
            codeInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    checkCodeInput();

                    if (s.length() == 1 && currentIndex < codeInputs.length - 1) {
                        codeInputs[currentIndex + 1].requestFocus();
                    }

                    if (s.length() == 0 && currentIndex > 0) {
                        boolean isNextEmpty = (currentIndex == codeInputs.length - 1) ||
                                codeInputs[currentIndex + 1].getText().toString().isEmpty();
                        if (isNextEmpty) {
                            codeInputs[currentIndex - 1].requestFocus();
                        }
                    }
                }
            });
        }
    }

    private void showDiaLog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyAccountActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        errorMessageTV = dialogView.findViewById(R.id.error_message1);
        errorMessageTV.setText(message);
        errorbutton = dialogView.findViewById(R.id.btn_back_error);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        errorbutton.setOnClickListener( v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
    private void showDiaLogSuccess(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyAccountActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_successfully, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        errorMessageTV = dialogView.findViewById(R.id.success_TV);
        errorMessageTV.setText(message);
        errorbutton = dialogView.findViewById(R.id.btn_back_success);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        errorbutton.setOnClickListener( v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
