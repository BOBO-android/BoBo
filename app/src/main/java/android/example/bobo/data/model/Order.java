package android.example.bobo.data.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private String userName;
    private String userImageUrl;
    private Date orderTime;
    private double totalPrice;
    private String orderStatus;
    private Date deliveryDate;
    private String deliverTo;
    private List<FoodItem> foodItems;

    // Local property for backward compatibility
    private int userImageResId;  // User profile image resource ID for local images

    // Default constructor for serialization
    public Order() {
    }

    // Constructor with essential fields
    public Order(String orderId, String userName, String userImageUrl, Date orderTime,
                 double totalPrice, String orderStatus, String deliverTo, List<FoodItem> foodItems) {
        this.orderId = orderId;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.deliverTo = deliverTo;
        this.foodItems = foodItems;
    }

    // Legacy constructor for backward compatibility
    public Order(String orderId, String userName, String dishName, String orderTime,
                 String totalPrice, int userImageResId) {
        this.orderId = orderId;
        this.userName = userName;
        this.userImageResId = userImageResId;

        try {
            this.totalPrice = Double.parseDouble(totalPrice.replace("$", ""));
        } catch (NumberFormatException e) {
            this.totalPrice = 0.0;
        }
    }

    // Helper method to get formatted price as string with currency symbol
    public String getFormattedPrice() {
        return "$" + String.format("%.2f", totalPrice);
    }

    // Helper method to get a display name for the first food item
    public String getMainDishName() {
        if (foodItems != null && !foodItems.isEmpty()) {
            return foodItems.get(0).getName();
        }
        return "N/A";
    }

    // Helper method for order status constants
    public static class Status {
        public static final String PENDING = "PENDING";
        public static final String PREPARING = "PREPARING";
        public static final String READY = "READY";
        public static final String INSHIPPING = "INSHIPPING";
        public static final String DELIVERED = "DELIVERED";
        public static final String CANCELLED = "CANCELLED";

        // Convert server status to display text
        public static String toDisplayText(String statusCode) {
            switch (statusCode) {
                case PENDING: return "Pending";
                case PREPARING: return "Preparing";
                case READY: return "Ready";
                case INSHIPPING: return "In delivery";
                case DELIVERED: return "Delivered";
                case CANCELLED: return "Order cancelled";
                default: return statusCode;
            }
        }
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStatusDisplay() {
        return Status.toDisplayText(orderStatus);
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public int getUserImageResId() {
        return userImageResId;
    }

    public void setUserImageResId(int userImageResId) {
        this.userImageResId = userImageResId;
    }
}
