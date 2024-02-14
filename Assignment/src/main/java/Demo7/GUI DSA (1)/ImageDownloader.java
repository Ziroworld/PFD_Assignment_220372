import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class ImageDownload extends JFrame {
    private final ExecutorService executorService;
    private final List<Future<?>> downloadTasks;

    private JTextField urlField;
    private JButton downloadButton;
    private JTextArea statusTextArea;

    public ImageDownload() {
        executorService = Executors.newFixedThreadPool(5);
        downloadTasks = new ArrayList<>();

        setTitle("Image Downloader");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        urlField = new JTextField();
        downloadButton = new JButton("Download");
        statusTextArea = new JTextArea();

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText().trim();
                if (!url.isEmpty()) {
                    downloadImage(url);
                }
            }
        });

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Enter URL:"));
        topPanel.add(urlField);
        topPanel.add(downloadButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
    }

    private void downloadImage(String url) {
        Callable<Void> downloadTask = () -> {
            // Simulate downloading by sleeping for a short time
            for (int i = 1; i <= 10; i++) {
                Thread.sleep(500);
                updateStatus("Downloading " + url + " - " + i * 10 + "%");
            }
            updateStatus("Download completed: " + url);
            return null;
        };

        Future<?> task = executorService.submit(downloadTask);
        downloadTasks.add(task);
    }

    private void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> statusTextArea.append(status + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageDownload().setVisible(true));
    }
}
