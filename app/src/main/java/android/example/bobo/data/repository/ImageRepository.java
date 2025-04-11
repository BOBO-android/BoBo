package android.example.bobo.data.repository;

import android.example.bobo.data.model.UploadResponse;
import android.example.bobo.network.ImageService;
import android.example.bobo.network.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRepository {
    private final ImageService imageService;

    public ImageRepository() {
        this.imageService = RetrofitClient.getInstance().create(ImageService.class);
    }

    public interface UploadImageCallback {
        void onSuccess(UploadResponse response);
        void onError(String errorMessage);
    }

    public void uploadImage(String token, File imageFile, UploadImageCallback callback) {
        // Tạo RequestBody từ file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

        // Tạo MultipartBody.Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        // Thêm "Bearer " vào token nếu cần
        String authToken = token.startsWith("Bearer ") ? token : "Bearer " + token;

        // Thực hiện gọi API
        Call<UploadResponse> call = imageService.uploadImage(authToken, filePart);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Lỗi: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
