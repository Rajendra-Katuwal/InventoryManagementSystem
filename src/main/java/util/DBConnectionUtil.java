package util;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


// Class for Database connection related utilities
public class DBConnectionUtil {
    private static String url;
    private static String user;
    private static String password;

    // This static block runs only once when the class is first loaded.
    static {
        try(InputStream inpStr = DBConnectionUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            prop.load(inpStr);
            Class.forName(prop.getProperty("db.driver"));
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    // Method for getting the database connection using the loaded connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    // Method for closing a database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

