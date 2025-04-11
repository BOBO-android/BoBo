package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.LoginRequest;
import android.example.bobo.data.model.LoginResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private final ApiService apiService;

    public LoginRepository(){
        apiService = RetrofitClient.getInstance().create(ApiService.class);
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
                            listener.OnFailure(apiResponse.getError() != null ? apiResponse.getError() : "Login Failed");
                        }
                    }else {
                        listener.OnFailure("Login Failed" + " \nMessage: email or password not correct!");
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
