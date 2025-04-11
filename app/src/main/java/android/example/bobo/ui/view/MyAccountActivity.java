package android.example.bobo.ui.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.example.bobo.R;
import android.example.bobo.ui.viewmodel.MyAccountViewModel;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.function.Consumer;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountActivity extends AppCompatActivity {
    private TextView cancel, save, errorMessageTV;
    private Button errorbutton;
    private CircleImageView userAvatar;
    private MyAccountViewModel myAccountViewModel;
    private TextInputLayout nameTextInputLayout, numberPhoneTextInputLayout, addressTextInputLayout;
    private TextInputEditText nameTextInputEditText, numberPhoneTextInputEditText, addressTextInputEditText;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        cancel = findViewById(R.id.tv_cancel);
        save = findViewById(R.id.tv_save);
        userAvatar = findViewById(R.id.user_avatar);
        nameTextInputEditText = findViewById(R.id.name_text_input_edit_text);
        nameTextInputLayout = findViewById(R.id.name_text_input_layout);
        numberPhoneTextInputLayout = findViewById(R.id.number_phone_text_input_layout);
        numberPhoneTextInputEditText = findViewById(R.id.number_phone_text_input_edit_text);
        addressTextInputLayout = findViewById(R.id.address_text_input_layout);
        addressTextInputEditText = findViewById(R.id.address_text_input_edit_text);

        // Khởi tạo MyAccountViewModel với Context
        myAccountViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(MyAccountViewModel.class)) {
                    return (T) new MyAccountViewModel(MyAccountActivity.this);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        }).get(MyAccountViewModel.class);

        myAccountViewModel.getUserInfo().observe(this, userInfo -> {
            if (userInfo != null) {
                nameTextInputEditText.setText(userInfo.getFullName());
                numberPhoneTextInputEditText.setText(userInfo.getPhoneNumber());
                addressTextInputEditText.setText(userInfo.getAddress());
                if (userInfo.getImage() != null && !userInfo.getImage().isEmpty()) {
                    Glide.with(this)
                            .load(userInfo.getImage())
                            .placeholder(R.drawable.test_image)
                            .error(R.drawable.test_image)
                            .into(userAvatar);
                }
            } else {
                Toast.makeText(this, "User info is null", Toast.LENGTH_SHORT).show();
            }
        });

        myAccountViewModel.getUserInfoErrorMessage().observe(this, error -> {
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        });

        // xu ly update
        myAccountViewModel.getUpdateResult().observe(this, result -> {
            showDiaLog(result, true);
        });

        myAccountViewModel.getUpdateErrorMessage().observe(this, error -> {
            showDiaLog(error, false);
        });

        // token
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = preferences.getString("access_token", "");
        myAccountViewModel.getUserInformation(token);

        // validate
        addValidationTextWatcher(numberPhoneTextInputEditText, this::validatePhoneNumber);
        addValidationTextWatcher(nameTextInputEditText, this::validateFullName);
        addValidationTextWatcher(addressTextInputEditText, this::validateAddress);

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccountActivity.this, MenuSideDrawerActivity.class);
            startActivity(intent);
            finish();
        });

        save.setOnClickListener(v -> {
            String fullName = nameTextInputEditText.getText().toString().trim();
            String phoneNumber = numberPhoneTextInputEditText.getText().toString().trim();
            String address = addressTextInputEditText.getText().toString().trim();

            File imageFile = null;
            if (selectedImageUri != null) {
                if (isValidImageUri(selectedImageUri)) {
                    imageFile = getFileFromUri(selectedImageUri);
                    if (imageFile == null) {
                        Toast.makeText(this, "Failed to process the selected image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(this, "Invalid image selected", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (validateFullName(fullName) && validatePhoneNumber(phoneNumber) && validateAddress(address)) {
                myAccountViewModel.updateAccount(token, fullName, imageFile, phoneNumber, address);
            } else {
                Toast.makeText(this, "Please fix the errors before saving", Toast.LENGTH_SHORT).show();
            }
        });

        userAvatar.setOnClickListener(v -> {
            // kiem tra android 10 tro len
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                openGallery();
            } else {
                // kiem tra quyen truy cap cho Android 9 tro xuong
                if (checkStoragePermission()) {
                    openGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
    }

    private boolean validateFullName(String fullName) {
        if (fullName.isEmpty()) {
            nameTextInputLayout.setError("Full name cannot be empty");
            return false;
        }
        String namePattern = "^\\p{L}+([ '-]\\p{L}+)*$";
        if (!fullName.matches(namePattern)) {
            nameTextInputLayout.setError("Full name can only contain letters, spaces, hyphens, or apostrophes");
            return false;
        }
        nameTextInputLayout.setError(null);
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

    private boolean validateAddress(String address) {
        if (address.isEmpty()) {
            addressTextInputLayout.setError("Phone number cannot be empty");
            return false;
        }

        addressTextInputLayout.setError(null);
        return true;
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

    // kiem tra quyen
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    // yc truy cap bo nho
    private void requestStoragePermission() {
        // kiem tra xem tu choi quyen truoc do chua
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Storage permission is needed to access your gallery.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE
        );
    }

    // xu ly quyen
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied! Cannot access gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // mo bo suu tap
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // xu ly ket qua tu bo suu tap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                userAvatar.setImageURI(selectedImageUri);
            } else {
                Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDiaLog(String message, boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        errorMessageTV = dialogView.findViewById(R.id.error_message); // Thêm dòng này để gán errorMessageTV
        errorMessageTV.setText(message); // Đặt message cho errorMessageTV
        errorbutton = dialogView.findViewById(R.id.btn_back_error);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        errorbutton.setOnClickListener(v -> {
            dialog.dismiss();
            if (isSuccess) {
                Intent intent = new Intent(MyAccountActivity.this, MenuSideDrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    // Kiểm tra URI ảnh hợp lệ
    private boolean isValidImageUri(Uri uri) {
        if (uri == null) {
            return false;
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                inputStream.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Chuyển URI thành File
    private File getFileFromUri(Uri uri) {
        try {
            String fileName = "temp_image.jpg";
            File file = new File(getCacheDir(), fileName);
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}