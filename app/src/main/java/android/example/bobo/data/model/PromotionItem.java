package android.example.bobo.data.model;

public class PromotionItem {
    private int id;
    private String title;
    private String subtitle;
    private String buttonText;
    private int imageRes;
    private int backgroundColor;

    public PromotionItem(int id, String title, String subtitle, String buttonText, int imageRes, int backgroundColor) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.buttonText = buttonText;
        this.imageRes = imageRes;
        this.backgroundColor = backgroundColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
