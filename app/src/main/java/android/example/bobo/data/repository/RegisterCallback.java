package android.example.bobo.data.repository;

public interface RegisterCallback {
    void onSuccess(String storeId);
    void onFailure(String error);
}

