package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ResendCodeResponse;
import android.example.bobo.data.model.ResetPasswordResponse;
import android.example.bobo.data.repository.CreateNewPasswordRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateNewPasswordViewModel extends ViewModel {
    private CreateNewPasswordRepository repository;
    private  MutableLiveData<ResetPasswordResponse> createNewPasswordMutableLiveData;
    private MutableLiveData<String> errorLiveData;

    public CreateNewPasswordViewModel(){
        repository = new CreateNewPasswordRepository();
        createNewPasswordMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<ResetPasswordResponse> getCreateNewPasswordMutableLiveData() {
        return createNewPasswordMutableLiveData;
    }

    public void resetPassword(String password, String token){
        repository.createNewPassword(password, token, new CreateNewPasswordRepository.OnCreateNewPasswordListener() {
            @Override
            public void onSuccess(ResetPasswordResponse response) {
                createNewPasswordMutableLiveData.postValue(response);
            }

            @Override
            public void onFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }
}
