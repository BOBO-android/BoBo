package android.example.bobo.data.repository;

import androidx.lifecycle.MutableLiveData;

import android.example.bobo.data.model.TrackingApiResponse;
import android.example.bobo.data.model.TrackingOrderModel;
import android.example.bobo.network.RetrofitClient;
import android.example.bobo.network.TrackingApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingRepository {
    private TrackingApiService apiService;
    private MutableLiveData<List<TrackingOrderModel>> ordersLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public TrackingRepository() {
        // Sử dụng RetrofitClient hiện có thay vì tạo mới
        apiService = RetrofitClient.getInstance().create(TrackingApiService.class);
    }

    public void fetchOrdersByCustomerId(String customerId) {
        isLoading.setValue(true);

        Call<TrackingApiResponse> call = apiService.getOrdersByCustomerId(customerId);
        call.enqueue(new Callback<TrackingApiResponse>() {
            @Override
            public void onResponse(Call<TrackingApiResponse> call, Response<TrackingApiResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    TrackingApiResponse apiResponse = response.body();
                    if (apiResponse.getStatusCode() == 200) {
                        ordersLiveData.setValue(apiResponse.getData());
                    } else {
                        errorMessage.setValue(apiResponse.getMessage());
                    }
                } else {
                    errorMessage.setValue("Failed to fetch orders: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TrackingApiResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<List<TrackingOrderModel>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
}