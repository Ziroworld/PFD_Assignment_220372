package Task6.Database;

import Task6.Model.ImageData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
    public static void saveImage(ImageData image) {
        String sql1 = "INSERT INTO images (image_url, image_data) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql1 )) {
            statement.setString(1, image.getImageUrl());
            statement.setBytes(2, image.getImageData());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getImageData(String imageUrl) {
        String sql2 = "SELECT image_data FROM images WHERE image_url = ?";
        byte[] imageData = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql2)) {
            statement.setString(1, imageUrl);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    imageData = resultSet.getBytes("imageData");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imageData;
    }

    private static Connection getConnection() throws SQLException {
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
