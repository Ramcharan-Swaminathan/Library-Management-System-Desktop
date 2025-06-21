package library_management_system.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import library_management_system.ui.User_Action_Page;

public class Login_Page extends javax.swing.JFrame {
    private Connection conn;
    private String Current_userId;
    private boolean isAdminLogin = false;

    public Login_Page(Connection connection) {
        this.conn = connection;
        initComponents();
        setTitle("Login Page");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    private void initComponents() {
       
        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        
     
        JLabel jLabel1 = new JLabel("Library Management System");
        JLabel jLabel2 = new JLabel("Welcome Back !");
        
        jLabel1.setFont(new java.awt.Font("Century", java.awt.Font.BOLD, 24));
        jLabel2.setFont(new java.awt.Font("Leelawadee", java.awt.Font.PLAIN, 14));

     
        JButton userTypeButton = new JButton("User Login");
        JButton adminTypeButton = new JButton("Admin Login");
        
        userTypeButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        adminTypeButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

       
        JLabel userid_label = new JLabel("Enter UserID");
        JLabel password_label = new JLabel("Enter Password");
        JTextField userid_textField = new JTextField();
        JPasswordField password_field = new JPasswordField();
        JButton loginButton = new JButton("LOGIN");
        JButton backButton = new JButton("Back");
        
        userid_label.setFont(new java.awt.Font("Century", java.awt.Font.PLAIN, 14));
        password_label.setFont(new java.awt.Font("Century", java.awt.Font.PLAIN, 14));
        loginButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        backButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));

       
        showSelectionPanel(mainPanel, layout, jLabel1, jLabel2, userTypeButton, adminTypeButton);
        
       
        userTypeButton.addActionListener(e -> {
            isAdminLogin = false;
            showLoginPanel(mainPanel, layout, jLabel1, jLabel2, 
                          userid_label, password_label, 
                          userid_textField, password_field, 
                          loginButton, backButton);
        });
        
        adminTypeButton.addActionListener(e -> {
            isAdminLogin = true;
            showLoginPanel(mainPanel, layout, jLabel1, jLabel2, 
                          userid_label, password_label, 
                          userid_textField, password_field, 
                          loginButton, backButton);
        });
        

        backButton.addActionListener(e -> {
            showSelectionPanel(mainPanel, layout, jLabel1, jLabel2, userTypeButton, adminTypeButton);
        });
        
      
        loginButton.addActionListener(evt -> {
            String userId = userid_textField.getText().trim();
            String password = new String(password_field.getPassword()).trim();

            if (userId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both User ID and Password", "Login Error", JOptionPane.ERROR_MESSAGE);
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

                        JOptionPane.showMessageDialog(this, "Welcome Admin, " + userName + "!", "Admin Login Successful", JOptionPane.INFORMATION_MESSAGE);
                          
                        new Admin_Option_Page(Current_userId).setVisible(true);
                        
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Admin credentials", "Admin Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                  
                    String sql = "SELECT * FROM Users WHERE UserID = ? AND Password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, userId);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String userName = rs.getString("UserName");
                        Current_userId = rs.getString("UserID");
                        String membershipType = rs.getString("MembershipType");

                        JOptionPane.showMessageDialog(this, "Welcome, " + userName + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                        new User_Option_Page(Current_userId).setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid User ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        getContentPane().add(mainPanel);
        pack();
    }

    private void showSelectionPanel(JPanel mainPanel, GroupLayout layout, 
                                  JLabel titleLabel, JLabel welcomeLabel,
                                  JButton userButton, JButton adminButton) {
        mainPanel.removeAll();
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(titleLabel)
                .addComponent(welcomeLabel)
                .addGap(50)
                .addGroup(layout.createSequentialGroup()
                    .addGap(50)
                    .addComponent(userButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                    .addGap(20)
                    .addComponent(adminButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                    .addGap(50))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(30)
                .addComponent(titleLabel)
                .addGap(10)
                .addComponent(welcomeLabel)
                .addGap(50)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(userButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                    .addComponent(adminButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                .addGap(50)
        );
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showLoginPanel(JPanel mainPanel, GroupLayout layout, 
                              JLabel titleLabel, JLabel welcomeLabel,
                              JLabel useridLabel, JLabel passwordLabel,
                              JTextField useridField, JPasswordField passwordField,
                              JButton loginButton, JButton backButton) {
        mainPanel.removeAll();
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(backButton)
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(titleLabel)
                        .addComponent(welcomeLabel)
                        .addGap(50)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(useridLabel)
                                .addComponent(passwordLabel))
                            .addGap(18)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(useridField, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                .addComponent(passwordField)))
                        .addGap(30)
                        .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(50, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addGap(10)
                .addComponent(titleLabel)
                .addGap(10)
                .addComponent(welcomeLabel)
                .addGap(30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(useridLabel)
                    .addComponent(useridField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(15)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(30)
                .addComponent(loginButton)
                .addGap(30)
        );
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}