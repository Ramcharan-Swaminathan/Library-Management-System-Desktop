import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class BookRatingPage extends JFrame {

    private JTextField bookIdField;
    private JComboBox<Integer> ratingComboBox;
    private JButton submitButton;
    private String userId;
    private Connection connection;

    public BookRatingPage(Connection connection,String userId) {
        this.userId = userId;
        this.connection = connection;

        setTitle("Rate a Book");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Submit Your Book Rating");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("Book ID (ISBN):"));
        bookIdField = new JTextField();
        inputPanel.add(bookIdField);

        inputPanel.add(new JLabel("Rating (1 to 5):"));
        ratingComboBox = new JComboBox<>();
        for (int i = 1; i <= 5; i++) ratingComboBox.addItem(i);
        inputPanel.add(ratingComboBox);

        submitButton = new JButton("Submit Rating");
        submitButton.addActionListener(e -> submitRating());

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void submitRating() {
        String bookId = bookIdField.getText().trim();
        int rating = (int) ratingComboBox.getSelectedItem();
        LocalDate ratingDate = LocalDate.now();

        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String insertSQL = "INSERT INTO Rating (UserID, BookID, RatingValue, RatingDate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, userId);
            stmt.setString(2, bookId);
            stmt.setInt(3, rating);
            stmt.setDate(4, Date.valueOf(ratingDate));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rating submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            bookIdField.setText("");
            ratingComboBox.setSelectedIndex(0);

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Please Provide Vaild Data", "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting rating.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
