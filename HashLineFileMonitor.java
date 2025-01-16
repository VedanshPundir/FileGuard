import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;


public class HashLineFileMonitor {

    private static final String BASELINE_DIR = "C:\\Users\\vedan\\Desktop\\sample dict";  // Update the path accordingly
    private String selectedFolder = "";
    private JTextArea filesChangedArea, filesAddedArea, filesRemovedArea;
    private JLabel messageLabel, folderPathLabel;
    private Map<String, String> baselineHashes = new HashMap<>();

    // Username and password for authentication
    private final String correctUsername = "Vedansh";
    private final String correctPassword = "Vedansh1234";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HashLineFileMonitor().createLoginForm());
    }

     // Create the login form
     public void createLoginForm() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null);  // Center the login window

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add components to login panel
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Position the components
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        // Add login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                if (authenticate(username, new String(password))) {
                    loginFrame.dispose();  // Close the login frame
                    showHashingDetailsPage();  // Show the hashing details page
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Authenticate the username and password
    private boolean authenticate(String username, String password) {
        return username.equals(correctUsername) && password.equals(correctPassword);
    }

    // Method to show the hashing details page after login
    private void showHashingDetailsPage() {
        JFrame hashingDetailsFrame = new JFrame("Hashing Information");
        hashingDetailsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hashingDetailsFrame.setSize(800, 600);
        hashingDetailsFrame.setLocationRelativeTo(null);  // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a text area to display detailed information about hashing
        JTextArea hashingDetailsArea = new JTextArea();
        hashingDetailsArea.setEditable(false);
        hashingDetailsArea.setFont(new Font("Arial", Font.BOLD, 20));

        // Detailed information about hashing
        String hashingInfo = "Hashing is the process of converting an input (or 'message') into a fixed-size string of bytes.\n" +
                "The output, typically a digest, is a representation of the input data, such that even a small change in\n" +
                "the input will produce a significantly different output.\n\n" +
                "Common Hashing Algorithms:\n" +
                "1. MD5: Message Digest Algorithm 5, produces a 128-bit hash value.\n" +
                "2. SHA-1: Secure Hash Algorithm 1, produces a 160-bit hash value.\n" +
                "3. SHA-256: A more secure version of SHA, produces a 256-bit hash value.\n" +
                "4. SHA-512: A more secure version of SHA, produces a 512-bit hash value.\n\n" +
                "Applications of Hashing:\n" +
                "1. Data Integrity: Hashing helps to verify data integrity by comparing the hashes of the original data\n" +
                "   and received data.\n" +
                "2. Password Storage: Hashing is used to store passwords securely.\n" +
                "3. Digital Signatures: Hashing is used in the creation of digital signatures, which authenticate data.\n\n" +
                "In the context of file integrity, hashing is used to generate a unique fingerprint of each file.\n" +
                "Any modification to the file will result in a different hash, which can be used to detect changes.";
        
        // Set the text area content
        hashingDetailsArea.setText(hashingInfo);

        // Add the text area inside a scroll pane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(hashingDetailsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a button to navigate back to the main screen
        JButton backButton = new JButton("Back to Main Screen");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hashingDetailsFrame.dispose();  // Close the hashing details frame
                createAndShowGUI();  // Show the main FileGuard window
            }
        });

        panel.add(backButton, BorderLayout.SOUTH);

        // Add the panel to the frame
        hashingDetailsFrame.add(panel);
        hashingDetailsFrame.setVisible(true);
    }

    // Main method to create the FileGuard window
    public void createAndShowGUI() {
        JFrame frame = new JFrame("HashLine - File Integrity Monitor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("giphy.gif");
                Image img = backgroundImage.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());
        frame.setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel headingLabel = new JLabel("FileGuard:File Integerity Monitoring Tool");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 36));
        headingLabel.setForeground(new Color(255, 165, 0));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(headingLabel, gbc);

        gbc.gridy = 1;

        
        JButton browseButton = new JButton("Select Directory");
        browseButton.setFont(new Font("Arial", Font.PLAIN, 14));
        browseButton.setBackground(new Color(34, 139, 34));
        browseButton.setForeground(Color.WHITE);
        browseButton.setFocusPainted(false);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });
