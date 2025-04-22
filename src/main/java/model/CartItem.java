// CartItem.java
package model;

import java.sql.Timestamp;

public class CartItem {
    private int cartitemId;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp addedAt;
    
    // For relationship display
    private String productName;
    private double unitPrice;
    private String imageUrl;
    
    // Default constructor
    public CartItem() {
    }
    
    // Constructor with essential fields
    public CartItem(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Full constructor
    public CartItem(int cartitemId, int userId, int productId, int quantity, Timestamp addedAt) {
        this.cartitemId = cartitemId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }
    
    // Getters and setters
    public int getCartitemId() {
        return cartitemId;
    }

    public void setCartitemId(int cartitemId) {
        this.cartitemId = cartitemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    // Utility methods
    public double getSubtotal() {
        return quantity * unitPrice;
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "cartitemId=" + cartitemId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                '}';
    }
}
