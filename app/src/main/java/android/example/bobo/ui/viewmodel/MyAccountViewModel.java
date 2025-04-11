package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.UpdateAccountRequest;
import android.example.bobo.data.model.UserInformationResponse;
import android.example.bobo.data.repository.UpdateAccountRepository;
import android.example.bobo.data.repository.UserInformationRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAccountViewModel extends ViewModel {
    private final UpdateAccountRepository updateAccountRepository;
    private final UserInformationRepository userInformationRepository;
    private final MutableLiveData<String> updateResult = new MutableLiveData<>();
    private final MutableLiveData<String> updateErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<UserInformationResponse> userInfo = new MutableLiveData<>();
    private final MutableLiveData<String> userInfoErrorMessage = new MutableLiveData<>();

    public MyAccountViewModel() {
        updateAccountRepository = new UpdateAccountRepository();
        userInformationRepository = new UserInformationRepository();
    }


    public LiveData<String> getUpdateResult() {
        return updateResult;
    }

    public LiveData<String> getUpdateErrorMessage() {
        return updateErrorMessage;
    }


    public LiveData<UserInformationResponse> getUserInfo() {
        return userInfo;
    }

    public LiveData<String> getUserInfoErrorMessage() {
        return userInfoErrorMessage;
    }


    public void updateAccount(String token, String fullName, String uriImage, String phoneNumber, String address) {
        UpdateAccountRequest request = new UpdateAccountRequest(fullName, uriImage, phoneNumber, address);
        updateAccountRepository.updateAccount(token, request, new UpdateAccountRepository.OnUpdateAccountListener() {
            @Override
            public void onSuccess(ApiResponse<Object> apiResponse) {
                updateResult.postValue(apiResponse.getMessage());
            }

            @Override
            public void onFailure(String error) {
                updateErrorMessage.postValue(error);
            }
        });
    }

    public void getUserInformation(String token) {
        userInformationRepository.getUserInformation(token, new UserInformationRepository.OnUserInformationListener() {
            @Override
            public void onSuccess(ApiResponse<UserInformationResponse> apiResponse) {
                userInfo.postValue(apiResponse.getData());
            }

            @Override
            public void onFailure(String error) {
                userInfoErrorMessage.postValue(error);
            }
        });
    }
}