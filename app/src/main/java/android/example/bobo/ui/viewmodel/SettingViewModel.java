package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.repository.LogOutRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingViewModel extends ViewModel {
    private final LogOutRepository logOutRepository;
    private final MutableLiveData<String> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<String> logoutErrorMessage = new MutableLiveData<>();

    public SettingViewModel() {
        logOutRepository = new LogOutRepository();
    }

    public LiveData<String> getLogoutResult() {
        return logoutResult;
    }

    public LiveData<String> getLogoutErrorMessage() {
        return logoutErrorMessage;
    }

    public void logout(String token) {
        logOutRepository.logout(token, new LogOutRepository.OnLogoutListener() {
            @Override
            public void onSuccess(ApiResponse<Object> apiResponse) {
                String message = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Logout successful";
                logoutResult.postValue(message);
            }

            @Override
            public void onFailure(String error) {
                logoutErrorMessage.postValue(error);
            }
        });
    }
}