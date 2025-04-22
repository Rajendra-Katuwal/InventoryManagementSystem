// Category.java
package model;

import java.sql.Timestamp;

public class Category {
    private int categoryId;
    private String name;
    private String description;
    private Integer parentCategoryId;  // Using Integer to allow null values
    private Timestamp createdAt;
    
    // Default constructor
    public Category() {
    }
    
    // Constructor with essential fields
    public Category(String name) {
        this.name = name;
    }
    
    // Constructor with parent category
    public Category(String name, Integer parentCategoryId) {
        this.name = name;
        this.parentCategoryId = parentCategoryId;
    }
    
    // Full constructor
    public Category(int categoryId, String name, String description, 
                    Integer parentCategoryId, Timestamp createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Utility methods
    public boolean hasParent() {
        return parentCategoryId != null;
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                '}';
    }
}
