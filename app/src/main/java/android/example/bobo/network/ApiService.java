package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Cart;
import android.example.bobo.data.model.FoodResponse;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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

    // Class để gửi body
    class QuantityBody {
        @SerializedName("quantity")
        private int quantity;

        public QuantityBody(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}