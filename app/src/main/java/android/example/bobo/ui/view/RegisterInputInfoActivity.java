package android.example.bobo.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.example.bobo.R;
import android.example.bobo.data.model.RegisterStoreRequest;
import android.example.bobo.ui.viewmodel.RegisterStoreViewModel;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class RegisterInputInfoActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE_LOGO = 1001;
    private static final int REQUEST_PICK_IMAGE_LICENSE = 1002;

    private RegisterStoreViewModel viewModel;

    private Uri logoUri, licenseUri;
    private String uploadedLogoUrl, uploadedLicenseUrl;

    private ImageView imgLogoStore, imgLicense;
    private Button btnContinue;

    private EditText edtStoreName, edtEmail, edtAddress, edtPhone, edtAccountBank, edtTypeBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input_info);

        saveTestToken();
        initViews();
        setupViewModel();
        setupListeners();
        observeViewModel();
    }

    private void initViews() {
        imgLogoStore = findViewById(R.id.iv_store_logo);
        imgLicense = findViewById(R.id.iv_business_license);
        btnContinue = findViewById(R.id.btn_Continue);

        edtStoreName = findViewById(R.id.et_store_name);
        edtEmail = findViewById(R.id.et_email);
        edtAddress = findViewById(R.id.et_address);
        edtPhone = findViewById(R.id.et_phone);
        edtAccountBank = findViewById(R.id.et_number_bank);
        edtTypeBank = findViewById(R.id.et_bank);

        ImageView ivBack = findViewById(R.id.btn_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(RegisterStoreViewModel.class);
    }

    private void setupListeners() {
        imgLogoStore.setOnClickListener(v -> openImagePicker(REQUEST_PICK_IMAGE_LOGO));
        imgLicense.setOnClickListener(v -> openImagePicker(REQUEST_PICK_IMAGE_LICENSE));

        btnContinue.setOnClickListener(v -> {
            String name = edtStoreName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String accountBank = edtAccountBank.getText().toString().trim();
            String typeBank = edtTypeBank.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String token = prefs.getString("accessToken", "");

            viewModel.validateInput(
                    name, email, address, phone, accountBank, typeBank,
                    uploadedLogoUrl, uploadedLicenseUrl, token
            );

        });

        addTextWatcher(edtStoreName, viewModel::updateStoreName);
        addTextWatcher(edtEmail, viewModel::updateEmail);
        addTextWatcher(edtAddress, viewModel::updateAddress);
        addTextWatcher(edtPhone, viewModel::updatePhone);
        addTextWatcher(edtAccountBank, viewModel::updateAccountBank);
        addTextWatcher(edtTypeBank, viewModel::updateTypeBank);
    }

    private void addTextWatcher(EditText editText, java.util.function.Consumer<String> callback) {
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callback.accept(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });
    }

    private void observeViewModel() {
        viewModel.isContinueButtonEnabled.observe(this, isEnabled -> {
            btnContinue.setEnabled(Boolean.TRUE.equals(isEnabled));
            btnContinue.setBackgroundResource(isEnabled != null && isEnabled
                    ? R.drawable.rounded_button
                    : R.drawable.rounded_btn_disable);
        });

        viewModel.getLogoUrl().observe(this, url -> uploadedLogoUrl = url);
        viewModel.getLicenseUrl().observe(this, url -> uploadedLicenseUrl = url);

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                Log.e("UploadError", error);
            }
        });

        viewModel.getRegisterSuccess().observe(this, isSuccess -> {
            if (Boolean.TRUE.equals(isSuccess)) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, EmailVerificationActivity.class));
            } else {
                Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getStoreNameError().observe(this, edtStoreName::setError);
        viewModel.getEmailError().observe(this, edtEmail::setError);
        viewModel.getPhoneError().observe(this, edtPhone::setError);
        viewModel.getAccountBankError().observe(this, edtAccountBank::setError);
        viewModel.getTypeBankError().observe(this, edtTypeBank::setError);
        viewModel.getLogoError().observe(this, err -> {
            if (err != null && !err.isEmpty()) Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        });
        viewModel.getLicenseError().observe(this, err -> {
            if (err != null && !err.isEmpty()) Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
        });
    }

    private void openImagePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            if (requestCode == REQUEST_PICK_IMAGE_LOGO) {
                logoUri = selectedImage;
                imgLogoStore.setImageURI(logoUri);
                uploadImageFromUri(logoUri, "logo");
            } else if (requestCode == REQUEST_PICK_IMAGE_LICENSE) {
                licenseUri = selectedImage;
                imgLicense.setImageURI(licenseUri);
                uploadImageFromUri(licenseUri, "license");
            }
        } else {
            Toast.makeText(this, "Không có ảnh được chọn", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageFromUri(Uri imageUri, String imageType) {
        String path = getPathFromUri(this, imageUri);
        if (path == null) {
            Toast.makeText(this, "Không thể lấy đường dẫn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "Không tìm thấy file ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("logo".equals(imageType)) {
            viewModel.uploadLogo(file);
        } else if ("license".equals(imageType)) {
            viewModel.uploadLicense(file);
        }
    }

    public static String getPathFromUri(Context context, Uri uri) {
        File file = new File(context.getCacheDir(), System.currentTimeMillis() + "_temp_image");
        try (InputStream input = context.getContentResolver().openInputStream(uri);
             FileOutputStream output = new FileOutputStream(file)) {

            byte[] buffer = new byte[4096];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveTestToken() {
        String testToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRidmlldDIwMDRAZ21haWwuY29tIiwic3ViIjoiNjdmNmE1NzM1OWM0MGU0NzQ4Yjk1MDFmIiwiaWF0IjoxNzQ0MzY4Nzk4LCJleHAiOjE3NDQzNzA1OTh9.1-C_W_jttmpWKq7f4cVzXaVIXJP1Aea0vADga3lnyO8";
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("accessToken", testToken);
        editor.apply();
    }
}
