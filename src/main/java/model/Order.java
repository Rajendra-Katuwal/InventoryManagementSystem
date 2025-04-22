// Order.java
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private String orderStatus;
    private BigDecimal totalAmount;
    private Integer shippingAddressId;  // Using Integer to allow null values
    private Integer billingAddressId;   // Using Integer to allow null values
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // For relationship display
    private String userName;
    private String userEmail;
    private List<OrderItem> orderItems = new ArrayList<>();
    
    // Default constructor
    public Order() {
    }
    
    // Constructor with essential fields
    public Order(int userId, BigDecimal totalAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderStatus = "pending";
    }
    
    // Full constructor
    public Order(int orderId, int userId, String orderStatus, BigDecimal totalAmount, 
                Integer shippingAddressId, Integer billingAddressId, String notes, 
                Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.shippingAddressId = shippingAddressId;
        this.billingAddressId = billingAddressId;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Integer shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public Integer getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Integer billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    // Utility methods
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }
    
    public boolean isPending() {
        return "pending".equalsIgnoreCase(orderStatus);
    }
    
    public boolean isProcessing() {
        return "processing".equalsIgnoreCase(orderStatus);
    }
    
    public boolean isShipped() {
        return "shipped".equalsIgnoreCase(orderStatus);
    }
    
    public boolean isDelivered() {
        return "delivered".equalsIgnoreCase(orderStatus);
    }
    
    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(orderStatus);
    }
    
    public int getTotalItems() {
        int total = 0;
        for (OrderItem item : orderItems) {
            total += item.getQuantity();
        }
        return total;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalAmount=" + totalAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}
