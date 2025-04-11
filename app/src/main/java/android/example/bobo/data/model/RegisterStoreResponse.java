// RegisterStoreResponse.java
package android.example.bobo.data.model;

public class RegisterStoreResponse {
    private boolean success;
    private String message;
    private String storeId;

    // Getter & Setter for success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter & Setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter & Setter for storeId
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
