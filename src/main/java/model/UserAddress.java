// UserAddress.java
package model;

public class UserAddress {
    private int addressId;
    private int userId;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String addressType;
    private boolean isDefault;
    
    // Default constructor
    public UserAddress() {
    }
    
    // Constructor with essential fields
    public UserAddress(int userId, String addressLine, String city, String postalCode, 
                       String country, String addressType) {
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.addressType = addressType;
    }
    
    // Full constructor
    public UserAddress(int addressId, int userId, String addressLine, String city, 
                       String state, String postalCode, String country, 
                       String addressType, boolean isDefault) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.addressType = addressType;
        this.isDefault = isDefault;
    }
    
    // Getters and setters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    // Utility methods
    public boolean isShippingAddress() {
        return "shipping".equalsIgnoreCase(addressType);
    }
    
    public boolean isBillingAddress() {
        return "billing".equalsIgnoreCase(addressType);
    }
    
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(addressLine).append(", ");
        sb.append(city).append(", ");
        if (state != null && !state.isEmpty()) {
            sb.append(state).append(", ");
        }
        sb.append(postalCode).append(", ");
        sb.append(country);
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "UserAddress{" +
                "addressId=" + addressId +
                ", userId=" + userId +
                ", addressLine='" + addressLine + '\'' +
                ", city='" + city + '\'' +
                ", addressType='" + addressType + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
