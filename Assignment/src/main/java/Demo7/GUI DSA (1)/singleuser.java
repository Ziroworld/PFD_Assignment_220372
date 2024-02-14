import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class singleuser extends JFrame {
    JPanel userDetailsPanel;
    JPanel friendSuggestionPanel;
    String loggedInUser;

    public singleuser(String loggedInUser) {
        this.loggedInUser = loggedInUser;

        setTitle("Homepage");
        setSize(800, 400); // Adjusted size for two frames
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main container panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Left frame (60%)
        JPanel leftFrame = new JPanel(new BorderLayout());
        userDetailsPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane userDetailsScrollPane = new JScrollPane(userDetailsPanel);
        leftFrame.add(userDetailsScrollPane, BorderLayout.CENTER);
        mainPanel.add(leftFrame);

        // Right frame (40%)
        JPanel rightFrame = new JPanel(new BorderLayout());
        friendSuggestionPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane friendScrollPane = new JScrollPane(friendSuggestionPanel);
        rightFrame.add(friendScrollPane, BorderLayout.NORTH); // Changed to NORTH for top alignment

        // Display user details and friend suggestions
        displayUserDetails();
        displayFriendSuggestions();

        mainPanel.add(rightFrame);

        add(mainPanel);
        setVisible(true);
    }

    private void displayUserDetails() {
        try {
            Scanner scanner = new Scanner(new File("users.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(loggedInUser)) {
                    String username = parts[0];
                    String hobbies = parts[1];  // Fetch hobbies instead of password
                    JPanel userDetailCard = createUserDetailCard(username, hobbies);
                    userDetailsPanel.add(userDetailCard);
                    break; // Stop after finding the user's details
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
        }
    }

    private void displayFriendSuggestions() {
        // Add header for friend suggestions
        JLabel suggestionHeader = new JLabel("Friend Suggestions");
        suggestionHeader.setHorizontalAlignment(SwingConstants.CENTER);
        friendSuggestionPanel.add(suggestionHeader);

        // Retrieve hobbies of logged-in user
        Set<String> loggedInUserHobbies = getUserHobbies(loggedInUser);

        // Display friend suggestions based on hobbies
        try {
            Scanner scanner = new Scanner(new File("users.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3 && !parts[0].equals(loggedInUser)) { // Exclude the logged-in user
                    String username = parts[0];
                    String hobbies = parts[1];  // Fetch hobbies instead of password
                    // Check if any hobby of the logged-in user matches with the hobbies of the current user
                    for (String hobby : loggedInUserHobbies) {
                        if (hobbies.contains(hobby)) {
                            JPanel suggestionPanel = createProfileCard(username, hobbies);
                            JButton addButton = new JButton("Add friend");
                            addButton.addActionListener(e -> handleAddFriend(username));
                            suggestionPanel.add(addButton);
                            friendSuggestionPanel.add(suggestionPanel);
                            break; // Break after adding the user to suggestions to avoid duplicate suggestions
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
        }
    }

    private Set<String> getUserHobbies(String username) {
        Set<String> hobbies = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File("users.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(username)) {
                    String userHobbies = parts[1];
                    String[] hobbyArray = userHobbies.split(";");
                    hobbies.addAll(Arrays.asList(hobbyArray));
                    break; // Stop after finding the user's hobbies
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
        }
        return hobbies;
    }

    private JPanel createProfileCard(String username, String hobbies) {
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel usernameLabel = new JLabel("Username: " + username);
        JLabel hobbiesLabel = new JLabel("Hobbies: " + hobbies);

        profileCard.add(usernameLabel);
        profileCard.add(hobbiesLabel);

        return profileCard;
    }

    private void handleAddFriend(String friendName) {
        // Logic to add a friend
        JOptionPane.showMessageDialog(this, "Added " + friendName + " as a friend!");
    }

    private JPanel createUserDetailCard(String username, String hobbies) {
        JPanel userDetailCard = new JPanel();
        userDetailCard.setLayout(new BoxLayout(userDetailCard, BoxLayout.Y_AXIS));
        userDetailCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel usernameLabel = new JLabel("Username: " + username);
        JLabel hobbiesLabel = new JLabel("Hobbies: " + hobbies);

        userDetailCard.add(usernameLabel);
        userDetailCard.add(hobbiesLabel);

        return userDetailCard;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new singleuser("ExampleUser"));
    }
}