gbc.gridx = 1;
panel.add(browseButton, gbc);
gbc.gridx = 1;
gbc.weightx = 0;
gbc.weighty = 0;
gbc.fill = GridBagConstraints.NONE; // Prevent it from expanding
gbc.insets = new Insets(8, 8, 8, 8); // Optional padding around the button
panel.add(browseButton, gbc);



        folderPathLabel = new JLabel("(Selected Folder path will appear here)");
        folderPathLabel.setFont(new Font("Verdana", Font.ITALIC, 14));
        folderPathLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(folderPathLabel, gbc);

        JButton updateBaselineButton = new JButton("Hashfile update");
        updateBaselineButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateBaselineButton.setBackground(new Color(50, 205, 50));
        updateBaselineButton.setForeground(Color.WHITE);
        updateBaselineButton.setFocusPainted(false);

        updateBaselineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectedFolder.isEmpty()) {
                    updateBaseline();
                } else {
                    showMessage("Error: Folder not selected");
                }
            }
        });
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(updateBaselineButton, gbc);

        JButton checkIntegrityButton = new JButton("Check Integrity");
        checkIntegrityButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkIntegrityButton.setBackground(new Color(255, 99, 71));
        checkIntegrityButton.setForeground(Color.WHITE);
        checkIntegrityButton.setFocusPainted(false);

        checkIntegrityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectedFolder.isEmpty()) {
                    checkIntegrity();
                } else {
                    showMessage("Error: Folder not selected");
                }
            }
        });
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(checkIntegrityButton, gbc);

        messageLabel = new JLabel("Message: ");
        messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        messageLabel.setForeground(Color.WHITE);
        gbc.gridy = 5;
        panel.add(messageLabel, gbc);

        JLabel fcLabel = new JLabel("Files Changed:");
        fcLabel.setFont(new Font("Arial", Font.BOLD, 22));
        fcLabel.setForeground(Color.WHITE);
        gbc.gridy = 6;
        panel.add(fcLabel, gbc);

        filesChangedArea = new JTextArea(5, 50);
        filesChangedArea.setEditable(false);
        filesChangedArea.setFont(new Font("Courier New", Font.BOLD, 13));
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(filesChangedArea), gbc);

        JLabel faLabel = new JLabel("Files Added:");
        faLabel.setFont(new Font("Arial", Font.BOLD, 22));
        faLabel.setForeground(Color.WHITE);
        gbc.gridy = 8;
        panel.add(faLabel, gbc);

        filesAddedArea = new JTextArea(5, 50);
        filesAddedArea.setEditable(false);
        filesAddedArea.setFont(new Font("Courier New", Font.BOLD, 13));
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(filesAddedArea), gbc);

        JLabel frLabel = new JLabel("Files Removed:");
        frLabel.setFont(new Font("Arial", Font.BOLD, 22));
        frLabel.setForeground(Color.WHITE);
        gbc.gridy = 10;
        panel.add(frLabel, gbc);

        filesRemovedArea = new JTextArea(5, 50);
        filesRemovedArea.setEditable(false);
        filesRemovedArea.setFont(new Font("Courier New", Font.BOLD, 13));
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(filesRemovedArea), gbc);

        frame.setVisible(true);

        // Add the 'Show Files in Directory' button at the bottom
        JButton showFilesButton = new JButton("Show Files in Directory");
         showFilesButton.setFont(new Font("Arial", Font.BOLD, 14));
        showFilesButton.setBackground(new Color(30, 144, 255));
        showFilesButton.setForeground(Color.WHITE);
        showFilesButton.setFocusPainted(false);

        showFilesButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
        if (!selectedFolder.isEmpty()) {
            showFilesPage();  // Show the new page with the file table
        } else {
            showMessage("Error: Folder not selected");
        }
      }
     });
       gbc.gridy = 12;  // Position the button at the bottom
     gbc.gridwidth = 2;
      panel.add(showFilesButton, gbc);

    }

    // Method to handle file browsing
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileChooser.getSelectedFile();
            selectedFolder = selectedDir.getAbsolutePath();
            folderPathLabel.setText("Selected Folder: " + selectedFolder);
        }
    }

    // Method to update baseline hashes
    private void updateBaseline() {
        baselineHashes.clear();
        try {
            Files.walk(Paths.get(selectedFolder))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String hash = generateFileHash(path.toString());
                            baselineHashes.put(path.toString(), hash);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            showMessage("Baseline updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check file integrity
    private void checkIntegrity() {
        Map<String, String> currentHashes = new HashMap<>();
        try {
            Files.walk(Paths.get(selectedFolder))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String hash = generateFileHash(path.toString());
                            currentHashes.put(path.toString(), hash);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder changedFiles = new StringBuilder();
        StringBuilder addedFiles = new StringBuilder();
        StringBuilder removedFiles = new StringBuilder();

        // Compare baseline with current files
        for (String filePath : baselineHashes.keySet()) {
            if (currentHashes.containsKey(filePath)) {
                if (!baselineHashes.get(filePath).equals(currentHashes.get(filePath))) {
                    changedFiles.append(filePath).append("\n");
                }
            } else {
                removedFiles.append(filePath).append("\n");
            }
        }

        for (String filePath : currentHashes.keySet()) {
            if (!baselineHashes.containsKey(filePath)) {
                addedFiles.append(filePath).append("\n");
            }
        }

        filesChangedArea.setText(changedFiles.toString());
        filesAddedArea.setText(addedFiles.toString());
        filesRemovedArea.setText(removedFiles.toString());

        showMessage("Integrity check completed.");
    }

    // Method to generate a file hash
    private String generateFileHash(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream is = Files.newInputStream(Paths.get(filePath));
             DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[1024];
            while (dis.read(buffer) != -1) {
                // Reading file in chunks and hashing
            }
        }

        byte[] hashBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Method to display messages
    private void showMessage(String message) {
        messageLabel.setText("Message: " + message);
    }
    public void showFilesPage() {
    JFrame frame = new JFrame("Files List with Last Modified Time");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 600);

    // Table to display files and their last modified times
    String[] columnNames = {"File Name", "Last Modified Time"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable filesTable = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(filesTable);
    frame.add(scrollPane, BorderLayout.CENTER);

    // Fetch files and their last modified time
    try {
        Files.walk(Paths.get(selectedFolder))  // Walk through the selected directory
            .filter(Files::isRegularFile)  // Filter to get only regular files
            .forEach(path -> {
                String fileName = path.getFileName().toString();
                try {
                    // Get last modified time
                    FileTime lastModifiedTime = Files.getLastModifiedTime(path);
                    String formattedDate = lastModifiedTime.toString();  // Format the date as needed
                    tableModel.addRow(new Object[]{fileName, formattedDate});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Adding a close button
    JButton closeButton = new JButton("Close");
    closeButton.setFont(new Font("Arial", Font.BOLD, 14));
    closeButton.setBackground(new Color(255, 69, 0));
    closeButton.setForeground(Color.WHITE);
    closeButton.setFocusPainted(false);
    closeButton.addActionListener(e -> frame.dispose());
    frame.add(closeButton, BorderLayout.SOUTH);

    // Display the frame
    frame.setLocationRelativeTo(null);  // Center the window
    frame.setVisible(true);
}
    

    
}
