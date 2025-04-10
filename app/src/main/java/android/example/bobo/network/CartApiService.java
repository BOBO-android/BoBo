package android.example.bobo.network;

import android.example.bobo.data.model.AddToCartRequest;
import android.example.bobo.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CartApiService {
    @POST("carts")
    Call<ApiResponse<Object>> addToCart(
            @Header("Authorization") String authToken,
            @Body AddToCartRequest request
    );
}
