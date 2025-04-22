// PurchaseOrderItem.java
package model;

import java.math.BigDecimal;

public class PurchaseOrderItem {
    private int poItemId;
    private int poId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    private int receivedQuantity;
    
    // For relationship display
    private String productName;
    private String sku;
    
    // Default constructor
    public PurchaseOrderItem() {
    }
    
    // Constructor with essential fields
    public PurchaseOrderItem(int poId, int productId, int quantity, BigDecimal unitPrice) {
        this.poId = poId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.receivedQuantity = 0;
    }
    
    // Full constructor
    public PurchaseOrderItem(int poItemId, int poId, int productId, int quantity, 
                           BigDecimal unitPrice, int receivedQuantity) {
        this.poItemId = poItemId;
        this.poId = poId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.receivedQuantity = receivedQuantity;
    }
    
    // Getters and setters
    public int getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(int poItemId) {
        this.poItemId = poItemId;
    }

    public int getPoId() {
        return poId;
    }

    public void setPoId(int poId) {
        this.poId = poId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    
    // Utility methods
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
    
    public boolean isFullyReceived() {
        return receivedQuantity >= quantity;
    }
    
    public boolean isPartiallyReceived() {
        return receivedQuantity > 0 && receivedQuantity < quantity;
    }
    
    public int getPendingQuantity() {
        return quantity - receivedQuantity;
    }
    
    @Override
    public String toString() {
        return "PurchaseOrderItem{" +
                "poItemId=" + poItemId +
                ", poId=" + poId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", receivedQuantity=" + receivedQuantity +
                '}';
    }
}