package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.CheckValidCodeResponse;
import android.example.bobo.data.model.ForgotPasswordResponse;
import android.example.bobo.data.repository.CheckValidCodeRepository;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckValidCodeViewModel extends ViewModel {
    private CheckValidCodeRepository repository;
    private MutableLiveData<CheckValidCodeResponse> checkValidCodeResponseMutableLiveData;
    private  MutableLiveData<String> errorLiveData;

    public CheckValidCodeViewModel(){
        repository = new CheckValidCodeRepository();
        checkValidCodeResponseMutableLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }
    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<CheckValidCodeResponse> getCheckValidCodeResponseMutableLiveData() {
        return checkValidCodeResponseMutableLiveData;
    }
    public void checkValidCode(String email, String code){
        repository.checkValidCode(email, code, new CheckValidCodeRepository.OnCheckValidCodeListener() {
            @Override
            public void onSuccess(CheckValidCodeResponse response) {
                checkValidCodeResponseMutableLiveData.postValue(response);
            }

            @Override
            public void onFailure(String error) {
                errorLiveData.postValue(error);
            }
        });
    }
}
