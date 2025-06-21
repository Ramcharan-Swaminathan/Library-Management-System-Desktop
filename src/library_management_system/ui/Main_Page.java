package library_management_system.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;

public class Main_Page extends JFrame {
    private Connection conn;
    private String Current_userId;
    private boolean isAdminLogin = false;

    public Main_Page(Connection connection) {
        this.conn = connection;
        initUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header Panel with blue background
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 60, 114));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with white background
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Library Management System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.BLACK);
        contentPanel.add(welcomeLabel, gbc);

        // Options panel
        gbc.gridy++;
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 50, 0));
        optionsPanel.setBackground(Color.WHITE);
        
        // Admin option
        Image adminImage = loadImage("D:/Java_Swings/Library_Management_System/src/library_management_system/asserts/admin_icon.png");
        JPanel adminPanel = createOptionPanel(adminImage, "ADMIN");
        adminPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showLoginForm(true);
            }
            public void mouseEntered(MouseEvent e) {
                adminPanel.setBorder(BorderFactory.createLineBorder(new Color(30, 60, 114), 3));
            }
            public void mouseExited(MouseEvent e) {
                adminPanel.setBorder(null);
            }
        });

        // User option
        Image userImage = loadImage("D:/Java_Swings/Library_Management_System/src/library_management_system/asserts/user_icon.png");
        JPanel userPanel = createOptionPanel(userImage, "USER");
        userPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showLoginForm(false);
            }
            public void mouseEntered(MouseEvent e) {
                userPanel.setBorder(BorderFactory.createLineBorder(new Color(30, 60, 114), 3));
            }
            public void mouseExited(MouseEvent e) {
                userPanel.setBorder(null);
            }
        });

        optionsPanel.add(adminPanel);
        optionsPanel.add(userPanel);
        contentPanel.add(optionsPanel, gbc);

        // Add content to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer with blue background
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(30, 60, 114));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel footerLabel = new JLabel("© 2025 Library Management System");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void showLoginForm(boolean isAdmin) {
        this.isAdminLogin = isAdmin;
        
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel(isAdmin ? "Admin Login" : "User Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        JTextField useridField = new JTextField(15);
        panel.add(useridField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");
        
        loginButton.addActionListener(e -> {
            String userId = useridField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (userId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginDialog, 
                    "Please enter both User ID and Password", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (isAdminLogin) {
                    // Admin login
                    String sql = "SELECT * FROM Admin WHERE AdminID = ? AND Password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userId);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String userName = rs.getString("AdminName");
                        Current_userId = rs.getString("AdminID");

                        JOptionPane.showMessageDialog(loginDialog, 
                            "Welcome Admin, " + userName + "!", 
                            "Admin Login Successful", JOptionPane.INFORMATION_MESSAGE);
                          
                        Admin_Option_Page adminOptionPage = new Admin_Option_Page(Current_userId);
adminOptionPage.setAlwaysOnTop(true);
adminOptionPage.setLocationRelativeTo(null); // center on screen
adminOptionPage.toFront(); // bring to front
adminOptionPage.setVisible(true);

                        loginDialog.dispose();
                        //dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginDialog, 
                            "Invalid Admin credentials", 
                            "Admin Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // User login
                    String sql = "SELECT * FROM Users WHERE UserID = ? AND Password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userId);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String userName = rs.getString("UserName");
                        Current_userId = rs.getString("UserID");

                        JOptionPane.showMessageDialog(loginDialog, 
                            "Welcome, " + userName + "!", 
                            "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                                               User_Option_Page user_option_page = new User_Option_Page(Current_userId);
user_option_page.setAlwaysOnTop(true);
user_option_page.setLocationRelativeTo(null); // center on screen
user_option_page.toFront(); // bring to front
user_option_page.setVisible(true);
                        loginDialog.dispose();
                        //dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginDialog, 
                            "Invalid User ID or Password", 
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginDialog, 
                    "Database error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        backButton.addActionListener(e -> loginDialog.dispose());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        loginDialog.add(panel);
        loginDialog.setVisible(true);
    }

    private Image loadImage(String absolutePath) {
        try {
            File file = new File(absolutePath);
            if (file.exists()) {
                return ImageIO.read(file);
            } else {
                System.out.println("Image not found at: " + absolutePath);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    private JPanel createOptionPanel(Image image, String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 300));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (image != null) {
            Image scaledImg = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(imageLabel, BorderLayout.CENTER);
        } else {
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            textLabel.setForeground(Color.BLACK);
            panel.add(textLabel, BorderLayout.CENTER);
        }

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        textLabel.setForeground(new Color(30, 60, 114));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);

        return panel;
    }
}