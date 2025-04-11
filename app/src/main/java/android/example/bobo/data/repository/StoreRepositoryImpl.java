package android.example.bobo.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.bobo.data.model.RegisterStoreRequest;
import android.example.bobo.data.model.RegisterStoreResponse;
import android.example.bobo.data.repository.RegisterCallback;
import android.example.bobo.data.model.UploadResponse;
import android.example.bobo.data.model.VerifyEmailRequest;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.ImageService;
import android.example.bobo.network.RetrofitClient;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreRepositoryImpl implements StoreRepository {

    private final Context context;
    private final ImageService imageService;
    private final ApiService apiService;

    public StoreRepositoryImpl(Context context) {
        this.context = context;
        this.imageService = RetrofitClient.getInstance().create(ImageService.class);
        this.apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    @Override
    public void uploadLogoImage(File logoFile, UploadCallback callback) {
        uploadImage(logoFile, callback);
    }

    @Override
    public void uploadLicenseImage(File licenseFile, UploadCallback callback) {
        uploadImage(licenseFile, callback);
    }

    @Override
    public void registerStore(String token, RegisterStoreRequest request, RegisterCallback callback) {
        Call<RegisterStoreResponse> call = apiService.registerStore("Bearer " + token, request);

        call.enqueue(new Callback<RegisterStoreResponse>() {
            @Override
            public void onResponse(Call<RegisterStoreResponse> call, Response<RegisterStoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Register", "Success: " + response.body().toString());
                    String storeId = response.body().getStoreId();
                    callback.onSuccess(storeId);
                } else {
                    callback.onFailure("Đăng ký thất bại. Code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<RegisterStoreResponse> call, Throwable t) {
                Log.e("Register", "Error: " + t.getMessage());
                callback.onFailure("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void uploadImage(File file, UploadCallback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", null);

        if (token == null) {
            callback.onError("Token null! Hãy đăng nhập trước.");
            return;
        }

        Call<UploadResponse> call = imageService.uploadImage("Bearer " + token, imagePart);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = response.body().getData().getSecureUrl();
                    callback.onSuccess(imageUrl);
                } else {
                    callback.onError("Lỗi phản hồi khi upload ảnh. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                callback.onError("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public Call<Void> verifyEmail(VerifyEmailRequest request) {
        return apiService.verifyEmail(request);
    }

}
