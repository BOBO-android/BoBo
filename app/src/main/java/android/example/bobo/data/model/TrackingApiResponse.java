package android.example.bobo.data.model;

import java.util.List;

public class TrackingApiResponse {
    private int statusCode;
    private String message;
    private List<TrackingOrderModel> data;

    // Getters và Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TrackingOrderModel> getData() {
        return data;
    }

    public void setData(List<TrackingOrderModel> data) {
        this.data = data;
    }
}