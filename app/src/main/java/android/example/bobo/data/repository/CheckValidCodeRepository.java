package android.example.bobo.data.repository;

import android.example.bobo.data.model.ApiResponse;
import android.example.bobo.data.model.CheckValidCodeRequest;
import android.example.bobo.data.model.CheckValidCodeResponse;
import android.example.bobo.data.model.ForgotPasswordResponse;
import android.example.bobo.network.ApiService;
import android.example.bobo.network.RetrofitClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckValidCodeRepository {
    private final ApiService apiService;
    private final Gson gson;

    public CheckValidCodeRepository(){
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        gson = new Gson();
    }
    public void checkValidCode(String email, String code, final OnCheckValidCodeListener listener){
        CheckValidCodeRequest request = new CheckValidCodeRequest(email, code);
        apiService.checkValidCode(request).enqueue(new Callback<ApiResponse<CheckValidCodeResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CheckValidCodeResponse>> call, Response<ApiResponse<CheckValidCodeResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<CheckValidCodeResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        listener.onSuccess(apiResponse.getData());
                    } else {
                        listener.onFailure(apiResponse.getError());
                    }
                }else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            ApiResponse<CheckValidCodeResponse> errorResponse = gson.fromJson(
                                    errorBodyString,
                                    new TypeToken<ApiResponse<CheckValidCodeResponse>>(){}.getType()
                            );
                            String errorMessage = errorResponse.getMessage() != null
                                    ? errorResponse.getMessage()
                                    : "Check Valid Code Failed";
                            listener.onFailure(errorMessage);
                        } else {
                            listener.onFailure("Check Valid Code Failed: No error body");
                        }
                    } catch (IOException e) {
                        listener.onFailure("Error parsing error response: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CheckValidCodeResponse>> call, Throwable t) {

            }
        });
    }

    public interface OnCheckValidCodeListener {
        void onSuccess(CheckValidCodeResponse response);
        void onFailure(String error);
    }
}
