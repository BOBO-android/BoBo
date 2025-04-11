package android.example.bobo.data.repository;

public interface UploadCallback {
    void onSuccess(String imageUrl);
    void onError(String message);
}



