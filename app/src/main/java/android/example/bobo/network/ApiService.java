package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.FoodResponse;
import android.example.bobo.data.model.RegisterStoreRequest;
import android.example.bobo.data.model.RegisterStoreResponse;
import android.example.bobo.data.model.ResendCodeRequest;
import android.example.bobo.data.model.UploadResponse;
import android.example.bobo.data.model.VerifyEmailRequest;

import java.util.List;

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
    @GET("stores/{storeId}/foods")
    Call<ApiResponse<FoodResponse>> getDishes(
            @Header("Authorization") String token,
            @Path("storeId") String storeId,
            @Query("current") int currentPage,
            @Query("pageSize") int pageSize
    );

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
