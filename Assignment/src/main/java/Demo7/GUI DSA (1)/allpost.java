import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class allpost extends JFrame {
    JPanel profilePanel;

    public allpost() {
        setTitle("Post");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add welcome label
        JLabel welcomeLabel = new JLabel("Welcome to post page");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Add profile panel
        profilePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JScrollPane scrollPane = new JScrollPane(profilePanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        displayPosts();

        add(mainPanel);
        setVisible(true);
    }

    private void displayPosts() {
        try {
            Scanner scanner = new Scanner(new File("posts.csv"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String postContent = parts[1];
                    JPanel postCard = createPostCard(postContent);
                    profilePanel.add(postCard);
                }
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error: CSV file not found.");
        }
    }

    private JPanel createPostCard(String postContent) {
        JPanel postCard = new JPanel();
        postCard.setLayout(new BoxLayout(postCard, BoxLayout.Y_AXIS));
        postCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel postContentLabel = new JLabel("Post: " + postContent);

        JButton likeButton = new JButton("Like");
        likeButton.addActionListener(e -> likePost(postContent));

        postCard.add(postContentLabel);
        postCard.add(likeButton);

        return postCard;
    }

    private void likePost(String postContent) {
        // Your code to handle liking a post goes here
        JOptionPane.showMessageDialog(this, "You liked the post: " + postContent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new allpost();
            }
        });
    }
}
