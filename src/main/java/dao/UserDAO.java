package dao;

import model.User;
import util.DBConnectionUtil;
import util.PasswordSecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // Create a new user
    public static boolean createUser(User user) {
        String sql = "INSERT INTO User (firstname, lastname, email, password, phone_number, role) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getRole());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Get user by ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM User WHERE user_id = ?";
        User user = null;
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    // Get user by email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        User user = null;
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    // Get all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM User";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DBConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    // Update user
    public boolean updateUser(User user) {
        String sql = "UPDATE User SET firstname = ?, lastname = ?, email = ?, phone_number = ?, is_active = ?, role = ?, profile_url = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setBoolean(5, user.isActive());
            stmt.setString(6, user.getRole());
            stmt.setString(7, user.getProfileUrl());
            stmt.setInt(8, user.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Update password
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE User SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Delete user
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE user_id = ?";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public User authenticate(String email, String password) {
        String sql = "SELECT * FROM User WHERE email = ? AND is_active = TRUE";
        User user = null;

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Validate entered password against the stored hashed password
                if (PasswordSecurityUtil.validatePassword(password, storedPassword)) {
                    user = mapResultSetToUser(rs);
                }
            }

        } catch (SQLException e) {
        	System.out.println("error in authenticate method");
            e.printStackTrace();
        }

        return user;
    }

    
    // Check if email already exists in the database
    public static boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ?";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setActive(rs.getBoolean("is_active"));
        user.setRole(rs.getString("role"));
        user.setJoinedAt(rs.getTimestamp("joined_at"));
        user.setProfileUrl(rs.getString("profile_url"));
        return user;
    }
}