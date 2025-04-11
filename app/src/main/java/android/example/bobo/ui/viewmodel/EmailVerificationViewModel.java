package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ResendCodeRequest;
import android.example.bobo.data.model.VerifyEmailRequest;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerificationViewModel extends ViewModel {

    private final MutableLiveData<Boolean> verifySuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<String> resendSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorResendMessage = new MutableLiveData<>();

    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

    public LiveData<Boolean> getVerifySuccess() {
        return verifySuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getResendSuccess() {
        return resendSuccess;
    }

    public LiveData<String> getErrorResendMessage() {
        return errorResendMessage;
    }

    public void verifyEmail(String storeId, String codeId) {
        VerifyEmailRequest request = new VerifyEmailRequest(storeId, codeId);

        apiService.verifyEmail(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                verifySuccess.setValue(response.isSuccessful());
                if (!response.isSuccessful()) {
                    errorMessage.setValue("Mã xác minh không đúng hoặc đã hết hạn.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối đến máy chủ.");
            }
        });
    }

    public void resendVerificationCode(String email) {
        apiService.resendCode1(new ResendCodeRequest(email)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resendSuccess.setValue("Mã xác minh mới đã được gửi!");
                } else {
                    errorResendMessage.setValue("Không thể gửi lại mã. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối máy chủ.");
            }
        });
    }
}
