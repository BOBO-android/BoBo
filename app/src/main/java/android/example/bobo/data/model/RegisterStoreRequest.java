package android.example.bobo.data.model;

public class RegisterStoreRequest {
    private String name;
    private String description;
    private String address;
    private String email;
    private String bankAccountNumber;
    private String bankType;
    private String businessLicense;
    private String logo;
    private String phoneNumber;

    public RegisterStoreRequest() {
    }

    public RegisterStoreRequest(String name, String address, String email,  String phoneNumber,
                                String bankAccountNumber, String bankType, String logo,
                                String businessLicense) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.bankAccountNumber = bankAccountNumber;
        this.bankType = bankType;
        this.businessLicense = businessLicense;
        this.logo = logo;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getBankType() {
        return bankType;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public String getLogo() {
        return logo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
