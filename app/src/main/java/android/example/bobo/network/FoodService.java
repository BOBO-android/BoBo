package android.example.bobo.network;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Dish;
import android.example.bobo.data.model.UserFoodResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodService {

    /**
     * Get public offered foods (foods with discount)
     * @param limit Number of items to fetch
     * @param lastId ID of the last item from previous fetch for pagination (can be null for first page)
     */
    @GET("foods/public-offered")
    Call<ApiResponse<UserFoodResponse>> getPublicOfferedFoods(
            @Query("limit") int limit,
            @Query("lastId") String lastId
    );

    /**
     * Get food detail by slug
     * @param slug The slug of the food item
     */
    @GET("foods/{slug}")
    Call<ApiResponse<Dish>> getFoodBySlug(
            @Path("slug") String slug
    );
}
