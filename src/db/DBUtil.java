package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // MySQL connection info
    private static final String URL = "jdbc:mysql://localhost:3306/elearning_db";
    private static final String USER = "root";   // MySQL username
    private static final String PASSWORD = "root";  // MySQL password

    // Method to get connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
