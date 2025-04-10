package android.example.bobo.data.model;

public class Order {
    private String id;
    private String summary;
    private String estDelivery;
    private String totalPrice;
    private int imageResId;

    public Order(String id,String summary, String estDelivery, String totalPrice, int imageResId) {
        this.id = id;
        this.summary = summary;
        this.estDelivery = estDelivery;
        this.totalPrice = totalPrice;
        this.imageResId = imageResId;
    }

    public String getId(){ return id; }
    public String getSummary() {
        return summary;
    }

    public String getEstDelivery() {
        return estDelivery;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public int getImageResId() {
        return imageResId;
    }
}
