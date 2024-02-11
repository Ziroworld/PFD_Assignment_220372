
package Task6.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Window extends JFrame {
    private JTextField urlField;
    private JButton downloadButton;
    private JLabel imageLabel;
    private JProgressBar progressBar; // Added progress bar

    public Window() {
        // Frame setup
        setTitle("Image Downloader");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE); // Set a background color if needed

        // Sub-panel for URL input and download button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // FlowLayout with horizontal and vertical gaps

        // URL input field
        urlField = new JTextField(35);
        inputPanel.add(urlField);

        // Download button
        downloadButton = new JButton("Download");
        downloadButton.setBackground(new Color(173, 216, 230)); // Light blue color
        downloadButton.setForeground(Color.BLACK); // Text color
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imageUrl = urlField.getText();
                if (!imageUrl.isEmpty()) {
                    downloadImageAsync(imageUrl);
                }
            }
        });
        inputPanel.add(downloadButton);

        // Adding the input panel to the main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel to display downloaded image
        JPanel imagePanel = new JPanel();
        imageLabel = new JLabel();
        imagePanel.add(imageLabel);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        // Create a progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Show percentage on the progress bar

        // Add the progress bar to the main panel
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        // Adding the main panel to the frame
        add(mainPanel);

        // Make the window visible
        setVisible(true);
    }

    private void downloadImageAsync(String imageUrl) {
        SwingWorker<byte[], Integer> imageDownloader = new SwingWorker<byte[], Integer>() {
            @Override
            protected byte[] doInBackground() throws Exception {
                try (InputStream in = new URL(imageUrl).openStream()) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int bytesRead;
                    byte[] data = new byte[1024];
                    long totalBytes = 0;
                    long fileSize = getContentLength(imageUrl); // Add a method to get the total file size
            
                    while ((bytesRead = in.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, bytesRead);
                        totalBytes += bytesRead;
            
                        // Calculate progress percentage and set it within the valid range (0 to 100)
                        int progress = (int) ((totalBytes * 100) / fileSize);
                        progress = Math.max(0, Math.min(progress, 100));
            
                        // Publish progress
                        publish(progress);
                    }
            
                    return buffer.toByteArray();
                } catch (IOException e) {
                    // Handle download error
                    e.printStackTrace();
                    return null;
                }
            }

            // Add this method to get the total file size
            private long getContentLength(String imageUrl) {
                try {
                    URL url = new URL(imageUrl);
                    URLConnection connection = url.openConnection();
                    return connection.getContentLengthLong();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            @Override
            protected void process(List<Integer> chunks) {
                // Update the progress bar while downloading
                for (Integer progress : chunks) {
                    // Ensure the progress value is within the valid range (0 to 100)
                    int validProgress = Math.max(0, Math.min(progress, 100));
                    progressBar.setValue(validProgress);
                }
            }


            @Override
            protected void done() {
                try {
                    byte[] imageData = get();
                    if (imageData != null) {
                        // Update the UI with the downloaded image
                        ImageIcon imageIcon = new ImageIcon(imageData);
                        imageLabel.setIcon(imageIcon);
                        imageLabel.setText(null);
                    } else {
                        // Display an error message
                        imageLabel.setIcon(null);
                        imageLabel.setText("Error downloading image.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        // Execute the SwingWorker
        imageDownloader.execute();
    }

    public static void main(String[] args) {
        // Run the GUI form
        SwingUtilities.invokeLater(() -> new Window());
    }
}
