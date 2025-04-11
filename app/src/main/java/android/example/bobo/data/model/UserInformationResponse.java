package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class UserInformationResponse {
    @SerializedName("_id")
    private String id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;
    @SerializedName("image")
    private String image;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    public UserInformationResponse(String id, String fullName, String email, String address, String image, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.image = image;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
