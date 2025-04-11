package android.example.bobo.ui.view;

import android.content.Intent;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.CreateNewPasswordViewModel;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateNewPasswordActivity extends AppCompatActivity {

    private  boolean isPasswordVisible = false;
    private TextInputLayout newPasswordInputLayout, rePasswordInputLayout;
    private TextInputEditText newPasswordEditText, rePasswordEditText;
    private TextView errorMessageTV;
    private Button continueButton, errorbutton;
    private CreateNewPasswordViewModel createNewPasswordViewModel;
    private LinearLayout back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        newPasswordInputLayout = findViewById(R.id.new_password_input_Layout);
        newPasswordEditText = findViewById(R.id.new_password_edit_text);
        rePasswordInputLayout = findViewById(R.id.re_password_input_layout);
        rePasswordEditText = findViewById(R.id.re_password_edit_text);
        continueButton = findViewById(R.id.continue_button);
        back = findViewById(R.id.back_container);

        // lay token
        String token = getIntent().getStringExtra("token");

        //tat nut continue
        continueButton.setClickable(false);
        continueButton.setFocusable(false);

        // xu ly an hien
        newPasswordInputLayout.setEndIconOnClickListener( v -> {
            if (isPasswordVisible) {
                newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newPasswordInputLayout.setEndIconDrawable(R.drawable.ic_eye_off);
                isPasswordVisible = false;
            } else {
                newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                newPasswordInputLayout.setEndIconDrawable(R.drawable.ic_eye_on);
                isPasswordVisible = true;
            }
            newPasswordEditText.setSelection(newPasswordEditText.getText().length());
        });
        rePasswordInputLayout.setEndIconOnClickListener( v -> {
            if (isPasswordVisible) {
                rePasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                rePasswordInputLayout.setEndIconDrawable(R.drawable.ic_eye_off);
                isPasswordVisible = false;
            } else {
                rePasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                rePasswordInputLayout.setEndIconDrawable(R.drawable.ic_eye_on);
                isPasswordVisible = true;
            }
            rePasswordEditText.setSelection(rePasswordEditText.getText().length());
        });

        // xu ly validate
        newPasswordInputLayout.setError(null);
        rePasswordInputLayout.setError(null);

        newPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                validateNewPassword(s.toString().trim());

            }
        });
        rePasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateRePassword(newPasswordEditText.getText().toString().trim(), s.toString().trim());
                checkValidationAndUpdateButton();
            }
        });

        //viewmodel
        createNewPasswordViewModel = new ViewModelProvider(this).get(CreateNewPasswordViewModel.class);

        // quan sat tu viewmodel
        createNewPasswordViewModel.getCreateNewPasswordMutableLiveData().observe(this , response ->{
            showDiaLogSuccess(response.getMessage());
        });
        createNewPasswordViewModel.getErrorLiveData().observe(this, error -> {
            showDiaLog(error);
        });

        // xu ly continue click
        continueButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String rePassword = rePasswordEditText.getText().toString().trim();
            if(validateAll(newPassword, rePassword)){
                createNewPasswordViewModel.resetPassword(newPassword,token);
            }
        });

        //xu ly back
        back.setOnClickListener( v -> {
            Intent intent = new Intent(CreateNewPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private boolean validateNewPassword(String password) {
        if (password.isEmpty()) {
            newPasswordInputLayout.setError("Password cannot be empty");
            return false;
        }

        if (password.length() < 6) {
            newPasswordInputLayout.setError("Password must be at least 6 characters");
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        if (!hasUppercase) {
            newPasswordInputLayout.setError("Password must contain at least 1 uppercase letter");
            return false;
        }

        boolean hasSpecialChar = password.matches(".*[@#$%^&+=!].*");
        if (!hasSpecialChar) {
            newPasswordInputLayout.setError("Password must contain at least 1 special character (e.g., @, #, $)");
            return false;
        }

        newPasswordInputLayout.setError(null);
        return true;
    }

    private boolean validateRePassword(String newPassword, String rePassword) {
        if (rePassword.isEmpty()) {
            rePasswordInputLayout.setError("Please confirm your password");
            return false;
        }

        if (!rePassword.equals(newPassword)) {
            rePasswordInputLayout.setError("Passwords do not match");
            return false;
        }

        rePasswordInputLayout.setError(null);
        return true;
    }
    private boolean validateAll(String newPassword, String rePassword) {
        if (!validateNewPassword(newPassword)) {
            return false;
        }
        return validateRePassword(newPassword, rePassword);
    }
    private void showDiaLog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewPasswordActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewPasswordActivity.this);
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
            Intent intent = new Intent(CreateNewPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        dialog.show();
    }
    private void enableButtonContinue(){
        continueButton.setClickable(true);
        continueButton.setFocusable(true);
        continueButton.setBackgroundResource(R.drawable.btn_next_background);
        continueButton.setTextColor(ContextCompat.getColor(CreateNewPasswordActivity.this, R.color.white));
    };
    private void disableButtonContinue(){
        continueButton.setClickable(false);
        continueButton.setFocusable(false);
        continueButton.setBackgroundResource(R.drawable.custom_button_disable);
        continueButton.setTextColor(ContextCompat.getColor(CreateNewPasswordActivity.this, R.color.light_gray));

    }private void checkValidationAndUpdateButton() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String rePassword = rePasswordEditText.getText().toString().trim();

        if (validateNewPassword(newPassword) && validateRePassword(newPassword, rePassword)) {
            enableButtonContinue();
        } else {
            disableButtonContinue();
        }
    }

}
