package library_management_system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FeedbackPage extends JFrame {

    private JTextArea feedbackArea;
    private JComboBox<Integer> ratingBox;
    private JButton submitButton;
    private String userId;
    private Connection connection;

    public FeedbackPage(Connection connection,String userId) {
        this.userId = userId;
        this.connection = connection;

        setTitle("Feedback to Library");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Share Your Feedback!!!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        feedbackArea = new JTextArea(8, 40);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);

        ratingBox = new JComboBox<>();
        for (int i = 1; i <= 5; i++) ratingBox.addItem(i);

        submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(e -> submitFeedback());

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Rating (1-5):"));
        bottomPanel.add(ratingBox);
        bottomPanel.add(submitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void submitFeedback() {
        String comment = feedbackArea.getText().trim();
        int rating = (int) ratingBox.getSelectedItem();
        LocalDate currentDate = LocalDate.now();

        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your feedback.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String insertQuery = "INSERT INTO Feedback (UserID, DateOfComment, Comments, Ratings, Response_Date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(currentDate));
            stmt.setString(3, comment);
            stmt.setInt(4, rating);
            stmt.setNull(5, Types.DATE); // Response_Date is initially NULL

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!", "Success", JOptionPane.INFORMATION_MESSAGE);
                feedbackArea.setText("");
                ratingBox.setSelectedIndex(0);
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "You already submitted feedback today.", "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting feedback.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


