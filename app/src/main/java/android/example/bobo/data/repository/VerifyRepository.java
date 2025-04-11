package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.VerifyRequest;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyRepository {
    private final ApiService apiService;
    private final Gson gson;

    public VerifyRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void verify(String userName, String verificationCode, final OnVerifyListener listener) {
        VerifyRequest request = new VerifyRequest(userName, verificationCode);
        apiService.verify(request).enqueue(new Callback<ApiResponse<java.lang.Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<java.lang.Object>> call, @NonNull Response<ApiResponse<java.lang.Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<java.lang.Object> apiResponse = response.body();
                    if (apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() <= 299) {
                        listener.onSuccess(apiResponse);
                    } else {
                        String errorMessage = apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage() != null ? apiResponse.getMessage() : "Verification failed: Unknown error";
                        listener.onFailure(errorMessage);
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<java.lang.Object> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<java.lang.Object>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Verification Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Verification Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<java.lang.Object>> call, @NonNull Throwable t) {
                listener.onFailure("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    public interface OnVerifyListener {
        void onSuccess(ApiResponse<java.lang.Object> apiResponse);
        void onFailure(String error);
    }
}