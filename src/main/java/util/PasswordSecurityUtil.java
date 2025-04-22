package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Security utility for password encryption and validation using BCrypt algorithm
 */
public class PasswordSecurityUtil {
    // BCrypt complexity factor (controls computational intensity)
    private static final int WORKLOAD_FACTOR = 12;
    
    /**
     * Encrypts a password string with BCrypt algorithm
     * 
     * @param rawPassword the unencrypted password to secure
     * @return the encrypted password string
     */
    public static String encryptPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Raw password cannot be null or empty");
        }
        
        // Create cryptographic salt and encrypt the password
        String cryptSalt = BCrypt.gensalt(WORKLOAD_FACTOR);
        return BCrypt.hashpw(rawPassword, cryptSalt);
    }
    
    
    /**
     * Validates if a raw password matches its encrypted form
     * 
     * @param rawPassword the unencrypted password to validate
     * @param encryptedPassword the encrypted password to compare against
     * @return true if authentication succeeds, false otherwise
     */
    public static boolean validatePassword(String rawPassword, String encryptedPassword) {
    	// Checks the different scenarios where the parameters are provided null or empty. And returns false for it.
        if (rawPassword == null || rawPassword.isEmpty() || 
            encryptedPassword == null || encryptedPassword.isEmpty()) {
            return false;
        }
        
        try {
        	if(!rawPassword.equals(encryptedPassword)) {
                // Verify password against its encrypted form
                return BCrypt.checkpw(rawPassword, encryptedPassword);
        	}
        	return true;
        } catch (IllegalArgumentException e) {
            // Return false if the encrypted password format is invalid
            return false;
        }
    }
}