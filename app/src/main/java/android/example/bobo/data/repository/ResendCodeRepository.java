package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.CheckValidCodeResponse;
import android.example.bobo.data.model.ResendCodeRequest;
import android.example.bobo.data.model.ResendCodeResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResendCodeRepository {
    private ApiService apiService;
    private Gson gson;

    public ResendCodeRepository(){
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }

    public void resendCode(String email, final OnResendCodeListener listener){
        ResendCodeRequest request = new ResendCodeRequest(email);
        apiService.resendCode(request).enqueue(new Callback<ApiResponse<ResendCodeResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResendCodeResponse>> call, Response<ApiResponse<ResendCodeResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ApiResponse<ResendCodeResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        listener.onSuccess(apiResponse);
                    } else {
                        listener.onFailure(apiResponse.getError());
                    }
                }else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<ResendCodeResponse> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<ResendCodeResponse>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Resend Code Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Resend Code Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResendCodeResponse>> call, Throwable t) {

            }
        });
    }

    public interface OnResendCodeListener {
        void onSuccess(ApiResponse<ResendCodeResponse> apiResponse);
        void onFailure(String error);
    }
}
