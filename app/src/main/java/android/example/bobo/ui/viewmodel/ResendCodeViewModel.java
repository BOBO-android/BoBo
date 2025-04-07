package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.ResendCodeResponse;
import android.example.bobo.data.repository.ResendCodeRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResendCodeViewModel extends ViewModel {
    private final ResendCodeRepository repository;
    private final MutableLiveData<ApiResponse<ResendCodeResponse>> resendCodeResponseLiveData;
    private final MutableLiveData<String> errorLiveData;

    public ResendCodeViewModel() {
        repository = new ResendCodeRepository();
        resendCodeResponseLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ApiResponse<ResendCodeResponse>> getResendCodeResponseLiveData() {
        return resendCodeResponseLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void resendCode(String email) {
        repository.resendCode(email, new ResendCodeRepository.OnResendCodeListener() {
            @Override
            public void onSuccess(ApiResponse<ResendCodeResponse> apiResponse) {
                resendCodeResponseLiveData.postValue(apiResponse);
            }

            @Override
            public void onFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }
}
