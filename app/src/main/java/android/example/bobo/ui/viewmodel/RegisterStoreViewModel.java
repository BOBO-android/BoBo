package android.example.bobo.ui.viewmodel;

import android.app.Application;
import android.example.bobo.data.model.RegisterStoreRequest;
import android.example.bobo.data.repository.StoreRepository;
import android.example.bobo.data.repository.StoreRepositoryImpl;
import android.example.bobo.data.repository.UploadCallback;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;

public class RegisterStoreViewModel extends AndroidViewModel {

    private final MutableLiveData<String> storeName = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> address = new MutableLiveData<>();
    private final MutableLiveData<String> phone = new MutableLiveData<>();
    private final MutableLiveData<String> accountBank = new MutableLiveData<>();
    private final MutableLiveData<String> typeBank = new MutableLiveData<>();
    private final MutableLiveData<String> logoUrl = new MutableLiveData<>();
    private final MutableLiveData<String> licenseUrl = new MutableLiveData<>();
    private final StoreRepository storeRepository;
    private final MutableLiveData<Boolean> continueButtonClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerButtonClicked = new MutableLiveData<>();
    public MutableLiveData<Boolean> isContinueButtonEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> storeNameError = new MutableLiveData<>();
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<String> addressError = new MutableLiveData<>();
    private final MutableLiveData<String> phoneError = new MutableLiveData<>();
    private final MutableLiveData<String> accountBankError = new MutableLiveData<>();
    private final MutableLiveData<String> typeBankError = new MutableLiveData<>();
    private final MutableLiveData<String> logoError = new MutableLiveData<>();
    private final MutableLiveData<String> licenseError = new MutableLiveData<>();

    public LiveData<String> getStoreNameError() { return storeNameError; }
    public LiveData<String> getEmailError() { return emailError; }
    public LiveData<String> getAddressError() {
        return addressError;
    }
    public LiveData<String> getPhoneError() { return phoneError; }
    public LiveData<String> getAccountBankError() { return accountBankError; }
    public LiveData<String> getTypeBankError() { return typeBankError; }
    public LiveData<String> getLogoError() { return logoError; }
    public LiveData<String> getLicenseError() { return licenseError; }

    MutableLiveData<Boolean> isFormValid = new MutableLiveData<>();
    public LiveData<Boolean> getIsFormValid() {
        return isFormValid;
    }


    public RegisterStoreViewModel(@NonNull Application application) {
        super(application);
        this.storeRepository = new StoreRepositoryImpl(application.getApplicationContext());
    }

    public LiveData<String> getLogoUrl() {
        return logoUrl;
    }

