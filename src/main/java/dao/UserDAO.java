package dao;

import model.User;
import util.DBConnection;
import util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Creates a new customer user.
     */
    public void createCustomer(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, role, contact_phone, profile_pic) VALUES (?, ?, ?, 'customer', ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getContactPhone());
            stmt.setString(5, user.getProfilePic());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates a user's details.
     */
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, contact_phone = ?, profile_pic = ? WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getContactPhone());
            stmt.setString(3, user.getProfilePic());
            stmt.setInt(4, user.getUserId());
            stmt.executeUpdate();
        }
    }

    /**
     * Soft deletes a user.
     */
    public void softDelete(int userId) throws SQLException {
        String sql = "UPDATE users SET is_deleted = TRUE WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves a user by ID.
     */
    public User findById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
                return null;
            }
        }
    }

    /**
     * Retrieves a user by email.
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
                return null;
            }
        }
    }

    /**
     * Retrieves all non-deleted users.
     */
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users WHERE is_deleted = FALSE";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
            return users;
        }
    }

    /**
     * Retrieves all non-deleted admins.
     */
    public List<User> getAdmins() throws SQLException {
        String sql = "SELECT user_id, name, email, role FROM users WHERE role = 'admin' AND is_deleted = FALSE ORDER BY name";
        List<User> admins = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                admins.add(user);
            }
            return admins;
        }
    }

    /**
     * Updates a user's password after verification.
     */
    public boolean updatePassword(int userId, String currentPassword, String newPassword) throws SQLException {
        // Verify current password
        String sql = "SELECT password FROM users WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (!PasswordHasher.verifyPassword(currentPassword, hashedPassword)) {
                        return false; // Incorrect current password
                    }
                } else {
                    return false; // User not found
                }
            }
        }

        // Update password
        sql = "UPDATE users SET password = ? WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String hashedNewPassword = PasswordHasher.hashPassword(newPassword);
            stmt.setString(1, hashedNewPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        }
    }

    /**
     * Maps ResultSet to a User object.
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setContactPhone(rs.getString("contact_phone"));
        user.setProfilePic(rs.getString("profile_pic"));
        user.setCreatedAt(rs.getString("created_at"));
        user.setDeleted(rs.getBoolean("is_deleted"));
        return user;
    }
}