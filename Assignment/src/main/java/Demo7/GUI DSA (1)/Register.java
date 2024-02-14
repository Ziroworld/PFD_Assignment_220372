import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register extends JFrame implements ActionListener {
    JTextField usernameField, passwordField, hobbiesField;

    public Register() {
        setTitle("User Registration");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Username: "));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password: "));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Hobbies: "));
        hobbiesField = new JTextField();
        panel.add(hobbiesField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Register")) {
            String username = usernameField.getText();
            String password = new String(((JPasswordField) passwordField).getPassword());
            String hobbies = hobbiesField.getText();

            // Perform input validation
            if (username.isEmpty() || password.isEmpty() || hobbies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            // Insert the new user data into the MySQL database
            try (Connection connection = getConnection()) {
                String insertQuery = "INSERT INTO users (username, password, hobbies) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    preparedStatement.setString(3, hobbies);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    dispose();  // Close the registration window
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: Unable to register. Please try again.");
            }
        }
    }

    private Connection getConnection() throws SQLException {
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
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register());
    }
}
