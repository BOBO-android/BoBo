package android.example.bobo.data.repository;

import android.example.bobo.data.model.RegisterStoreRequest;

import java.io.File;

public interface StoreRepository {
    interface RegisterCallback {
        void onSuccess(String storeId);
        void onFailure(String error);
    }

    void uploadLogoImage(File logoFile, UploadCallback callback);

    void uploadLicenseImage(File licenseFile, UploadCallback callback);

    void registerStore(String token, RegisterStoreRequest request, RegisterCallback callback);
}
