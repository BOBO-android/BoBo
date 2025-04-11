package android.example.bobo.ui.viewmodel;

import android.content.Context;
import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.UpdateAccountRequest;
import android.example.bobo.data.model.UserInformationResponse;
import android.example.bobo.data.repository.StoreRepository;
import android.example.bobo.data.repository.StoreRepositoryImpl;
import android.example.bobo.data.repository.UpdateAccountRepository;
import android.example.bobo.data.repository.UserInformationRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

public class MyAccountViewModel extends ViewModel {
    private final UpdateAccountRepository updateAccountRepository;
    private final UserInformationRepository userInformationRepository;
    private final StoreRepository storeRepository; // Thay ImageUploadRepository bằng StoreRepository
    private final MutableLiveData<String> updateResult = new MutableLiveData<>();
    private final MutableLiveData<String> updateErrorMessage = new MutableLiveData<>();
    private final MutableLiveData<UserInformationResponse> userInfo = new MutableLiveData<>();
    private final MutableLiveData<String> userInfoErrorMessage = new MutableLiveData<>();

    public MyAccountViewModel(Context context) {
        updateAccountRepository = new UpdateAccountRepository();
        userInformationRepository = new UserInformationRepository();
        storeRepository = new StoreRepositoryImpl(context); // Truyền Context vào StoreRepositoryImpl
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

    public void updateAccount(String token, String fullName, File imageFile, String phoneNumber, String address) {
//        if (imageFile != null) {
//            // Sử dụng uploadImage từ StoreRepository
//            storeRepository.uploadImage(imageFile, new StoreRepository.UploadCallback() {
//                @Override
//                public void onSuccess(String imageUrl) {
//                    // Gọi updateAccount với imageUrl
//                    UpdateAccountRequest request = new UpdateAccountRequest(fullName, imageUrl, phoneNumber, address);
//                    updateAccountRepository.updateAccount(token, request, new UpdateAccountRepository.OnUpdateAccountListener() {
//                        @Override
//                        public void onSuccess(ApiResponse<Object> apiResponse) {
//                            String message = apiResponse.getMessage() != null && !apiResponse.getMessage().isEmpty()
//                                    ? apiResponse.getMessage().get(0)
//                                    : "Update successful";
//                            updateResult.postValue(message);
//                        }
//
//                        @Override
//                        public void onFailure(String error) {
//                            updateErrorMessage.postValue(error);
//                        }
//                    });
//                }
//
//                @Override
//                public void onError(String error) {
//                    updateErrorMessage.postValue("Image upload failed: " + error);
//                }
//            });
//        } else {
//            // Nếu không có ảnh, gọi updateAccount với imageUrl là null
//            UpdateAccountRequest request = new UpdateAccountRequest(fullName, null, phoneNumber, address);
//            updateAccountRepository.updateAccount(token, request, new UpdateAccountRepository.OnUpdateAccountListener() {
//                @Override
//                public void onSuccess(ApiResponse<Object> apiResponse) {
//                    String message = apiResponse.getMessage() != null && !apiResponse.getMessage().isEmpty()
//                            ? apiResponse.getMessage().get(0)
//                            : "Update successful";
//                    updateResult.postValue(message);
//                }
//
//                @Override
//                public void onFailure(String error) {
//                    updateErrorMessage.postValue(error);
//                }
//            });
//        }
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