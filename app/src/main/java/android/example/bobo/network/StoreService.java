package android.example.bobo.network;

import android.example.bobo.data.model.RegisterStoreRequest;
import android.example.bobo.data.model.RegisterStoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface StoreService {
    @POST("api/v1/stores/register")
    Call<RegisterStoreResponse> registerStore(
            @Header("Authorization") String token,
            @Body RegisterStoreRequest request
    );
}
