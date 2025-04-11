package android.example.bobo.ui.viewmodel;

import android.app.Application;
import android.example.bobo.data.model.UserOrder;
import android.example.bobo.data.repository.UserOrderRepository;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class UserOrderViewModel extends AndroidViewModel {
    private static final String TAG = "UserOrderViewModel";
    private final UserOrderRepository userOrderRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserOrderViewModel(@NonNull Application application) {
        super(application);
        userOrderRepository = new UserOrderRepository();
    }

    public LiveData<List<UserOrder>> getUserOrders(String token, String customerId) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        LiveData<List<UserOrder>> ordersLiveData = userOrderRepository.getUserOrders(token, customerId);

        // We need to observe this LiveData to update our loading state
        LiveData<List<UserOrder>> transformedOrdersLiveData = new MutableLiveData<>();

        // This is a workaround to handle loading state, in a real app you might want to use a Resource class
        ordersLiveData.observeForever(orders -> {
            isLoading.setValue(false);
            if (orders == null) {
                errorMessage.setValue("Error fetching orders. Please try again.");
            }
            ((MutableLiveData<List<UserOrder>>) transformedOrdersLiveData).setValue(orders);
        });

        return transformedOrdersLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}