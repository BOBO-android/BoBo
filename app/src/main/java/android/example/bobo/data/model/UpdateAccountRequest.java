package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateAccountRequest {
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("image")
    private String uriImage;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("address")
    private String address;

    public UpdateAccountRequest(String fullName, String uriImage, String phoneNumber, String address) {
        this.fullName = fullName;
        this.uriImage = uriImage;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
