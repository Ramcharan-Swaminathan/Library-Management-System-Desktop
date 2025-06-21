package library_management_system.ui;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;

public class AdminFeedbackView extends JFrame {
    private Connection conn;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;

    public AdminFeedbackView(Connection connection) {
        this.conn = connection;
        initComponents();
        setTitle("Admin - View Feedback");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Table model setup
        String[] columnNames = {"User ID", "Date of Comment", "Comments", "Rating", "Response Date", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the Actions column is editable
            }
        };
        
        feedbackTable = new JTable(tableModel);
        feedbackTable.setRowHeight(30);
        feedbackTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Wider for comments
        
        // Add button renderer and editor for the Actions column
        feedbackTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        feedbackTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), conn, tableModel));
        
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        
        // Add components to main panel
        mainPanel.add(new JLabel("Feedback Management", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Load data button
        JButton refreshButton = new JButton("Refresh Feedback");
        refreshButton.addActionListener(e -> loadFeedbackData());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
       
        loadFeedbackData();
    }

    private void loadFeedbackData() {
        try {
              tableModel.setRowCount(0);
            
            String sql = "SELECT * FROM Feedback ORDER BY DateOfComment DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String userId = rs.getString("UserID");
                Date dateOfComment = rs.getDate("DateOfComment");
                String comments = rs.getString("Comments");
                int rating = rs.getInt("Ratings");
                Date responseDate = rs.getDate("Response_Date");
                
                Object[] rowData = {
                    userId,
                    dateOfComment,
                    comments,
                    rating,
                    responseDate,
                    responseDate == null ? "Set Response Date" : "Update Response Date"
                };
                
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading feedback: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Button renderer for the table
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Button editor for the table
    class ButtonEditor extends DefaultCellEditor {
        private String currentUserId;
        private Date currentDateOfComment;
        private Connection conn;
        private DefaultTableModel model;

        public ButtonEditor(JCheckBox checkBox, Connection conn, DefaultTableModel model) {
            super(checkBox);
            this.conn = conn;
            this.model = model;
            setClickCountToStart(1);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            currentUserId = (String) table.getModel().getValueAt(row, 0);
            currentDateOfComment = (Date) table.getModel().getValueAt(row, 1);
            
            JButton button = new JButton();
            button.setText(value == null ? "" : value.toString());
            button.addActionListener(e -> {
               
                try {
                    Date currentDate = Date.valueOf(LocalDate.now());
                    
                    String sql = "UPDATE Feedback SET Response_Date = ? WHERE UserID = ? AND DateOfComment = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setDate(1, currentDate);
                    pstmt.setString(2, currentUserId);
                    pstmt.setDate(3, currentDateOfComment);
                    
                    int updated = pstmt.executeUpdate();
                    if (updated > 0) {
                        
                        model.setValueAt(currentDate, row, 4);
                        model.setValueAt("Update Response Date", row, 5);
                        JOptionPane.showMessageDialog(null, "Response date updated successfully!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating response date: " + ex.getMessage(), 
                                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                
                fireEditingStopped();
            });
            
            return button;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }


}