package android.example.bobo.data.repository;

import android.example.bobo.data.model.AddToCartRequest;
import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.network.CartApiService;
import android.example.bobo.network.FoodService;
import android.example.bobo.network.RetrofitClient;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartRepository {
    private static final String TAG = "CartRepository";

    private final CartApiService cartApiService;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<Object>> cartResponse = new MutableLiveData<>();

    public CartRepository() {
        this.cartApiService = RetrofitClient.getInstance().create(CartApiService.class);
    }

    public void addToCart(String token, String foodId, int quantity) {
        isLoading.setValue(true);

        // Đảm bảo token có định dạng đúng (thường là "Bearer <token>")
        String authToken = token;
        if (!token.startsWith("Bearer ")) {
            authToken = "Bearer " + token;
        }

        AddToCartRequest request = new AddToCartRequest(foodId, quantity);

        cartApiService.addToCart(authToken, request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    cartResponse.setValue(response.body());
                    if (!response.body().isSuccess()) {
                        errorMessage.setValue(response.body().getError() != null ?
                                response.body().getError() : response.body().getMessage());
                    }
                } else {
                    String error = "Add to cart failed: " +
                            (response.errorBody() != null ? response.code() : "Unknown error");
                    errorMessage.setValue(error);
                    Log.e(TAG, error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                isLoading.setValue(false);
                String error = "Connection error: " + t.getMessage();
                errorMessage.setValue(error);
                Log.e(TAG, error, t);
            }
        });
    }

    // Getters cho LiveData
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<ApiResponse<Object>> getCartResponse() {
        return cartResponse;
    }

    // Reset error message
    public void resetError() {
        errorMessage.setValue(null);
    }
}
