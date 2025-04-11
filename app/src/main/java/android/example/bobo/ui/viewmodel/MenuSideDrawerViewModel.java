package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.UserInformationResponse;
import android.example.bobo.data.repository.UserInformationRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuSideDrawerViewModel extends ViewModel {
    private final UserInformationRepository userInformationRepository;
    private final MutableLiveData<UserInformationResponse> userInfo = new MutableLiveData<>();
    private final MutableLiveData<String> userInfoErrorMessage = new MutableLiveData<>();

    public MenuSideDrawerViewModel() {
        userInformationRepository = new UserInformationRepository();
    }

    public LiveData<UserInformationResponse> getUserInfo() {
        return userInfo;
    }

    public LiveData<String> getUserInfoErrorMessage() {
        return userInfoErrorMessage;
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