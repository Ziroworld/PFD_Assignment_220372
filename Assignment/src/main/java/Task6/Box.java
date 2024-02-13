package Task6;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ExtendedSwingFrame extends JFrame {

    private JTextField textField; // Text field for entering image URLs
    private JButton addUrlButton; // Button to add image URLs to the list
    private JButton downloadButton; // Button to start downloading images
    private JButton clearUrlButton; // Button to clear the list of image URLs
    private JPanel progressBarPanel; // Panel to display progress bars for each image
    private JButton pauseButton;
    private JButton cancelButton;

    private ExecutorService executorService; // Thread pool for managing concurrent downloads
    private List<String> urlList; // List to store image URLs
    private List<JProgressBar> progressBars; // List to store progress bars for each image
    private List<SwingWorker<Void, Integer>> workers; // List to store SwingWorkers for each download

    // Constructor for the ExtendedSwingFrame class
    public ExtendedSwingFrame() {
        setTitle("Extended Swing Frame");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(); // Main panel containing UI components

        textField = new JTextField(25); // Text field to enter image URLs
        addUrlButton = new JButton("Add URL"); // Button to add image URLs
        downloadButton = new JButton("Download Images"); // Button to start downloading images
        clearUrlButton = new JButton("Clear URLs"); // Button to clear the list of image URLs
        pauseButton = new JButton("Pause"); 
        cancelButton = new JButton("Cancle");
        progressBarPanel = new JPanel(new GridLayout(0, 1)); // Panel to display progress bars

        urlList = new ArrayList<>(); // Initialize the list to store image URLs
        progressBars = new ArrayList<>(); // Initialize the list to store progress bars
        workers = new ArrayList<>(); // Initialize the list to store SwingWorkers

        // ActionListener for the "Add URL" button
        addUrlButton.addActionListener(e -> {
            String imageUrl = textField.getText(); // Get the URL from the text field
            if (!imageUrl.isEmpty()) {
                urlList.add(imageUrl); // Add the URL to the list
                textField.setText(""); // Clear the text field
                addProgressBar(urlList.size()); // Add a progress bar for the new URL
            }
        });

        // ActionListener for the "Download Images" button
        downloadButton.addActionListener(e -> {
            if (!urlList.isEmpty()) {
                for (int i = 0; i < urlList.size(); i++) {
                    downloadImage(urlList.get(i), progressBars.get(i), i + 1); // Download each image concurrently
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No URLs to download.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ActionListener for the "Clear URLs" button
        clearUrlButton.addActionListener(e -> {
            urlList.clear(); // Clear the list of image URLs
            progressBars.clear(); // Clear the list of progress bars
            progressBarPanel.removeAll(); // Remove progress bars from the panel
            progressBarPanel.revalidate(); // Revalidate the panel to reflect changes
            progressBarPanel.repaint(); // Repaint the panel
        });

        // ActionListener for the "Pause" button
        pauseButton.addActionListener(e -> {
            int index = progressBars.indexOf(((Component) e.getSource()).getParent().getComponent(1));
            pauseDownload(index);
        });

        // ActionListener for the "Cancel" button
        cancelButton.addActionListener(e -> {
            int index = progressBars.indexOf(((Component) e.getSource()).getParent().getComponent(1));
            cancelDownload(index);
        });

        // Add UI components to the main panel
        panel.add(new JLabel("Image URL:"));
        panel.add(textField);
        panel.add(addUrlButton);
        panel.add(downloadButton);
        panel.add(clearUrlButton);
        panel.add(progressBarPanel);
        panel.add(pauseButton);
        panel.add(cancelButton);

        getContentPane().add(panel); // Add the main panel to the frame

        setSize(400, 300); // Set the size of the frame
        setVisible(true); // Make the frame visible

        // Use a fixed thread pool with a maximum of 10 threads
        executorService = Executors.newFixedThreadPool(10);
    }

    // Method to add a progress bar for a new image
    private void addProgressBar(int imageNumber) {
        JLabel label = new JLabel("Image " + imageNumber + ": "); // Label to identify the image
        JProgressBar progressBar = new JProgressBar(0, 100); // Progress bar for the image
        progressBar.setStringPainted(true); // Show the progress as a string

        JPanel progressBarPanel = new JPanel(new BorderLayout()); // Panel for the progress bar
        progressBarPanel.add(label, BorderLayout.WEST); // Add label to the left
        progressBarPanel.add(progressBar, BorderLayout.CENTER); // Add progress bar to the center

        this.progressBarPanel.add(progressBarPanel); // Add the progress bar panel to the main panel

        progressBars.add(progressBar); // Add the progress bar to the list

        revalidate(); // Revalidate the frame to reflect changes
        repaint(); // Repaint the frame
    }

    // Method to download an image
    private void downloadImage(String imageUrl, JProgressBar progressBar, int imageNumber) {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    if (!isValidUrl(imageUrl)) {
                        throw new MalformedURLException("Invalid URL: " + imageUrl);
                    }

                    URL url = new URL(imageUrl);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }

                    String contentType = connection.getContentType();
                    connection.disconnect();

                    if (contentType == null || !contentType.startsWith("image")) {
                        throw new IOException("URL does not point to an image: " + imageUrl);
                    }

                    String fileName = url.getFile();
                    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                    fileName = fileName.split("\\?")[0];

                    String newFileName = getDynamicFileName(System.getProperty("user.home") + "/Desktop/", fileName);
                    Path outputPath = Paths.get(System.getProperty("user.home") + "/Desktop/", newFileName);

                    Files.createDirectories(outputPath.getParent());

                    int contentLength = connection.getContentLength();
                    int totalBytesRead = 0;

                    try (InputStream in = url.openStream();
                         OutputStream out = Files.newOutputStream(outputPath)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;

                            int progress = (int) ((double) totalBytesRead / contentLength * 100);
                            publish(progress);
                        }
                    }

                    publish(100);
                    // Wait until the file is saved to the directory before completing
                    Files.move(Paths.get(System.getProperty("user.home") + "/Desktop/", fileName),
                            outputPath, StandardCopyOption.REPLACE_EXISTING);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    showError("Error downloading image: Invalid URL", imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    showError("Error downloading image: " + e.getMessage(), imageUrl);
                }

                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (int progress : chunks) {
                    progressBar.setValue(progress);
                }
            }

            @Override
            protected void done() {
                // Check if all workers are done
                boolean allDone = true;
                for (SwingWorker<Void, Integer> worker : workers) {
                    if (!worker.isDone()) {
                        allDone = false;
                        break;
                    }
                }
                if (allDone) {
                    // Clear URLs when all downloads are completed
                    urlList.clear();
                    JOptionPane.showMessageDialog(ExtendedSwingFrame.this,
                            "All images downloaded successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };

        executorService.execute(() -> {
            worker.execute();
            workers.add(worker);
        });
    }

    // Method to check if a URL is valid
    private boolean isValidUrl(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to generate a dynamic file name to avoid conflicts
    private String getDynamicFileName(String directory, String fileName) {
        String baseName = fileName.substring(0, Math.min(fileName.lastIndexOf('.'), 255));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        Path filePath = Paths.get(directory, fileName);
        int count = 1;

        while (Files.exists(filePath)) {
            String newFileName = MessageFormat.format("{0}_{1}{2}", baseName, count++, extension);
            filePath = Paths.get(directory, newFileName);
        }

        return filePath.getFileName().toString();
    }

    // Method to show an error message in a dialog
    private void showError(String message, String imageUrl) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    message + "\nURL: " + imageUrl,
                    "Error", JOptionPane.ERROR_MESSAGE);
        });
    }
    
    // Method to pause a download
    private void pauseDownload(int index) {
        if (index >= 0 && index < workers.size()) {
            workers.get(index).cancel(true); // Cancel the corresponding SwingWorker
        }
    }

    // Method to cancel a download
    private void cancelDownload(int index) {
        if (index >= 0 && index < workers.size()) {
            workers.get(index).cancel(true); // Cancel the corresponding SwingWorker
            progressBars.get(index).setValue(0); // Reset progress bar
            progressBars.get(index).setString("Canceled"); // Update progress bar label
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExtendedSwingFrame());
    }

    ///Note: for some reason some image url does not download images no errors, no nothing nada !
}
