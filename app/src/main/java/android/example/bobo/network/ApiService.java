package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Cart;
import android.example.bobo.data.model.CheckValidCodeRequest;
import android.example.bobo.data.model.CheckValidCodeResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.FoodResponse;
import android.example.bobo.data.model.FoodSearchResponse;
import android.example.bobo.data.model.RegisterStoreRequest;
import android.example.bobo.data.model.RegisterStoreResponse;
import android.example.bobo.data.model.ResendCodeRequest;
import android.example.bobo.data.model.UploadResponse;
import android.example.bobo.data.model.VerifyEmailRequest;
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
import android.example.bobo.data.model.SearchFoodItemModel;
import android.example.bobo.data.model.UserOrder;
import android.example.bobo.data.model.VerifyRequest;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
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
  
    @GET("stores/{storeId}/foods")
    Call<ApiResponse<FoodResponse>> getDishes(
            @Header("Authorization") String token,
            @Path("storeId") String storeId,
            @Query("current") int currentPage,
            @Query("pageSize") int pageSize
    );
    @GET("orders")
    Call<ApiResponse<List<UserOrder>>> getUserOrders(
            @Header("Authorization") String token,
            @Query("customerId") String customerId
    );

    @GET("foods/{slug}")
    Call<ApiResponse<SearchFoodItemModel>> getFoodBySlug(@Path("slug") String slug);

    @GET("foods")
    Call<ApiResponse<FoodSearchResponse>> searchFoods(@Query("query") String query);
  
    @POST("/api/v1/stores/register")
    Call<RegisterStoreResponse> registerStore(
            @Header("Authorization") String token,
            @Body RegisterStoreRequest request
    );

    @Multipart
    @POST("/api/v1/media/images/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @POST("/api/v1/store/verify")
    Call<Void> verifyEmail(@Body VerifyEmailRequest request);

    @POST("store/resend-code")
    Call<Void> resendCode(@Body ResendCodeRequest request);
}
