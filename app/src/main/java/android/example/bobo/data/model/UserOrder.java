package android.example.bobo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

public class UserOrder {
    @SerializedName("_id")
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("storeId")
    private String storeId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("userImageUrl")
    private String userImageUrl;

    @SerializedName("orderTime")
    private String orderTime;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("status")
    private String status;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("paymentStatus")
    private String paymentStatus;

    @SerializedName("deliveryDate")
    private String deliveryDate;

    @SerializedName("deliverTo")
    private String deliverTo;

    // Chỉ giữ một trường cho deserialization từ API
    @SerializedName("foodItems")
    private List<FoodItemDto> apiOrders;
    private transient List<UserFoodItem> convertedFoodItems;

    @SerializedName("vnpayTransactionId")
    private String vnpayTransactionId;

    @SerializedName("notes")
    private String notes;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    // This static inner class is only for API deserialization
    public static class FoodItemDto {
        @SerializedName("_id")
        private String id;

        @SerializedName("foodId")
        private String foodId;

        @SerializedName("name")
        private String name;

        @SerializedName("price")
        private double price;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("quantity")
        private int quantity;

        public UserFoodItem toUserFoodItem() {
            return new UserFoodItem(
                    foodId,
                    name,
                    imageUrl,
                    "",
                    price,
                    0.0f
            );
        }
    }

    // Converts apiOrders to foodItems
    public void processFoodItems() {
        if (apiOrders != null && !apiOrders.isEmpty()) {
            convertedFoodItems = new ArrayList<>();
            for (FoodItemDto dto : apiOrders) {
                convertedFoodItems.add(dto.toUserFoodItem());
            }
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public List<UserFoodItem> getFoodItems() {
        if (convertedFoodItems == null && apiOrders != null) {
            processFoodItems();
        }
        return convertedFoodItems;
    }

    public void setFoodItems(List<UserFoodItem> foodItems) {
        this.convertedFoodItems = foodItems;
    }

    public String getVnpayTransactionId() {
        return vnpayTransactionId;
    }

    public void setVnpayTransactionId(String vnpayTransactionId) {
        this.vnpayTransactionId = vnpayTransactionId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}