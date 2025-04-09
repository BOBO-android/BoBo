package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.repository.VerifyRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyAccountViewModel extends ViewModel {
    private final VerifyRepository verifyRepository;
    private final MutableLiveData<ApiResponse<java.lang.Object>> verifyResponseMutableLiveData;
    private final MutableLiveData<String> errorLiveData;

    public VerifyAccountViewModel() {
        verifyRepository = new VerifyRepository();
        verifyResponseMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void verify(String userName, String verificationCode) {
        verifyRepository.verify(userName, verificationCode, new VerifyRepository.OnVerifyListener() {
            @Override
            public void onSuccess(ApiResponse<java.lang.Object> apiResponse) {
                verifyResponseMutableLiveData.postValue(apiResponse);
            }

            @Override
            public void onFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }

    public LiveData<ApiResponse<java.lang.Object>> getVerifyResponseMutableLiveData() {
        return verifyResponseMutableLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}