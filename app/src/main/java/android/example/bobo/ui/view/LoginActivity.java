package android.example.bobo.ui.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.ForgotPasswordViewModel;
import android.example.bobo.ui.viewmodel.LoginViewModel;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private Button loginButton, errorbutton;
    private TextView forgotPasswordTV, signUpTV, errorMessageTV;
    private LoginViewModel loginViewModel;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private LinearLayout backLL;
    private  boolean isPasswordVisible = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // anh xa giao dien
        emailInputLayout = findViewById(R.id.emailEditLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.btnLogin);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTextView);
        signUpTV = findViewById(R.id.signUpTextView);
        backLL = findViewById(R.id.back_container);

        // xu ly an hien pass
        passwordInputLayout.setEndIconOnClickListener( v -> {
            if (isPasswordVisible) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInputLayout.setEndIconDrawable(R.drawable.ic_eye_off);
                isPasswordVisible = false;
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordInputLayout.setEndIconDrawable(R.drawable.ic_eye_on);
                isPasswordVisible = true;
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        });


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        // phan hoi dang nhap
        loginViewModel.getLoginResponseMutableLiveData().observe(this, response -> {
                // luu token
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("access_token", response.getAccessToken());
                editor.putString("refresh_token", response.getRefreshToken());
                editor.putString("user_id", response.getUser().getId());
                editor.putString("username", response.getUser().getUsername());
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
        });
        loginViewModel.getErrorLiveData().observe(this, error -> {
//            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            showDiaLog(error.toString());
        });

        // phan hoi forgotpass
        forgotPasswordViewModel.getForgotPasswordResponseMutableLiveData().observe(this, response -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            intent.putExtra("email", emailEditText.getText().toString().trim());
            startActivity(intent);
            finish();
        });
        forgotPasswordViewModel.getErrorLiveData().observe(this, error -> {
            showDiaLog("Error: " + error);
        });

        // xu ly back
        backLL.setOnClickListener( v -> {
            Intent intent = new Intent(LoginActivity.this, Onboarding1.class);
            startActivity(intent);
            finish();
        });

        // xu ly forgot pass
        forgotPasswordTV.setOnClickListener( v -> {
            String email = emailEditText.getText().toString().trim();
            if(email.isEmpty()){
                showDiaLog("Email can not be empty");
            }else {
               forgotPasswordViewModel.forgotPassword(email);
            }

        });

        // xu ly skien nut login
        loginButton.setOnClickListener( v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if(email.isEmpty() || password.isEmpty())
            {
                showDiaLog("Email and Password can not be empty");
                return;
            }else {
                loginViewModel.login(email,password);
            }
        });

        // xu ly su kien sign up
        signUpTV.setOnClickListener( v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void showDiaLog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        errorMessageTV = dialogView.findViewById(R.id.error_message);
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
}