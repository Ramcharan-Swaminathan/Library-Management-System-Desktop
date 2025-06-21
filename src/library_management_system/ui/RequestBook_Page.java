package library_management_system.ui;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RequestBook_Page extends JFrame {
    private Connection conn;
    private String currentUserId;
    private JComboBox<String> requestTypeComboBox;
    private JTextField bookIdField, titleField, authorField;

    public RequestBook_Page(Connection connection, String userId) {
        this.conn = connection;
        this.currentUserId = userId;
        initComponents();
        setTitle("Book Request Page");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

       
        JLabel headerLabel = new JLabel("Book Request Form", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

    
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

      
        formPanel.add(new JLabel("Book ID:"));
        bookIdField = new JTextField();
        formPanel.add(bookIdField);

       
        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);


        formPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        formPanel.add(authorField);

        
        formPanel.add(new JLabel("Request Type:"));
        String[] requestTypes = {"Additional Copies", "New Edition", "Replacement", "Other"};
        requestTypeComboBox = new JComboBox<>(requestTypes);
        formPanel.add(requestTypeComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton submitButton = new JButton("Submit Request");
        submitButton.addActionListener(this::submitRequest);
        buttonPanel.add(submitButton);

        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispose());
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void submitRequest(ActionEvent evt) {
        String bookId = bookIdField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String requestType = (String) requestTypeComboBox.getSelectedItem();

        if (bookId.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all book details", 
                                        "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
           
            conn.setAutoCommit(false);

           
            String insertRequestSQL = "INSERT INTO RequestBook (BookID, RequestedDate, RequestType, RequestStatus) " +
                                    "VALUES (?, ?, ?, 'Pending')";
            PreparedStatement pstmt = conn.prepareStatement(insertRequestSQL);
            pstmt.setString(1, bookId);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, requestType);
            pstmt.executeUpdate();

          
            String insertDetailsSQL = "INSERT INTO RequestBookWithDetails (UserID, RequestDate, BookID, Title, Author) " +
                                     "VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertDetailsSQL);
            pstmt.setString(1, currentUserId);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, bookId);
            pstmt.setString(4, title);
            pstmt.setString(5, author);
            pstmt.executeUpdate();

            
            conn.commit();

            JOptionPane.showMessageDialog(this, "Book request submitted successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error submitting request: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void clearForm() {
        bookIdField.setText("");
        titleField.setText("");
        authorField.setText("");
        requestTypeComboBox.setSelectedIndex(0);
    }


}