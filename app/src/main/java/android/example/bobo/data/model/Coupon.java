package android.example.bobo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Coupon implements Parcelable {
    private String name;
    private String details;
    private String type;
    private double value;
    private int condition;

    public Coupon(String name, String details, String type, double value, int condition) {
        this.name = name;
        this.details = details;
        this.type = type;
        this.value = value;
        this.condition = condition;
    }

    protected Coupon(Parcel in) {
        name = in.readString();
        details = in.readString();
        type = in.readString();
        value = in.readDouble();
        condition = in.readInt();
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getType(){return type;}

    public Double getValue() {return value;}

    public Integer getCondition(){return condition;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(details);
        dest.writeString(type);
        dest.writeDouble(value);
        dest.writeInt(condition);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
