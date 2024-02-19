package Task7.ZenApp.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationSystem {
    private Connection connection;

    public RecommendationSystem() {
        // Connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/dsa_db", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateRecommendations(String username) {
        Map<String, List<String>> graph = buildGraph(username);
        List<String> recommendations = new ArrayList<>();

        // Find recommendations based on mutual friends
        for (String friend : graph.keySet()) {
            List<String> mutualFriends = findMutualFriends(graph, username, friend);
            for (String mutualFriend : mutualFriends) {
                if (!graph.get(username).contains(mutualFriend) && !recommendations.contains(mutualFriend)) {
                    recommendations.add(mutualFriend);
                }
            }
        }

        // Display recommendations
        System.out.println("Recommendations for " + username + ": " + recommendations);
    }

    private Map<String, List<String>> buildGraph(String username) {
        Map<String, List<String>> graph = new HashMap<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT friends FROM friendsof" + username);
            while (resultSet.next()) {
                String friend = resultSet.getString("friends");
                List<String> friendsList = new ArrayList<>();
                ResultSet friendSet = statement.executeQuery("SELECT friends FROM friendsof" + friend);
                while (friendSet.next()) {
                    friendsList.add(friendSet.getString("friends"));
                }
                graph.put(friend, friendsList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private List<String> findMutualFriends(Map<String, List<String>> graph, String user1, String user2) {
        List<String> mutualFriends = new ArrayList<>();
        List<String> friends1 = graph.get(user1);
        List<String> friends2 = graph.get(user2);
        for (String friend : friends1) {
            if (friends2.contains(friend)) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }
}
