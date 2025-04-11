package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.LoginRequest;
import android.example.bobo.data.model.LoginResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private final ApiService apiService;
    private final Gson gson;

    public LoginRepository(){
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void login(String email, String password, final OnLoginListener listener){
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request)
            .enqueue(new Callback<ApiResponse<LoginResponse>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Response<ApiResponse<LoginResponse>> response) {
                    if (response.isSuccessful() && response.body() != null){
                        ApiResponse<LoginResponse> apiResponse = response.body();
                        if (apiResponse.isSuccess()){
                            listener.OnSuccess(apiResponse.getData());
                        } else {
                            // Ưu tiên sử dụng message từ response
                            String errorMessage = "";
                            if (apiResponse.getMessage() != null && !apiResponse.getMessage().isEmpty()) {
                                errorMessage = apiResponse.getMessage();
                            } else if (apiResponse.getError() != null && !apiResponse.getError().isEmpty()) {
                                errorMessage = apiResponse.getError();
                            } else {
                                errorMessage = "Login Failed";
                            }
                            listener.OnFailure(errorMessage);
                        }
                    } else {
                        // Xử lý lỗi khi response code không phải 2xx
                        try {
                            if (response.errorBody() != null) {
                                String errorBodyStr = response.errorBody().string();
                                try {
                                    // Cố gắng parse JSON từ errorBody
                                    ApiResponse<?> errorResponse = gson.fromJson(errorBodyStr, ApiResponse.class);
                                    String errorMessage = "";
                                    // Ưu tiên sử dụng message
                                    if (errorResponse.getMessage() != null && !errorResponse.getMessage().isEmpty()) {
                                        errorMessage = errorResponse.getMessage();
                                    } else if (errorResponse.getError() != null && !errorResponse.getError().isEmpty()) {
                                        errorMessage = errorResponse.getError();
                                    } else {
                                        errorMessage = "Login Failed: " + response.code();
                                    }
                                    listener.OnFailure(errorMessage);
                                } catch (Exception e) {
                                    // Nếu không parse được JSON, trả về nội dung gốc
                                    listener.OnFailure(errorBodyStr);
                                }
                            } else {
                                listener.OnFailure("Login Failed: " + response.code() + " " + response.message());
                            }
                        } catch (IOException e) {
                            listener.OnFailure("Login Failed: " + response.code());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Throwable t) {
                    listener.OnFailure("Network error: " + t.getLocalizedMessage());
                }
        });
    }

    public interface OnLoginListener{
        void OnSuccess(LoginResponse response);
        void OnFailure(String error);
    }
}
