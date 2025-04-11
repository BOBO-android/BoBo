package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.CheckValidCodeResponse;
import android.example.bobo.data.model.ResendCodeResponse;
import android.example.bobo.data.model.ResetPasswordRequest;
import android.example.bobo.data.model.ResetPasswordResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPasswordRepository {
    private final ApiService apiService;
    private final Gson gson;

    public CreateNewPasswordRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void createNewPassword(String newPassword, String token, final OnCreateNewPasswordListener listener) {
        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, token);
        apiService.resetPassword(request).enqueue(new Callback<ApiResponse<ResetPasswordResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ResetPasswordResponse>> call, @NonNull Response<ApiResponse<ResetPasswordResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<ResetPasswordResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        listener.onSuccess(apiResponse.getData());
                    } else {
                        listener.onFailure(apiResponse.getError());
                    }
                }else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<CheckValidCodeResponse> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<CheckValidCodeResponse>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Reset Password Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Reset Password Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ResetPasswordResponse>> call, Throwable t) {
                listener.onFailure("Network error" + t.getLocalizedMessage());
            }
        });
    }

    public interface OnCreateNewPasswordListener {
        void onSuccess(ResetPasswordResponse response);
        void onFailure(String error);
    }
}
