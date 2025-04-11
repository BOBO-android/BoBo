package android.example.bobo.network;

import android.example.bobo.data.model.TrackingApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TrackingApiService {
    @GET("orders")
    Call<TrackingApiResponse> getOrdersByCustomerId(@Query("customerId") String customerId);
}