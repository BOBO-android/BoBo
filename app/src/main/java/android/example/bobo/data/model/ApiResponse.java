package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    @SerializedName("error")
    private String error; // Only for error responses

    // Getters
    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    // Custom method to check if response is successful
    public boolean isSuccess() {
        return statusCode == 200 && data != null;
    }
}
