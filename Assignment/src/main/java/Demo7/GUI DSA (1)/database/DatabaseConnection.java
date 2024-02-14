

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            String dbUrl = "jdbc:mysql://localhost:3306/pfd"; // Updated database name
            String username = "root";
            String password = "iknow";
            return DriverManager.getConnection(dbUrl, username, password);
        } catch (ClassNotFoundException e) {
            // Handle the ClassNotFoundException appropriately (e.g., log it)
            e.printStackTrace();
            throw new SQLException("MySQL JDBC driver not found.", e);
        } catch (SQLException e) {
            // Handle other SQLExceptions appropriately (e.g., log it)
            e.printStackTrace();
            throw e;
        }
    }
}

