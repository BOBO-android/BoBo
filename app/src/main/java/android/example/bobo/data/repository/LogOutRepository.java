package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogOutRepository {
    private final ApiService apiService;
    private final Gson gson;

    public LogOutRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void logout(String token, final OnLogoutListener listener) {
        String authToken = "Bearer " + token;
        apiService.logout(authToken).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call, @NonNull Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiResponse = response.body();
                    if (apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() <= 299) {
                        listener.onSuccess(apiResponse);
                    } else {
                        String errorMessage = apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage() != null ? apiResponse.getMessage() : "Logout failed: Unknown error";
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
                                    : "Logout Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Logout Failed: No error body");
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

    public interface OnLogoutListener {
        void onSuccess(ApiResponse<Object> apiResponse);
        void onFailure(String error);
    }
}