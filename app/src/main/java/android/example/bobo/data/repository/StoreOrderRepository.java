package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Order;
import android.example.bobo.network.RetrofitClient;
import android.example.bobo.network.StoreOrderApiService;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreOrderRepository {
    private static final String TAG = "StoreOrderRepository";
    private final StoreOrderApiService storeOrderApiService;

    public StoreOrderRepository() {
        storeOrderApiService = RetrofitClient.getInstance().create(StoreOrderApiService.class);
    }

    /**
     * Fetch store orders with the given status
     *
     * @param storeId The store ID
     * @param status Order status to filter by
     * @param ordersData LiveData to be updated with the results
     * @param token Authentication token
     */
    public void getStoreOrdersByStatus(String storeId, String status, MutableLiveData<List<Order>> ordersData, String token) {
        storeOrderApiService.getStoreOrders("Bearer " + token, storeId, status).enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Order>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<Order> orders = apiResponse.getData();
                        for (int i = 0; i < orders.size(); i++) {
                            Log.d(TAG, "Order " + (i+1) + ": " + orders.get(i).getOrderId());
                        }

                        // Now we can directly use the orders list from the data field
                        ordersData.postValue(orders);
                    } else {
                        // Handle API error
                        ordersData.postValue(new ArrayList<>());
                    }
                } else {
                    // Handle HTTP error
                    try {
                        if (response.errorBody() != null) {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    ordersData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                // Handle network error
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                ordersData.postValue(new ArrayList<>());
            }
        });
    }

    /**
     * Update order status
     *
     * @param storeId The store ID
     * @param orderId The order ID
     * @param status New status to set
     * @param token Authentication token
     */
    public void updateOrderStatus(String storeId, String orderId, String status, String token,
                                  MutableLiveData<Boolean> resultCallback) {
        // Create request body with the status
        Map<String, String> statusBody = new HashMap<>();
        statusBody.put("status", status);

        storeOrderApiService.updateOrderStatus(
                        "Bearer " + token,
                        storeId,
                        orderId,
                        statusBody)
                .enqueue(new Callback<ApiResponse<Order>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<Order> apiResponse = response.body();
                            if (apiResponse.isSuccess()) {
                                Log.d(TAG, "Order status updated successfully");
                                resultCallback.postValue(true);
                            } else {
                                Log.e(TAG, "API error: " + apiResponse.getMessage());
                                resultCallback.postValue(false);
                            }
                        } else {
                            // Handle HTTP error
                            try {
                                if (response.errorBody() != null) {
                                    Log.e(TAG, "Error body: " + response.errorBody().string());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body", e);
                            }
                            resultCallback.postValue(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage(), t);
                        resultCallback.postValue(false);
                    }
                });
    }
}
