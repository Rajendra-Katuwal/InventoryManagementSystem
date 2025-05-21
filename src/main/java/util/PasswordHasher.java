package util;

import org.mindrot.jbcrypt.BCrypt;

// Utility class for secure password hashing and verification
public class PasswordHasher {

	private static final int LOG_ROUNDS = 12; // Configurable BCrypt work factor

	// Hash a password using BCrypt
	public static String hashPassword(String password) {
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}
		return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
	}

	// Verifies a password against its hash
	public static boolean verifyPassword(String password, String hashedPassword) {
		if (password == null || password.trim().isEmpty() || hashedPassword == null) {
			throw new IllegalArgumentException("Password and hash cannot be null or empty");
		}
		return BCrypt.checkpw(password, hashedPassword);
	}
}