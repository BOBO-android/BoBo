package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Order;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoreOrderApiService {
    /**
     * Get orders for a specific store with optional status filter
     *
     * @param token The authorization token
     * @param storeId The ID of the store
     * @param orderStatus Optional status filter ("pending", "processing")
     * @return API response containing a list of orders
     */
    @GET("stores/orders/{storeId}")
    Call<ApiResponse<List<Order>>> getStoreOrders(
            @Header("Authorization") String token,
            @Path("storeId") String storeId,
            @Query("orderStatus") String orderStatus
    );

    @PATCH("stores/orders/{storeId}/{orderId}/status")
    Call<ApiResponse<Order>> updateOrderStatus(
            @Header("Authorization") String token,
            @Path("storeId") String storeId,
            @Path("orderId") String orderId,
            @Body Map<String, String> statusBody);
}
