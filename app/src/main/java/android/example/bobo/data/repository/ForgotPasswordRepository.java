package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.ForgotPasswordRequest;
import android.example.bobo.data.model.ForgotPasswordResponse;

import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordRepository {
    private final ApiService apiService;
    private final Gson gson;

    public ForgotPasswordRepository(){
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void forgotPassword(String email, final OnForgotPasswordListener listener) {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        apiService.forgotPassword(request).enqueue(new Callback<ApiResponse<ForgotPasswordResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ForgotPasswordResponse>> call,@NonNull Response<ApiResponse<ForgotPasswordResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ForgotPasswordResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        listener.onSuccess(apiResponse.getData());
                    } else {
                        listener.onFailure(apiResponse.getError());
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<ForgotPasswordResponse> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<ForgotPasswordResponse>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Forgot Password Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Forgot Password Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ForgotPasswordResponse>> call,@NonNull Throwable t) {
                listener.onFailure("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    public interface OnForgotPasswordListener {
        void onSuccess(ForgotPasswordResponse response);
        void onFailure(String error);
    }
}
