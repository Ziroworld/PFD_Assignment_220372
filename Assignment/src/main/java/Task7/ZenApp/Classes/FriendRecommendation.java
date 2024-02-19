package Task7.ZenApp.Classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRecommendation {
    private Connection connection;

    // Constructor to initialize the database connection
    public FriendRecommendation(Connection connection) {
        this.connection = connection;
    }

    // Method to retrieve friends list for a user from the database
    public List<String> getFriendsList(String user) {
        List<String> friends = new ArrayList<>();
        try {
            // Prepare and execute SQL query to fetch friends of the user from the database
            String query = "SELECT friends FROM friendsof" + user;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Iterate through the result set and add each friend to the list
            while (resultSet.next()) {
                friends.add(resultSet.getString("friends"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    // Method to generate friend recommendations based on mutual friends
    public List<String> generateFriendRecommendations(String user) {
        List<String> recommendations = new ArrayList<>();
        List<String> friends = getFriendsList(user);

        // Iterate through each friend of the user
        for (String friend : friends) {
            // Get friends of the friend (mutual friends)
            List<String> friendsOfFriend = getFriendsList(friend);

            // Iterate through the friends of the friend
            for (String potentialFriend : friendsOfFriend) {
                // Check if potential friend is not the user and not already a direct friend
                if (!potentialFriend.equals(user) && !friends.contains(potentialFriend)) {
                    // Add potential friend to recommendations if not already recommended
                    if (!recommendations.contains(potentialFriend)) {
                        recommendations.add(potentialFriend);
                    }
                }
            }
        }
        return recommendations;
    }

    public void showRecommendations(String user) {
        // Generate friend recommendations for the user
        List<String> recommendations = generateFriendRecommendations(user);

        // Display the recommendations (you can customize this according to your UI)
        System.out.println("Friend Recommendations for " + user + ":");
        for (String recommendation : recommendations) {
            System.out.println(recommendation);
        }
    }


    // Sample usage
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dsa_db", "username", "password");
            FriendRecommendation friendRecommendation = new FriendRecommendation(connection);
            String user = "ashishmool";

            // Generate and display friend recommendations for the user
            List<String> recommendations = friendRecommendation.generateFriendRecommendations(user);
            System.out.println("Friend Recommendations for " + user + ":");
            for (String recommendation : recommendations) {
                System.out.println(recommendation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
