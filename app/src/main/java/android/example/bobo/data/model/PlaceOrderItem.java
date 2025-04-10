package android.example.bobo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceOrderItem implements Parcelable {
    private String id ;
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;

    public PlaceOrderItem(String id, String name, double price, int quantity, String imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageResId;
    }

    protected PlaceOrderItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<PlaceOrderItem> CREATOR = new Creator<PlaceOrderItem>() {
        @Override
        public PlaceOrderItem createFromParcel(Parcel in) {
            return new PlaceOrderItem(in);
        }

        @Override
        public PlaceOrderItem[] newArray(int size) {
            return new PlaceOrderItem[size];
        }
    };
    public String getId(){ return  id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
