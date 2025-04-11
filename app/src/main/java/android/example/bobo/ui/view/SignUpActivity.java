package android.example.bobo.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.SignUpViewModel;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.Consumer;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText passwordTextInputEditText, confirmPasswordTextInputEditText, fullNameTextInputEditText, emailTextInputEditText, numberPhoneTextInputEditText;
    private TextInputLayout passwordTextInputLayout, confirmPasswordTextInputLayout, numberPhoneTextInputLayout, emailTextInputLayout, fullnameTextInputLayout;
    private CheckBox checkBox;
    private TextView errorMessageTV, signInTV;
    private Button createAccountButton, errorbutton;
    private boolean isPasswordVisible = false;
    private SignUpViewModel signUpViewModel;
    private LinearLayout back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //khoi tao viewmodel
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        // anh xa
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        passwordTextInputEditText = findViewById(R.id.password_text_input_edit_text);
        confirmPasswordTextInputLayout = findViewById(R.id.confirm_password_text_input_layout);
        confirmPasswordTextInputEditText = findViewById(R.id.confirm_password_text_input_edit_text);
        fullNameTextInputEditText = findViewById(R.id.full_name_text_input_edit_text);
        fullnameTextInputLayout = findViewById(R.id.full_name_text_input_layout);
        emailTextInputEditText = findViewById(R.id.email_text_input_edit_text);
        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        numberPhoneTextInputEditText = findViewById(R.id.number_phone_text_input_edit_text);
        numberPhoneTextInputLayout = findViewById(R.id.number_phone_text_input_layout);
        checkBox = findViewById(R.id.checkbox);
        signInTV = findViewById(R.id.sign_in_text_view);
        createAccountButton = findViewById(R.id.create_account_button);
        back = findViewById(R.id.back_container);

        // xu ly an hien pass
        passwordTextInputLayout.setEndIconOnClickListener( v -> {
            if (isPasswordVisible) {
                passwordTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_eye_off);
                isPasswordVisible = false;
            } else {
                passwordTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_eye_on);
                isPasswordVisible = true;
            }
            passwordTextInputEditText.setSelection(passwordTextInputEditText.getText().length());
        });

        confirmPasswordTextInputLayout.setEndIconOnClickListener( v -> {
            if (isPasswordVisible) {
                confirmPasswordTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirmPasswordTextInputLayout.setEndIconDrawable(R.drawable.ic_eye_off);
                isPasswordVisible = false;
            } else {
                confirmPasswordTextInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                confirmPasswordTextInputLayout.setEndIconDrawable(R.drawable.ic_eye_on);
                isPasswordVisible = true;
            }
            confirmPasswordTextInputEditText.setSelection(confirmPasswordTextInputEditText.getText().length());
        });


        // xu ly validate
        addValidationTextWatcher(fullNameTextInputEditText, this::validateFullName);
        addValidationTextWatcher(emailTextInputEditText, this::validateEmail);
        addValidationTextWatcher(passwordTextInputEditText, this::validatePassword);
        addValidationTextWatcher(numberPhoneTextInputEditText, this::validatePhoneNumber);
        addValidationTextWatcher(confirmPasswordTextInputEditText, confirmPassword ->
                validateConfirmPassword(passwordTextInputEditText.getText().toString().trim(), confirmPassword));

        //viewmodel
        signUpViewModel.getRegisterResponseMutableLiveData().observe(this, response -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userId", response.getData().getId());
            editor.putString("userName", response.getData().getUserName());
            editor.apply();

            showDiaLogSuccess(response.getMessage());

        });
        signUpViewModel.getErrorLiveData().observe(this, error -> {
            showDiaLog(error);
        });


        // xu ly checkbox
        checkBox.setOnClickListener( v -> {
            if (checkBox.isChecked())
            {
                checkBox.setForeground(ContextCompat.getDrawable(this, R.drawable.custom_checkmark));
            }else{
                checkBox.setForeground(null);
            }
            checkUpdateButton();
        });

        // xu ly nut
        createAccountButton.setOnClickListener( v -> {
                String fullName = fullNameTextInputEditText.getText().toString().trim();
                String email = emailTextInputEditText.getText().toString().trim();
                String phoneNumber = numberPhoneTextInputEditText.getText().toString().trim();
                String password = passwordTextInputEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordTextInputEditText.getText().toString().trim();
            if (validateFullName(fullName) &&
                    validateEmail(email) &&
                    validatePhoneNumber(phoneNumber) &&
                    validatePassword(password) &&
                    validateConfirmPassword(password, confirmPassword) &&
                    checkBox.isChecked()) {
               signUpViewModel.register(fullName,email,phoneNumber,password,confirmPassword);
            }

        });

         //xu ly sign In
        signInTV.setOnClickListener( v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // xu ly back
        back.setOnClickListener( v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateFullName(String fullName) {
        if (fullName.isEmpty()) {
            fullnameTextInputLayout.setError("Full name cannot be empty");
            return false;
        }
        String namePattern = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        if (!fullName.matches(namePattern)) {
            fullnameTextInputLayout.setError("Full name can only contain letters, spaces, hyphens, or apostrophes");
            return false;
        }
        fullnameTextInputLayout.setError(null);
        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailTextInputLayout.setError("Email cannot be empty");
            return false;
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailPattern)) {
            emailTextInputLayout.setError("Invalid email address (e.g., example@domain.com)");
            return false;
        }

        emailTextInputLayout.setError(null);
        return true;
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            numberPhoneTextInputLayout.setError("Phone number cannot be empty");
            return false;
        }

        String phonePattern = "^(\\+84|0)[0-9]{9}$";
        if (!phoneNumber.matches(phonePattern)) {
            numberPhoneTextInputLayout.setError("Invalid phone number (e.g., 0123456789 or +84123456789)");
            return false;
        }

        numberPhoneTextInputLayout.setError(null);
        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            passwordTextInputLayout.setError("Password cannot be empty");
            return false;
        }

        if (password.length() < 6) {
            passwordTextInputLayout.setError("Password must be at least 6 characters");
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        if (!hasUppercase) {
            passwordTextInputLayout.setError("Password must contain at least 1 uppercase letter");
            return false;
        }

        boolean hasSpecialChar = password.matches(".*[@#$%^&+=!].*");
        if (!hasSpecialChar) {
            passwordTextInputLayout.setError("Password must contain at least 1 special character (e.g., @, #, $)");
            return false;
        }

        passwordTextInputLayout.setError(null);
        return true;
    }

    private boolean validateConfirmPassword(String newPassword, String rePassword) {
        if (rePassword.isEmpty()) {
            confirmPasswordTextInputLayout.setError("Please confirm your password");
            return false;
        }

        if (!rePassword.equals(newPassword)) {
            confirmPasswordTextInputLayout.setError("Passwords do not match");
            return false;
        }

        confirmPasswordTextInputLayout.setError(null);
        return true;
    }
    private boolean validateAll(String newPassword, String rePassword) {
        if (!validatePassword(newPassword)) {
            return false;
        }
        return validateConfirmPassword(newPassword, rePassword);
    }
    private void enableCreateAccountButton(){
        createAccountButton.setClickable(true);
        createAccountButton.setFocusable(true);
        createAccountButton.setBackgroundResource(R.drawable.btn_next_background);
        createAccountButton.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.white));
    };
    private void disableCreateAccountButton() {
        createAccountButton.setClickable(false);
        createAccountButton.setFocusable(false);
        createAccountButton.setBackgroundResource(R.drawable.custom_button_disable);
        createAccountButton.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.light_gray));
    }

    private void addValidationTextWatcher(TextInputEditText editText, Consumer<String> validationFunction) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validationFunction.accept(s.toString().trim());

            }
        });
    }
    private void showDiaLog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
            Intent intent = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
            intent.putExtra("email", emailTextInputEditText.getText().toString().trim());
            startActivity(intent);
            startActivity(intent);
            finish();
        });
        dialog.show();
    }

    private void checkUpdateButton() {
        String fullName = fullNameTextInputEditText.getText().toString().trim();
        String email = emailTextInputEditText.getText().toString().trim();
        String phoneNumber = numberPhoneTextInputEditText.getText().toString().trim();
        String password = passwordTextInputEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordTextInputEditText.getText().toString().trim();

        if (validateFullName(fullName) &&
                validateEmail(email) &&
                validatePhoneNumber(phoneNumber) &&
                validatePassword(password) &&
                validateConfirmPassword(password, confirmPassword) &&
                checkBox.isChecked()) {
            enableCreateAccountButton();
        } else {
            disableCreateAccountButton();
        }
    }
}
