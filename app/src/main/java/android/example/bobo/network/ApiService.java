package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.FoodResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
}
