package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.BaseUserProfile;
import android.example.bobo.network.RetrofitClient;
import android.example.bobo.network.UserService;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final UserService userService;
    private final MutableLiveData<BaseUserProfile> userProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserRepository() {
        userService = RetrofitClient.getInstance().create(UserService.class);
    }

    public LiveData<BaseUserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchUserProfile(String token) {
        isLoading.setValue(true);

        // Ensure token is in proper format
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }

        userService.getUserProfile(token).enqueue(new Callback<ApiResponse<BaseUserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<BaseUserProfile>> call, Response<ApiResponse<BaseUserProfile>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<BaseUserProfile> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        userProfile.setValue(apiResponse.getData());
                    } else {
                        errorMessage.setValue("Error: " + apiResponse.getMessage());
                        Log.e(TAG, "API error: " + apiResponse.getMessage());
                    }
                } else {
                    errorMessage.setValue("Error fetching profile: " + response.message());
                    Log.e(TAG, "Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BaseUserProfile>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
                Log.e(TAG, "Network error", t);
            }
        });
    }
}
