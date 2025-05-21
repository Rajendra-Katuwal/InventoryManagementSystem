package dao;

import model.ContactMessage;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ContactMessageDAO {

    /**
     * Saves a new contact message.
     */
    public void saveContactMessage(ContactMessage message) throws SQLException {
        if (message == null || message.getEmail() == null || message.getEmail().trim().isEmpty() ||
            message.getName() == null || message.getName().trim().isEmpty() ||
            message.getMessage() == null || message.getMessage().trim().isEmpty()) {
            throw new SQLException("Contact message fields (name, email, message) cannot be null or empty");
        }

        String sql = "INSERT INTO contact_messages (user_id, name, email, subject, message) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (message.getUserId() != null) {
                stmt.setInt(1, message.getUserId());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, message.getName());
            stmt.setString(3, message.getEmail());
            stmt.setString(4, message.getSubject());
            stmt.setString(5, message.getMessage());
            stmt.executeUpdate();
        }
    }
    
    /**
     * Retrieves all contact messages.
     */
    public List<ContactMessage> getAllMessages() throws SQLException {
        List<ContactMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM contact_messages ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                messages.add(mapContactMessage(rs));
            }
        }
        return messages;
    }

    private ContactMessage mapContactMessage(ResultSet rs) throws SQLException {
        ContactMessage message = new ContactMessage();
        message.setId(rs.getInt("id")); // Adjust for actual column name
        message.setUserId(rs.getInt("user_id"));
        message.setName(rs.getString("name"));
        message.setEmail(rs.getString("email"));
        message.setSubject(rs.getString("subject"));
        message.setMessage(rs.getString("message"));
        return message;
    }
}