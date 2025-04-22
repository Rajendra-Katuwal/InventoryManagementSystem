// Product.java
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private String sku;
    private String barcode;
    private String imageUrl;
    private int quantityInStock;
    private int reorderLevel;
    private BigDecimal unitPrice;
    private BigDecimal costPrice;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer supplierId;  // Using Integer to allow null values
    private Integer categoryId;  // Using Integer to allow null values
    
    // For relationship display
    private String supplierName;
    private String categoryName;
    
    // Default constructor
    public Product() {
    }
    
    // Constructor with essential fields
    public Product(String productName, BigDecimal unitPrice, int quantityInStock) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
        this.isActive = true;
    }
    
    // Full constructor
    public Product(int productId, String productName, String description, String sku, 
                  String barcode, String imageUrl, int quantityInStock, int reorderLevel, 
                  BigDecimal unitPrice, BigDecimal costPrice, boolean isActive, 
                  Timestamp createdAt, Timestamp updatedAt, Integer supplierId, Integer categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.sku = sku;
        this.barcode = barcode;
        this.imageUrl = imageUrl;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
        this.unitPrice = unitPrice;
        this.costPrice = costPrice;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
    }
    
    // Getters and setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    // Utility methods
    public boolean needsReorder() {
        return quantityInStock <= reorderLevel;
    }
    
    public BigDecimal calculateProfit() {
        if (costPrice != null && unitPrice != null) {
            return unitPrice.subtract(costPrice);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", sku='" + sku + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", unitPrice=" + unitPrice +
                ", isActive=" + isActive +
                '}';
    }
}
