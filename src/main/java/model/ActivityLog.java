// ActivityLog.java
package model;

import java.sql.Timestamp;

public class ActivityLog {
    private int logId;
    private Integer userId;  // Using Integer to allow null values
    private String action;
    private String entityType;
    private Integer entityId;  // Using Integer to allow null values
    private String details;
    private String ipAddress;
    private Timestamp timestamp;
    
    // For relationship display
    private String userName;
    
    // Default constructor
    public ActivityLog() {
    }
    
    // Constructor with essential fields
    public ActivityLog(String action, String entityType) {
        this.action = action;
        this.entityType = entityType;
    }
    
    // Constructor with user
    public ActivityLog(Integer userId, String action, String entityType, Integer entityId) {
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    // Full constructor
    public ActivityLog(int logId, Integer userId, String action, String entityType, 
                      Integer entityId, String details, String ipAddress, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
    }
    
    // Getters and setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public String toString() {
        return "ActivityLog{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", timestamp=" + timestamp +
                '}';
    }
}