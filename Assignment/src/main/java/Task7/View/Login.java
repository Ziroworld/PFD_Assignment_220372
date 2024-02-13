package Task7.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckBox;
    private JButton loginButton;

    public Login() {
        setTitle("Complex Login Window");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        rememberMeCheckBox = new JCheckBox("Remember Me");

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel()); // Placeholder for better spacing
        inputPanel.add(rememberMeCheckBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(80, 30)); // Set fixed size for the button
        buttonPanel.add(loginButton);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add authentication logic here
                if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                    JOptionPane.showMessageDialog(Login.this, "Login successful!");
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Login failed. Invalid credentials.");
                }
            }
        });

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private boolean authenticate(String username, String password) {
        // Replace this with your authentication logic
        return username.equals("admin") && password.equals("password");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}

