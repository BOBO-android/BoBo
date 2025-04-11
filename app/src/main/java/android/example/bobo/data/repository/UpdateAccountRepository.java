package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.UpdateAccountRequest;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccountRepository {
    private final ApiService apiService;
    private final Gson gson;

    public UpdateAccountRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void updateAccount(String token, UpdateAccountRequest request, final OnUpdateAccountListener listener) {
        String authToken = "Bearer " + token;
        apiService.updateAccount(authToken, request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() <= 299) {
                        listener.onSuccess(apiResponse);
                    } else {
                        String errorMessage = apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage() != null ? apiResponse.getMessage() : "Update account failed: Unknown error";
                        listener.onFailure(errorMessage);
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<Object> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<Object>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Update Account Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Update Account Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                listener.onFailure("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    public interface OnUpdateAccountListener {
        void onSuccess(ApiResponse<Object> apiResponse);
        void onFailure(String error);
    }
}