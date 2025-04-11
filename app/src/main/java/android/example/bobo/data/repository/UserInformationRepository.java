package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.UserInformationResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformationRepository {
    private final ApiService apiService;
    private final Gson gson;

    public UserInformationRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void getUserInformation(String token, final OnUserInformationListener listener) {
        String authToken = "Bearer " + token;
        apiService.userInformation(authToken).enqueue(new Callback<ApiResponse<UserInformationResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<UserInformationResponse>> call, @NonNull Response<ApiResponse<UserInformationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserInformationResponse> apiResponse = response.body();
                    if (apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() <= 299) {
                        listener.onSuccess(apiResponse);
                    } else {
                        String errorMessage = apiResponse.getError() != null ? apiResponse.getError() : apiResponse.getMessage() != null ? apiResponse.getMessage() : "Get user information failed: Unknown error";
                        listener.onFailure(errorMessage);
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<UserInformationResponse> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<UserInformationResponse>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Get User Information Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Get User Information Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<UserInformationResponse>> call, @NonNull Throwable t) {
                listener.onFailure("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    public interface OnUserInformationListener {
        void onSuccess(ApiResponse<UserInformationResponse> apiResponse);
        void onFailure(String error);
    }
}