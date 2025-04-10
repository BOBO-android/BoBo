package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.Cart;
import android.example.bobo.data.model.CartItem;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private static final String TAG = "CartRepository";

    private ApiService apiService;
    private String authToken; // Token xác thực
    private MutableLiveData<List<CartItem>> cartItems = new MutableLiveData<>();
    private MutableLiveData<Double> totalPrice = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEmpty = new MutableLiveData<>();

    public CartRepository(String token) {
        this.authToken = "Bearer " + token;
        apiService = RetrofitClient.getApiService();
        isEmpty.setValue(true); // Mặc định giỏ hàng trống
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public LiveData<Double> getTotalPrice() {
        return totalPrice;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isEmpty() {
        return isEmpty;
    }

    public void refreshCart() {
        fetchCartItems();
    }

    private void fetchCartItems() {
        if (authToken == null || authToken.isEmpty()) {
            errorMessage.setValue("Thiếu token xác thực");
            isLoading.setValue(false);
            isEmpty.setValue(true);
            Log.e(TAG, "AuthToken is null or empty");
            return;
        }

        isLoading.setValue(true);
        apiService.getCart(authToken).enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonResponse = new com.google.gson.Gson().toJson(response.body());
                        Log.d(TAG, "Raw JSON response: " + jsonResponse);
                    } catch (Exception e) {
                        Log.e(TAG, "Error logging JSON: " + e.getMessage());
                    }

                    ApiResponse<Cart> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Cart cart = apiResponse.getData();
                        if (cart != null && cart.getItems() != null) {
                            cartItems.setValue(cart.getItems());
                            totalPrice.setValue(cart.getTotalAmount());
                            isEmpty.setValue(cart.getItems().isEmpty());
                        } else {
                            cartItems.setValue(new ArrayList<>());
                            totalPrice.setValue(0.0);
                            isEmpty.setValue(true);
                        }
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "API error";
                        errorMessage.setValue(errorMsg);
                        isEmpty.setValue(true);
                        Log.e(TAG, "API Response Error: " + errorMsg);
                    }
                } else {
                    String errorMsg = "Error: " + response.code();
                    errorMessage.setValue(errorMsg);
                    isEmpty.setValue(true);
                    Log.e(TAG, "HTTP Error: " + response.code() + ", Message: " + response.message());
                    if (response.code() == 401) {
                        errorMessage.setValue("Phiên đăng nhập hết hạn, vui lòng đăng nhập lại");
                    } else if (response.code() == 404) {
                        errorMessage.setValue("Không tìm thấy giỏ hàng");
                    } else if (response.code() == 500) {
                        errorMessage.setValue("Lỗi server, vui lòng thử lại sau");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
                isEmpty.setValue(true);
                Log.e(TAG, "Network Error: " + t.getMessage(), t);
            }
        });
    }

    public void updateQuantity(String foodId, int newQuantity) {
        // Kiểm tra token và foodId (giữ nguyên)

        // Lưu dữ liệu hiện tại trước khi cập nhật
        List<CartItem> currentItems = cartItems.getValue();
        Map<String, CartItem> itemMap = new HashMap<>();

        if (currentItems != null) {
            for (CartItem item : currentItems) {
                itemMap.put(item.getFoodId(), item);
            }
        }

        isLoading.setValue(true);

        Call<ApiResponse<Cart>> call;
        if (newQuantity > 0) {
            call = apiService.updateCartItem(authToken, foodId, new ApiService.QuantityBody(newQuantity));
        } else {
            call = apiService.removeCartItem(authToken, foodId);
        }

        call.enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                isLoading.setValue(false);

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Cart> apiResponse = response.body();

                        // Log để debug
                        String rawJson = new com.google.gson.Gson().toJson(response.body());
                        Log.d(TAG, "Raw JSON after update: " + rawJson);

                        if (apiResponse.isSuccess()) {
                            Cart cart = apiResponse.getData();
                            if (cart != null && cart.getItems() != null) {
                                // Hợp nhất dữ liệu mới với dữ liệu cũ
                                List<CartItem> updatedItems = new ArrayList<>();

                                for (CartItem newItem : cart.getItems()) {
                                    // Lấy foodId đúng từ đối tượng
                                    String itemFoodId = newItem.getFoodId();

                                    // Tìm item cũ tương ứng
                                    CartItem oldItem = itemMap.get(itemFoodId);

                                    // Hợp nhất dữ liệu
                                    if (oldItem != null) {
                                        // Chỉ cập nhật số lượng từ item mới
                                        CartItem mergedItem = new CartItem(
                                                newItem.getId(),
                                                itemFoodId,
                                                oldItem.getName(),
                                                oldItem.getPrice(),
                                                newItem.getQuantity(),
                                                oldItem.getImageUrl()        // Giữ URL hình ảnh từ item cũ
                                        );
                                        updatedItems.add(mergedItem);

                                        Log.d(TAG, "Merged item - ID: " + mergedItem.getId() +
                                                ", Name: " + mergedItem.getName() +
                                                ", Price: " + mergedItem.getPrice() +
                                                ", Quantity: " + mergedItem.getQuantity());
                                    } else {
                                        // Nếu không tìm thấy item cũ, thêm item mới
                                        updatedItems.add(newItem);
                                        Log.d(TAG, "Added new item without merging - ID: " + newItem.getId());
                                    }
                                }

                                // Cập nhật UI với dữ liệu đã hợp nhất
                                cartItems.setValue(updatedItems);

                                // Tính lại tổng tiền
                                double total = 0;
                                for (CartItem item : updatedItems) {
                                    total += item.getPrice() * item.getQuantity();
                                }
                                totalPrice.setValue(total);
                                isEmpty.setValue(updatedItems.isEmpty());

                                Log.d(TAG, "Updated cart with " + updatedItems.size() + " items, total: " + total);
                            } else {
                                // Xử lý giỏ hàng trống
                                cartItems.setValue(new ArrayList<>());
                                totalPrice.setValue(0.0);
                                isEmpty.setValue(true);
                                Log.d(TAG, "Cart is empty after update");
                            }
                        } else {
                            String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Update failed";
                            errorMessage.setValue(errorMsg);
                            Log.e(TAG, "API Error on update: " + errorMsg);
                        }
                    } else {
                        // Xử lý lỗi HTTP (giữ nguyên)
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing response: " + e.getMessage(), e);
                    errorMessage.setValue("Lỗi xử lý phản hồi: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                // Xử lý failure (giữ nguyên)
            }
        });
    }

    public void removeItem(String foodId) {
        updateQuantity(foodId, 0);
    }

    public void proceedToCheckout(OnCheckoutListener listener) {
        if (Boolean.TRUE.equals(isEmpty.getValue())) {
            listener.onError("Giỏ hàng trống, không thể thanh toán");
            return;
        }
        isLoading.setValue(true);
        listener.onSuccess("Chuyển đến trang đặt hàng");
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public interface OnCheckoutListener {
        void onSuccess(String message);
        void onError(String message);
    }
}