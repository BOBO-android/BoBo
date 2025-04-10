package android.example.bobo.data.model;

public class OrderStatus {
    private String title;
    private String description;
    private String time;
    private boolean isActive;

    public OrderStatus(String title, String description, String time, boolean isActive) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public boolean isActive() {
        return isActive;
    }
}