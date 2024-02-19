/*
    Task 6
    Implement a Multithreaded Asynchronous Image Downloader in Java Swing
    Task Description:
    You are tasked with designing and implementing a multithreaded asynchronous image downloader in a Java Swing
    application. The application should allow users to enter a URL and download images from that URL in the
    background, while keeping the UI responsive. The image downloader should utilize multithreading and provide a
    smooth user experience when downloading images.
    Requirements:
    Design and implement a GUI application that allows users to enter a URL and download images.
    Implement a multithreaded asynchronous framework to handle the image downloading process in the background.
    Provide a user interface that displays the progress of each image download, including the current download status
    and completion percentage.
    Utilize a thread pool to manage the concurrent downloading of multiple images, ensuring efficient use of system
    resources.
    Implement a mechanism to handle downloading errors or exceptions, displaying appropriate error messages to the
    user.
    Use thread synchronization mechanisms, such as locks or semaphores, to ensure data integrity and avoid conflicts
    during image downloading.
    Provide options for the user to pause, resume, or cancel image downloads.
    Test the application with various URLs containing multiple images to verify its functionality and responsiveness.
    Include proper error handling and reporting for cases such as invalid URLs or network failures
 */


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
import java.util.concurrent.atomic.AtomicBoolean;

class Box extends JFrame {

    private JTextField textField;
    private JButton addUrlButton;
    private JButton downloadButton;
    private JButton clearUrlButton;
    private JButton cancelButton;
    private JPanel progressBarPanel;

    private ExecutorService executorService;
    private List<String> urlList;
    private List<JProgressBar> progressBars;
    private List<DownloadWorker> workers;

    private AtomicBoolean isCanceled;

    public Box() {
        setTitle("Extended Swing Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        textField = new JTextField(20);
        addUrlButton = new JButton("Add URL");
        downloadButton = new JButton("Download Images");
        clearUrlButton = new JButton("Clear URLs");
        cancelButton = new JButton("Cancel All Downloads");
        progressBarPanel = new JPanel(new GridLayout(0, 1));

        urlList = new ArrayList<>();
        progressBars = new ArrayList<>();
        workers = new ArrayList<>();

        isCanceled = new AtomicBoolean(false);

        addUrlButton.addActionListener(e -> {
            String imageUrl = textField.getText();
            if (!imageUrl.isEmpty()) {
                urlList.add(imageUrl);
                textField.setText("");
                addProgressBar(urlList.size());
            }
        });

        downloadButton.addActionListener(e -> {
            if (!urlList.isEmpty()) {
                for (int i = 0; i < urlList.size(); i++) {
                    downloadImage(urlList.get(i), progressBars.get(i), i + 1);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No URLs to download.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        clearUrlButton.addActionListener(e -> {
            urlList.clear();
            progressBars.clear();
            progressBarPanel.removeAll();
            progressBarPanel.revalidate();
            progressBarPanel.repaint();
        });

        cancelButton.addActionListener(e -> {
            isCanceled.set(true);
            cancelAllDownloads();
        });

        panel.add(new JLabel("Image URL:"));
        panel.add(textField);
        panel.add(addUrlButton);
        panel.add(downloadButton);
        panel.add(clearUrlButton);
        panel.add(cancelButton);
        panel.add(progressBarPanel);

        getContentPane().add(panel);

        setSize(400, 300);
        setVisible(true);

        executorService = Executors.newFixedThreadPool(10);
    }

    private void addProgressBar(int imageNumber) {
        JLabel label = new JLabel("Image " + imageNumber + ": ");
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            int workerIndex = findWorkerIndexByProgressBar(progressBar);
            if (workerIndex != -1) {
                workers.get(workerIndex).pauseDownload();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pauseButton);

        JPanel progressBarPanel = new JPanel(new BorderLayout());
        progressBarPanel.add(label, BorderLayout.WEST);
        progressBarPanel.add(progressBar, BorderLayout.CENTER);
        progressBarPanel.add(buttonPanel, BorderLayout.EAST);

        this.progressBarPanel.add(progressBarPanel);
        progressBars.add(progressBar);

        revalidate();
        repaint();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        downloadImage(urlList.get(imageNumber - 1), progressBar, imageNumber);
    }

    private void cancelAllDownloads() {
        if (workers != null && !workers.isEmpty()) {
            for (DownloadWorker worker : workers) {
                worker.cancel(true);
            }
        }
    }

    private int findWorkerIndexByProgressBar(JProgressBar progressBar) {
        for (int i = 0; i < progressBars.size(); i++) {
            if (progressBars.get(i) == progressBar) {
                return i;
            }
        }
        return -1;
    }

    private void downloadImage(String imageUrl, JProgressBar progressBar, int imageNumber) {
        DownloadWorker worker = new DownloadWorker(imageUrl, progressBar, imageNumber);
        executorService.execute(worker);
        workers.add(worker);
    }

    private class DownloadWorker extends SwingWorker<Void, Integer> {

        private final String imageUrl;
        private final JProgressBar progressBar;
        private final int imageNumber;
        private final AtomicBoolean isPaused;

        public DownloadWorker(String imageUrl, JProgressBar progressBar, int imageNumber) {
            this.imageUrl = imageUrl;
            this.progressBar = progressBar;
            this.imageNumber = imageNumber;
            this.isPaused = new AtomicBoolean(false);
        }

        public void pauseDownload() {
            isPaused.set(!isPaused.get());
            if (isPaused.get()) {
                JOptionPane.showMessageDialog(Box.this,
                        "Download paused for Image " + imageNumber,
                        "Paused", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        protected Void doInBackground() {
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

                // Move the disconnection after obtaining the content length
                int contentLength = connection.getContentLength();
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

                int totalBytesRead = 0;

                try (InputStream in = url.openStream();
                        OutputStream out = Files.newOutputStream(outputPath)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        if (isPaused.get()) {
                            while (isPaused.get()) {
                                if (isCancelled()) {
                                    return null;
                                }
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    return null;
                                }
                            }
                        }

                        out.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        int progress = (int) ((double) totalBytesRead / contentLength * 100);
                        publish(progress);

                        // Introduce a delay to slow down the download progress
                        try {
                            Thread.sleep(100); // Adjust the sleep duration as needed
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    }
                }

                publish(100);
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
            boolean allDone = true;
            for (DownloadWorker worker : workers) {
                if (!worker.isDone() || worker.isCancelled()) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                urlList.clear();
                JOptionPane.showMessageDialog(Box.this,
                        "All images downloaded successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean isValidUrl(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    private void showError(String message, String imageUrl) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    message + "\nURL: " + imageUrl,
                    "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Box());
    }
}