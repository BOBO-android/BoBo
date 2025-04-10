package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}