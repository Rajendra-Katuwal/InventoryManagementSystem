package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {

	private static String url;
	private static String user;
	private static String password;

	// Load database configuration from db.properties
	static {
		try (InputStream inpStr = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
			if (inpStr == null) {
				throw new RuntimeException("db.properties not found in classpath");
			}
			Properties prop = new Properties();
			prop.load(inpStr);

			// Validate required properties
			validateProperty(prop, "db.driver");
			validateProperty(prop, "db.url");
			validateProperty(prop, "db.username");

			Class.forName(prop.getProperty("db.driver"));
			url = prop.getProperty("db.url");
			user = prop.getProperty("db.username");
			password = prop.getProperty("db.password");
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize DBConnection", e);
		}
	}

	// Validate that a property exists and is not empty
	private static void validateProperty(Properties prop, String key) {
		String value = prop.getProperty(key);
		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException("Missing or empty property: " + key);
		}
	}

	// Get a database connection
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	// Close a database connection
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException("Failed to close connection", e);
			}
		}
	}
}