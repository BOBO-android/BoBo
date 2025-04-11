package android.example.bobo.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.example.bobo.data.model.UploadResponse;
import android.example.bobo.data.repository.ImageRepository;
import android.example.bobo.utils.TokenManager;


import java.io.File;

public class ImageViewModel extends AndroidViewModel {
    private final ImageRepository imageRepository;

    private final MutableLiveData<UploadResponse> uploadResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public ImageViewModel(@NonNull Application application) {
        super(application);
        imageRepository = new ImageRepository();
    }

    public LiveData<UploadResponse> getUploadResult() {
        return uploadResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void uploadImage(File imageFile, String token) {
        isLoading.setValue(true);

        imageRepository.uploadImage(token, imageFile, new ImageRepository.UploadImageCallback() {
            @Override
            public void onSuccess(UploadResponse response) {
                uploadResult.postValue(response);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
                isLoading.postValue(false);
            }
        });
    }
}
