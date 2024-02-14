import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Userprofile extends JFrame {
    JPanel profilePanel;
    JTextField searchField;
    JPanel friendSuggestionPanel; // New panel for friend suggestions

    public Userprofile() {
        setTitle("User Profiles");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add welcome label
        JLabel welcomeLabel = new JLabel("Welcome to profile page");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Add search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> searchByUsername());
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchByUsername());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        // Add profile panel
        profilePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JScrollPane profileScrollPane = new JScrollPane(profilePanel);
        profileScrollPane.setPreferredSize(new Dimension(300, 300)); // Set preferred size for profile panel
        mainPanel.add(profileScrollPane, BorderLayout.CENTER);

        // Add friend suggestion panel
        friendSuggestionPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane friendScrollPane = new JScrollPane(friendSuggestionPanel);
        friendScrollPane.setPreferredSize(new Dimension(200, 300)); // Set preferred size for friend suggestion panel
        mainPanel.add(friendScrollPane, BorderLayout.EAST); // Add to the EAST

        // Display user profiles and friend suggestions
        displayUserProfiles();
        displayFriendSuggestions(); // New method to display friend suggestions

        add(mainPanel);
        setVisible(true);
    }

    private void displayUserProfiles() {
        try {
            Scanner scanner = new Scanner(new File("users.csv"));
            if (scanner.hasNextLine()) {
                // Display only the first profile in the main panel
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String email = parts[2];
                    JPanel profileCard = createProfileCard(username, email);
                    profilePanel.add(profileCard);
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

        // Display remaining profiles in friend suggestions
        try {
            Scanner scanner = new Scanner(new File("users.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String email = parts[2];
                    JPanel suggestionPanel = createProfileCard(username, email);
                    JButton addButton = new JButton("Add");
                    addButton.addActionListener(e -> addFriend(username));
                    suggestionPanel.add(addButton);
                    friendSuggestionPanel.add(suggestionPanel);
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
        }
    }

    private JPanel createProfileCard(String username, String email) {
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel usernameLabel = new JLabel("Username: " + username);
        JLabel emailLabel = new JLabel("Hobbies: " + email);

        profileCard.add(usernameLabel);
        profileCard.add(emailLabel);

        return profileCard;
    }

    private void searchByUsername() {
        String searchTerm = searchField.getText();
        if (searchTerm.isEmpty()) {
            // If the search field is empty, display all user profiles
            displayUserProfiles();
        } else {
            // Clear existing profile panel before displaying search results
            profilePanel.removeAll();
            profilePanel.revalidate();
            profilePanel.repaint();
            
            try {
                Scanner scanner = new Scanner(new File("users.csv"));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 3 && parts[0].equalsIgnoreCase(searchTerm.trim())) {
                        String username = parts[0];
                        String email = parts[2];
                        JPanel profileCard = createProfileCard(username, email);
                        profilePanel.add(profileCard);
                    }
                }
                scanner.close();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
            }
        }
    }

    private void addFriend(String friendName) {
        // Logic to add a friend
        JOptionPane.showMessageDialog(this, "Added " + friendName + " as a friend!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Userprofile();
            }
        });
    }
}
