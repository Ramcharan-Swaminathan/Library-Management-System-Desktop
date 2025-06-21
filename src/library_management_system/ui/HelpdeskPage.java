package library_management_system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class HelpdeskPage extends JFrame {

    private JComboBox<String> issueTypeCombo;
    private JTextArea descriptionArea;
    private JButton submitButton;
    private JButton cancelButton;
    private String userId;
    private Connection connection;

    public HelpdeskPage(Connection connection, String userId) {
        this.userId = userId;
        this.connection = connection;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Helpdesk");
        setSize(700, 550); // Increased window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel with form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 102, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("Library Helpdesk");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger font
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40)); // Increased padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased spacing
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Issue Type - Larger dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel issueLabel = new JLabel("Issue Type:");
        issueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font
        panel.add(issueLabel, gbc);

        gbc.gridx = 1;
        issueTypeCombo = new JComboBox<>();
        issueTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font
        
        // Expanded dropdown options
        issueTypeCombo.addItem("Select an issue type...");
        issueTypeCombo.addItem("Book Availability/Reservation");
        issueTypeCombo.addItem("Membership Registration/Renewal");
        issueTypeCombo.addItem("Account Access Issues");
        issueTypeCombo.addItem("Late Returns/Fines");
        issueTypeCombo.addItem("Damaged/Lost Book Reporting");
        issueTypeCombo.addItem("Technical Problems with Website");
        issueTypeCombo.addItem("Billing/Payment Issues");
        issueTypeCombo.addItem("Research Assistance");
        issueTypeCombo.addItem("Interlibrary Loan Requests");
        issueTypeCombo.addItem("Other General Inquiry");
        
        issueTypeCombo.setPreferredSize(new Dimension(400, 35)); // Wider and taller dropdown
        panel.add(issueTypeCombo, gbc);

        // Description - Larger text area
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descLabel = new JLabel("Detailed Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Larger font
        panel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(12, 50); // More rows and columns
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Larger font
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
         descriptionArea.setMaximumSize(new Dimension(600, 400));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setMaximumSize(new Dimension(600, 400));
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20)); // More spacing
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        submitButton = new JButton("Submit Request");
        submitButton.setPreferredSize(new Dimension(180, 40)); // Larger button
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Larger font
        submitButton.setBackground(new Color(0, 102, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> submitHelpRequest());

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(180, 40)); // Larger button
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Larger font
        cancelButton.addActionListener(e -> dispose());

        panel.add(submitButton);
        panel.add(cancelButton);

        return panel;
    }

    private void submitHelpRequest() {
        String issueType = (String) issueTypeCombo.getSelectedItem();
        String description = descriptionArea.getText().trim();
        LocalDate currentDate = LocalDate.now();

        // Validation
        if ("Select an issue type...".equals(issueType)) {
            JOptionPane.showMessageDialog(this, "Please select an issue type", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please describe your issue in detail", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String insertQuery = "INSERT INTO HelpdeskRequests (UserID, RequestDate, IssueType, Description, Status) " +
                            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(currentDate));
            stmt.setString(3, issueType);
            stmt.setString(4, description);
            stmt.setString(5, "Pending"); // Initial status

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, 
                    "<html><div style='width: 300px;'>" +
                    "<h3>Request Submitted Successfully!</h3>" +
                    "<p>Your help request has been received. Our staff will respond within 2 business days.</p>" +
                    "<p>Reference ID: HD-" + System.currentTimeMillis() + "</p>" +
                    "</div></html>", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error submitting help request: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        issueTypeCombo.setSelectedIndex(0);
        descriptionArea.setText("");
    }
}