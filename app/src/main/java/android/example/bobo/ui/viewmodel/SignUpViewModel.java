package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.RegisterResponse;
import android.example.bobo.data.repository.RegisterRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
    private RegisterRepository repository;
    private MutableLiveData<ApiResponse<RegisterResponse>> registerResponseMutableLiveData;
    private MutableLiveData<String> errorLiveData;

    public SignUpViewModel() {
        repository = new RegisterRepository();
        registerResponseMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ApiResponse<RegisterResponse>> getRegisterResponseMutableLiveData() {
        return registerResponseMutableLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void register(String fullName, String email, String phoneNumber, String password, String confirmPassword) {
        repository.register(fullName, email, phoneNumber, password, confirmPassword, new RegisterRepository.OnRegisterListener() {
            @Override
            public void onSuccess(ApiResponse<RegisterResponse> apiResponse) {
                registerResponseMutableLiveData.postValue(apiResponse);
            }

            @Override
            public void onFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }
}