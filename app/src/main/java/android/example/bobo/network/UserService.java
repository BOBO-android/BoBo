package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.BaseUserProfile;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {
    @GET("me/base-profile")
    Call<ApiResponse<BaseUserProfile>> getUserProfile(@Header("Authorization") String token);
}
