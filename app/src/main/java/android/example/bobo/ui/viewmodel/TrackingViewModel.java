package android.example.bobo.ui.viewmodel;

import android.example.bobo.data.model.TrackingOrderModel;
import android.example.bobo.data.repository.TrackingRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

public class TrackingViewModel extends ViewModel {
    private TrackingRepository repository;
    private LiveData<List<TrackingOrderModel>> orders;
    private LiveData<Boolean> isLoading;
    private LiveData<String> errorMessage;

    public TrackingViewModel() {
        repository = new TrackingRepository();
        orders = repository.getOrdersLiveData();
        isLoading = repository.getIsLoading();
        errorMessage = repository.getErrorMessage();
    }

    public void loadOrdersByCustomerId(String customerId) {
        repository.fetchOrdersByCustomerId(customerId);
    }

    public LiveData<List<TrackingOrderModel>> getOrders() {
        return orders;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}