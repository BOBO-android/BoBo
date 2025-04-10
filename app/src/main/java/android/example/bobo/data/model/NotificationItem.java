package android.example.bobo.data.model;

public class NotificationItem {
    private int imageResId;
    private String title;
    private String message;
    private String time;

    public NotificationItem(int imageResId, String title, String message, String time) {
        this.imageResId = imageResId;
        this.title = title;
        this.message = message;
        this.time = time;
    }

    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
}
