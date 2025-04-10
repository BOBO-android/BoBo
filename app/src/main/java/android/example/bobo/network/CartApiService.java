package android.example.bobo.network;

import android.example.bobo.data.model.AddToCartRequest;
import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Cart;
import android.example.bobo.data.model.QuantityBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartApiService {
    @POST("carts")
    Call<ApiResponse<Object>> addToCart(
            @Header("Authorization") String authToken,
            @Body AddToCartRequest request
    );

    @GET("carts")
    Call<ApiResponse<Cart>> getCart(
            @Header("Authorization") String token
    );

    @PATCH("carts/items/{foodId}")
    Call<ApiResponse<Cart>> updateCartItem(
            @Header("Authorization") String token,
            @Path("foodId") String foodId,
            @Body QuantityBody quantityBody
    );

    @DELETE("carts/items/{foodId}")
    Call<ApiResponse<Cart>> removeCartItem(
            @Header("Authorization") String token,
            @Path("foodId") String foodId
    );

    @GET("checkout")
    Call<ApiResponse<Object>> checkout();
}