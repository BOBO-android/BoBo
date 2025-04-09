package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.CheckValidCodeRequest;
import android.example.bobo.data.model.CheckValidCodeResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.FoodResponse;
import android.example.bobo.data.model.ForgotPasswordRequest;
import android.example.bobo.data.model.ForgotPasswordResponse;
import android.example.bobo.data.model.LoginRequest;
import android.example.bobo.data.model.LoginResponse;
import android.example.bobo.data.model.RegisterRequest;
import android.example.bobo.data.model.RegisterResponse;
import android.example.bobo.data.model.ResendCodeRequest;
import android.example.bobo.data.model.ResendCodeResponse;
import android.example.bobo.data.model.ResetPasswordRequest;
import android.example.bobo.data.model.ResetPasswordResponse;
import android.example.bobo.data.model.VerifyRequest;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("stores/{storeId}/foods")
    Call<ApiResponse<FoodResponse>> getDishes(
            @Header("Authorization") String token,
            @Path("storeId") String storeId,
            @Query("current") int currentPage,
            @Query("pageSize") int pageSize
    );
    // login
    @POST("auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    //forgot password
    @POST("auth/forgot-password")
    Call<ApiResponse<ForgotPasswordResponse>> forgotPassword(@Body ForgotPasswordRequest request);

    //CheckValidCode
    @POST("auth/check-validcode")
    Call<ApiResponse<CheckValidCodeResponse>> checkValidCode(@Body CheckValidCodeRequest request);

    // Resend Code
    @POST("auth/resend-code")
    Call<ApiResponse<ResendCodeResponse>> resendCode(@Body ResendCodeRequest request);

    // ResetPassword
    @POST("auth/reset-password")
    Call<ApiResponse<ResetPasswordResponse>> resetPassword(@Body ResetPasswordRequest request);

    @POST("auth/register")
    Call<ApiResponse<RegisterResponse>> register(@Body RegisterRequest request);

    @POST("auth/verify")
    Call<ApiResponse<java.lang.Object>> verify(@Body VerifyRequest request);
}
