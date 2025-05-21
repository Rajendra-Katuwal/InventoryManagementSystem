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

/** Data Access Object for managing User entities. */
public class UserDAO {

	/**
	 * Creates a new customer user with hashed password.
	 */
	public void createCustomer(User user) throws SQLException {
		if (user == null || user.getEmail() == null || user.getPassword() == null) {
			throw new SQLException("User email and password are required.");
		}
		String sql = "INSERT INTO users (name, email, password, role, contact_phone, profile_pic) VALUES (?, ?, ?, 'customer', ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getName() != null ? user.getName() : "");
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getContactPhone() != null ? user.getContactPhone() : "");
			stmt.setString(5, user.getProfilePic());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) { // Unique constraint violation (e.g., duplicate email)
				throw new SQLException("Email already exists.");
			}
			throw e;
		}
	}

	/**
	 * Updates a user's profile (email, contact phone, profile picture).
	 */
	public void updateProfile(int userId, String email, String contactPhone, String profilePic) throws SQLException {
		if (email == null || email.trim().isEmpty()) {
			throw new SQLException("Valid email is required.");
		}
		String sql = "UPDATE users SET email = ?, contact_phone = ?, profile_pic = ? WHERE user_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, contactPhone != null ? contactPhone : "");
			stmt.setString(3, profilePic);
			stmt.setInt(4, userId);
			int rows = stmt.executeUpdate();
			if (rows == 0) {
				throw new SQLException("User not found or already deleted.");
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				throw new SQLException("Email already in use by another user.");
			}
			throw e;
		}
	}

	/**
	 * Updates a user's details (name, contact phone, profile picture).
	 */
	public void update(User user) throws SQLException {
		if (user == null || user.getUserId() <= 0) {
			throw new SQLException("Valid user ID is required.");
		}
		String sql = "UPDATE users SET name = ?, contact_phone = ?, profile_pic = ? WHERE user_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getName() != null ? user.getName() : "");
			stmt.setString(2, user.getContactPhone() != null ? user.getContactPhone() : "");
			stmt.setString(3, user.getProfilePic());
			stmt.setInt(4, user.getUserId());
			int rows = stmt.executeUpdate();
			if (rows == 0) {
				throw new SQLException("User not found or already deleted.");
			}
		}
	}

	/**
	 * Soft-deletes a user by ID.
	 */
	public void softDelete(int userId) throws SQLException {
		String sql = "UPDATE users SET is_deleted = TRUE WHERE user_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			int rows = stmt.executeUpdate();
			if (rows == 0) {
				throw new SQLException("User not found or already deleted.");
			}
		}
	}

	/**
	 * Retrieves a user by ID, or null if not found.
	 */
	public User findById(int userId) throws SQLException {
		String sql = "SELECT * FROM users WHERE user_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next() ? mapUser(rs) : null;
			}
		}
	}

	/**
	 * Retrieves a user by email, or null if not found.
	 */
	public User findByEmail(String email) throws SQLException {
		if (email == null || email.trim().isEmpty()) {
			throw new SQLException("Email cannot be null or empty.");
		}
		String sql = "SELECT * FROM users WHERE email = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next() ? mapUser(rs) : null;
			}
		}
	}

	/**
	 * Retrieves all non-deleted users.
	 */
	public List<User> findAll() throws SQLException {
		String sql = "SELECT * FROM users WHERE is_deleted = FALSE ORDER BY created_at DESC";
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
	 * Retrieves all non-deleted admin users.
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
	 * Updates a user's password after verifying the current password.
	 */
	public boolean updatePassword(int userId, String currentPassword, String newPassword) throws SQLException {
		if (currentPassword == null || newPassword == null) {
			throw new SQLException("Current and new passwords are required.");
		}
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
			int rows = stmt.executeUpdate();
			return rows > 0;
		}
	}

	/**
	 * Maps a ResultSet row to a User object.
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
		user.setCreatedAt(rs.getString("created_at") != null ? rs.getString("created_at") : "");
		user.setDeleted(rs.getBoolean("is_deleted"));
		return user;
	}
}