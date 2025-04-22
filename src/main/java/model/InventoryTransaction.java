// InventoryTransaction.java
package model;

import java.sql.Timestamp;

public class InventoryTransaction {
    private int transactionId;
    private int productId;
    private int quantity;
    private String transactionType;
    private Integer referenceId;  // Using Integer to allow null values
    private String notes;
    private Timestamp transactionDate;
    private Integer userId;  // Using Integer to allow null values
    
    // For relationship display
    private String productName;
    private String userName;
    
    // Default constructor
    public InventoryTransaction() {
    }
    
    // Constructor with essential fields
    public InventoryTransaction(int productId, int quantity, String transactionType) {
        this.productId = productId;
        this.quantity = quantity;
        this.transactionType = transactionType;
    }
    
    // Full constructor
    public InventoryTransaction(int transactionId, int productId, int quantity, 
                              String transactionType, Integer referenceId, String notes, 
                              Timestamp transactionDate, Integer userId) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.referenceId = referenceId;
        this.notes = notes;
        this.transactionDate = transactionDate;
        this.userId = userId;
    }
    
    // Getters and setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    // Utility methods
    public boolean isPositiveTransaction() {
        return "purchase".equalsIgnoreCase(transactionType) || 
               "return".equalsIgnoreCase(transactionType);
    }
    
    public boolean isNegativeTransaction() {
        return "sale".equalsIgnoreCase(transactionType);
    }
    
    @Override
    public String toString() {
        return "InventoryTransaction{" +
                "transactionId=" + transactionId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", transactionType='" + transactionType + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
