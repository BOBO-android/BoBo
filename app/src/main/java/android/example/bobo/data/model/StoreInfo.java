package android.example.bobo.data.model;

public class StoreInfo {
    private String storeName;
    private String email;
    private String phone;
    private String address;
    private String bankNumber;
    private String bankName;

    // Constructor, Getters, Setters

    public StoreInfo(String storeName, String email, String phone, String address, String bankNumber, String bankName) {
        this.storeName = storeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.bankNumber = bankNumber;
        this.bankName = bankName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
