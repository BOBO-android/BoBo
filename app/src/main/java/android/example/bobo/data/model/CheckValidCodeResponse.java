package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class CheckValidCodeResponse {
    @SerializedName("token")
    private String token;

    public CheckValidCodeResponse(String token) {

        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
