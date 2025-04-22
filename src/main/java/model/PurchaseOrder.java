// PurchaseOrder.java
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder {
    private int poId;
    private int supplierId;
    private Timestamp orderDate;
    private Timestamp deliveryDate;
    private String status;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String notes;
    private Integer createdBy;  // Using Integer to allow null values
    
    // For relationship display
    private String supplierName;
    private String createdByName;
    private List<PurchaseOrderItem> items = new ArrayList<>();
    
    // Default constructor
    public PurchaseOrder() {
    }
    
    // Constructor with essential fields
    public PurchaseOrder(int supplierId, BigDecimal totalAmount) {
        this.supplierId = supplierId;
        this.totalAmount = totalAmount;
        this.status = "draft";
        this.paymentStatus = "unpaid";
    }
    
    // Full constructor
    public PurchaseOrder(int poId, int supplierId, Timestamp orderDate, Timestamp deliveryDate,
                        String status, BigDecimal totalAmount, String paymentStatus,
                        String notes, Integer createdBy) {
        this.poId = poId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.notes = notes;
        this.createdBy = createdBy;
    }
    
    // Getters and setters
    public int getPoId() {
        return poId;
    }

    public void setPoId(int poId) {
        this.poId = poId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
    }
    
    // Utility methods
    public void addItem(PurchaseOrderItem item) {
        items.add(item);
    }
    
    public boolean isDraft() {
        return "draft".equalsIgnoreCase(status);
    }
    
    public boolean isOrdered() {
        return "ordered".equalsIgnoreCase(status);
    }
    
    public boolean isReceived() {
        return "received".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }
    
    public boolean isUnpaid() {
        return "unpaid".equalsIgnoreCase(paymentStatus);
    }
    
    public boolean isPartiallyPaid() {
        return "partial".equalsIgnoreCase(paymentStatus);
    }
    
    public boolean isPaid() {
        return "paid".equalsIgnoreCase(paymentStatus);
    }
    
    public boolean isDelivered() {
        return deliveryDate != null && deliveryDate.before(new Timestamp(System.currentTimeMillis()));
    }
    
    public boolean isOverdue() {
        return deliveryDate != null && deliveryDate.before(new Timestamp(System.currentTimeMillis())) 
               && !"received".equalsIgnoreCase(status) && !"cancelled".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "poId=" + poId +
                ", supplierId=" + supplierId +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}