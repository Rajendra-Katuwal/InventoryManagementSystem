// Payment.java
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private Timestamp paidAt;
    
    // For relationship display
    private String orderReference;
    
    // Default constructor
    public Payment() {
    }
    
    // Constructor with essential fields
    public Payment(int orderId, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "pending";
    }
    
    // Full constructor
    public Payment(int paymentId, int orderId, BigDecimal amount, String paymentMethod,
                  String transactionId, String status, Timestamp paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
        this.paidAt = paidAt;
    }
    
    // Getters and setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }
    
    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }
    
    // Utility methods
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    public boolean isPaid() {
        return "paid".equalsIgnoreCase(status);
    }
    
    public boolean isFailed() {
        return "failed".equalsIgnoreCase(status);
    }
    
    public boolean isRefunded() {
        return "refunded".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", paidAt=" + paidAt +
                '}';
    }
}
