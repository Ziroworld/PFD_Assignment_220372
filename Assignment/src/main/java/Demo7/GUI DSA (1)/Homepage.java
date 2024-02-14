import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Homepage extends JFrame {
    JPanel profilePanel;
    JTextField postField;
    JButton postButton;
    String currentUserID; // To store the ID of the currently logged-in user

    public Homepage() {
        setTitle("User Profiles and Posts");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Home Page");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Add profile panel
        profilePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JScrollPane profileScrollPane = new JScrollPane(profilePanel);
        mainPanel.add(profileScrollPane, BorderLayout.CENTER);

        // Add post section
        JPanel postPanel = new JPanel(new BorderLayout());
        postField = new JTextField();
        postButton = new JButton("Post");
        postButton.addActionListener(e -> addPost());
        postPanel.add(postField, BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);
        mainPanel.add(postPanel, BorderLayout.SOUTH);

        // Add buttons to display all posts and all profiles
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton viewAllPostsButton = new JButton("View All Posts");
        JButton viewAllProfilesButton = new JButton("View All Profiles");
        viewAllPostsButton.addActionListener(e -> viewAllPosts());
        viewAllProfilesButton.addActionListener(e -> viewAllProfiles());
        buttonPanel.add(viewAllPostsButton);
        buttonPanel.add(viewAllProfilesButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        displayUserProfiles();

        add(mainPanel);
        setVisible(true);
    }

    private void displayUserProfiles() {
        try {
            Scanner scanner = new Scanner(new File("users.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[1]; // index 1 is username
                    JPanel profileCard = createProfileCard(username);
                    profilePanel.add(profileCard);

                    // Assuming you have a method to retrieve the current user ID after login
                    currentUserID = username;
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
        }
    }

    private JPanel createProfileCard(String username) {
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel usernameLabel = new JLabel("Username: " + username);
        profileCard.add(usernameLabel);

        return profileCard;
    }

    private void addPost() {
        String postContent = postField.getText();
        if (!postContent.isEmpty()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("posts.csv", true));
                writer.write(currentUserID + "," + postContent);
                writer.newLine();
                writer.close();
                JOptionPane.showMessageDialog(this, "Post added successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a post.");
        }
    }

    private void viewAllPosts() {
        // Open a new window to view all posts
        new allpost();
    }

    private void viewAllProfiles() {
        // Open a new window to view all profiles
        new Userprofile();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Homepage();
            }
        });
    }
}
