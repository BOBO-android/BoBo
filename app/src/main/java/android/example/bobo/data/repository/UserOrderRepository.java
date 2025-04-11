package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.UserOrder;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderRepository {
    private static final String TAG = "UserOrderRepository";
    private final ApiService apiService;

    public UserOrderRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public LiveData<List<UserOrder>> getUserOrders(String token, String customerId) {
        MutableLiveData<List<UserOrder>> ordersLiveData = new MutableLiveData<>();

        // Make sure token is in the correct format (Bearer token)
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }

        apiService.getUserOrders(token, customerId).enqueue(new Callback<ApiResponse<List<UserOrder>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserOrder>>> call, Response<ApiResponse<List<UserOrder>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<UserOrder>> apiResponse = response.body();
                    List<UserOrder> orders = apiResponse.getData();

                    if (orders != null) {
                        // Process food items to convert from API format to app format
                        for (UserOrder order : orders) {
                            order.processFoodItems();
                        }

                        ordersLiveData.setValue(orders);
                        Log.d(TAG, "Orders fetched successfully: " + orders.size());
                    } else {
                        Log.e(TAG, "Order response data is null");
                        ordersLiveData.setValue(null);
                    }
                } else {
                    Log.e(TAG, "Error fetching orders: " + response.code());
                    ordersLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserOrder>>> call, Throwable t) {
                Log.e(TAG, "Network error when fetching orders", t);
                ordersLiveData.setValue(null);
            }
        });

        return ordersLiveData;
    }
}