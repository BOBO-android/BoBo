package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ForgotPasswordResponse;
import android.example.bobo.data.model.LoginResponse;
import android.example.bobo.data.repository.ForgotPasswordRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgotPasswordViewModel  extends ViewModel {
    private ForgotPasswordRepository repository;
    private  MutableLiveData<ForgotPasswordResponse> forgotPasswordResponseMutableLiveData;
    private  MutableLiveData<String> errorLiveData;

    public ForgotPasswordViewModel(){
        repository = new ForgotPasswordRepository();
        forgotPasswordResponseMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ForgotPasswordResponse> getForgotPasswordResponseMutableLiveData() {
        return forgotPasswordResponseMutableLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void forgotPassword(String email) {
        repository.forgotPassword(email, new ForgotPasswordRepository.OnForgotPasswordListener() {
            @Override
            public void onSuccess(ForgotPasswordResponse response) {
                forgotPasswordResponseMutableLiveData.postValue(response);
            }

            @Override
            public void onFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }
}
