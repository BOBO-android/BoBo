package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.LoginResponse;
import android.example.bobo.data.repository.LoginRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private  final LoginRepository repository;
    private  final MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private  final MutableLiveData<String> errorLiveData;

    public LoginViewModel(){
        repository = new LoginRepository();
        loginResponseMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData() {
        return loginResponseMutableLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void login(String email, String password){
        repository.login(email, password, new LoginRepository.OnLoginListener() {
            @Override
            public void OnSuccess(LoginResponse response) {
                loginResponseMutableLiveData.postValue(response);
            }

            @Override
            public void OnFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }
}