    public LiveData<String> getLicenseUrl() {
        return licenseUrl;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final MutableLiveData<String> registerSuccessStoreId = new MutableLiveData<>();
    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public void onRegisterClick() {
        registerButtonClicked.setValue(true);
    }

    public LiveData<Boolean> getRegisterButtonClicked() {
        return registerButtonClicked;
    }

    public void onContinueClick() {
        continueButtonClicked.setValue(true);
    }

    public LiveData<Boolean> getContinueButtonClicked() {
        return continueButtonClicked;
    }

    public void uploadLogo(File logoFile) {
        storeRepository.uploadLogoImage(logoFile, new UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                logoUrl.postValue(imageUrl);
                checkIfContinueEnabled();
                updateLogo(imageUrl);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue("Lỗi upload logo: " + message);
            }
        });
    }

    public void uploadLicense(File licenseFile) {
        storeRepository.uploadLicenseImage(licenseFile, new UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                licenseUrl.postValue(imageUrl);
                checkIfContinueEnabled();
                updateLicense(imageUrl);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue("Lỗi upload license: " + message);
            }
        });
    }

    public void registerStore(RegisterStoreRequest request, String token) {
        Log.d("RegisterStore", "Calling API...");
        storeRepository.registerStore(token, request, new StoreRepository.RegisterCallback() {
            @Override
            public void onSuccess(String storeId) {
                Log.d("RegisterStore", "Register success, Store ID: " + storeId);
                registerSuccess.postValue(true);
                registerSuccessStoreId.postValue(storeId);  // 🔥 Gửi storeId về cho Activity
            }

            @Override
            public void onFailure(String error) {
                Log.e("RegisterStore", "Register failed: " + error);
                errorMessage.postValue(error);
            }
        });
    }


    public void checkIfContinueEnabled() {
        boolean isEnabled = storeName.getValue() != null && !storeName.getValue().isEmpty() &&
                email.getValue() != null && !email.getValue().isEmpty() &&
                address.getValue() != null && !address.getValue().isEmpty() &&
                phone.getValue() != null && !phone.getValue().isEmpty() &&
                accountBank.getValue() != null && !accountBank.getValue().isEmpty() &&
                typeBank.getValue() != null && !typeBank.getValue().isEmpty() &&
                logoUrl.getValue() != null && !logoUrl.getValue().isEmpty() &&
                licenseUrl.getValue() != null && !licenseUrl.getValue().isEmpty();

        isContinueButtonEnabled.setValue(isEnabled);
    };


    public void updateStoreName(String name) {
        storeName.setValue(name);
        checkIfContinueEnabled();
    }

    public void updateEmail(String email) {
        this.email.setValue(email);
        checkIfContinueEnabled();
    }

    public void updateAddress(String address) {
        this.address.setValue(address);
        checkIfContinueEnabled();
    }

    public void updatePhone(String phone) {
        this.phone.setValue(phone);
        checkIfContinueEnabled();
    }

    public void updateAccountBank(String accountBank) {
        this.accountBank.setValue(accountBank);
        checkIfContinueEnabled();
    }

    public void updateTypeBank(String typeBank) {
        this.typeBank.setValue(typeBank);
        checkIfContinueEnabled();
    }

    public void updateLogo(String logoUrl) {
        this.logoUrl.setValue(logoUrl);
        checkIfContinueEnabled();
    }

    public void updateLicense(String licenseUrl) {
        this.licenseUrl.setValue(licenseUrl);
        checkIfContinueEnabled();
    }

    public boolean validateInput(String name, String email, String address, String phoneNumber,
                                 String bankAccountNumber, String typeBank,
                                 String logoUrl, String businessLicenseUrl, String token) {
        boolean isValid = true;

        if (name == null || name.trim().isEmpty()) {
            storeNameError.setValue("Tên cửa hàng không được để trống");
            isValid = false;
        } else {
            storeNameError.setValue(null);
        }

        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setValue("Email không hợp lệ");
            isValid = false;
        } else {
            emailError.setValue(null);
        }

        if (address == null || address.trim().isEmpty()) {
            addressError.setValue("Địa chỉ không được để trống");
            isValid = false;
        } else {
            addressError.setValue(null);
        }

        if (phone.getValue() != null && phone.getValue().matches("^\\d{11,12}$")) {
            phoneError.setValue("Số điện thoại không hợp lệ");
            isValid = false;
        } else {
            phoneError.setValue(null);
        }

        if (storeName.getValue() == null || storeName.getValue().trim().isEmpty()){
            accountBankError.setValue("Số tài khoản không được để trống");
            isValid = false;
        } else {
            accountBankError.setValue(null);
        }

        if (typeBank == null || typeBank.trim().isEmpty()) {
            typeBankError.setValue("Tên ngân hàng không được để trống");
            isValid = false;
        } else {
            typeBankError.setValue(null);
        }

        if (logoUrl == null || logoUrl.trim().isEmpty()) {
            logoError.setValue("Vui lòng tải ảnh logo");
            isValid = false;
        } else {
            logoError.setValue(null);
        }

        String licenseValue = licenseUrl.getValue();
        if (licenseValue == null || licenseValue.trim().isEmpty()) {
            licenseError.setValue("Vui lòng tải ảnh giấy phép kinh doanh");
            isValid = false;
        } else {
            licenseError.setValue(null);
        }

        if (isValid) {
            isFormValid.setValue(true);

            RegisterStoreRequest request = new RegisterStoreRequest(
                    name,
                    address,
                    email,
                    phoneNumber,
                    bankAccountNumber,
                    typeBank,
                    logoUrl,
                    businessLicenseUrl
            );
            registerStore(request, token);
        }
        return isValid;
    }

}
