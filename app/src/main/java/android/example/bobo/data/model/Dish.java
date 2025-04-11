package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("storeId")
    private String storeId;

    @SerializedName("isAvailable")
    private boolean isAvailable;

    @SerializedName("preparationTime")
    private int preparationTime;

    @SerializedName("rating")
    private int rating;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("isDeleted")
    private boolean isDeleted;

    @SerializedName("deletedAt")
    private String deletedAt; // Nullable

    @SerializedName("isOffered")
    private boolean isOffered;

    @SerializedName("discount")
    private double discount;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("slug")
    private String slug;


    public Dish(String number, String donutBox, double v, String assortedDeliciousDonuts, String store1, boolean b, int i, int i1, String url, boolean b1, Object o, boolean b2, double v1, String s, String s1) {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getStoreId() {
        return storeId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public int getRating() {
        return rating;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public boolean isOffered() {
        return isOffered;
    }

    public double getDiscount() {
        return discount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getSlug() {
        return slug;
    }
}
