package library_management_system.ui;
import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;

public class Renewal_Page extends JFrame {
    private Connection conn;
    private String currentUserId;
    private JTable borrowedBooksTable;
    private DefaultTableModel tableModel;

    public Renewal_Page(Connection connection, String userId) {
        this.conn = connection;
        this.currentUserId = userId;
        initComponents();
        setTitle("Book Renewal Page");
        setSize(900, 500); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        
        JLabel headerLabel = new JLabel("Your Borrowed Books", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

       
        String[] columnNames = {"Borrowed ID", "Book ID", "Issue Date", "Renewal Date", 
                              "Return Date", "Total Renewals", "Renewals Left", "Status", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; 
            }
        };

        borrowedBooksTable = new JTable(tableModel);
        borrowedBooksTable.setRowHeight(30);
        
       
        borrowedBooksTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        borrowedBooksTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), conn, tableModel, currentUserId));
        
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

       
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(e -> loadBorrowedBooks());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
       
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        try {
          
            tableModel.setRowCount(0);
            
            String sql = "SELECT bb.BorrowedID, bb.BookID, bb.IssueDate, bb.RenewalDate, " +
                         "bb.ReturnDate, bb.TotalRenewals, bb.RenewalsLeft, bb.Status, " +
                         "b.Title " +  
                         "FROM BorrowedBook bb " +
                         "JOIN Book b ON bb.BookID = b.ISBN " +
                         "WHERE bb.UserID = ? AND bb.Status IN ('Borrowed', 'Overdue') " +
                         "ORDER BY bb.IssueDate DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String borrowedId = rs.getString("BorrowedID");
                String bookId = rs.getString("BookID");
                String bookTitle = rs.getString("Title");
                Date issueDate = rs.getDate("IssueDate");
                Date renewalDate = rs.getDate("RenewalDate");
                Date returnDate = rs.getDate("ReturnDate");
                int totalRenewals = rs.getInt("TotalRenewals");
                int renewalsLeft = rs.getInt("RenewalsLeft");
                String status = rs.getString("Status");
                
                
                boolean canRenew = canRenewBook(issueDate, renewalDate, renewalsLeft, status);
                
                Object[] rowData = {
                    borrowedId,
                    bookId + " - " + bookTitle,
                    issueDate,
                    renewalDate,
                    returnDate,
                    totalRenewals,
                    renewalsLeft,
                    status,
                    canRenew ? "Renew Book" : "Cannot Renew"
                };
                
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading borrowed books: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean canRenewBook(Date issueDate, Date renewalDate, int renewalsLeft, String status) {
        
        if (renewalsLeft <= 0 || !status.equals("Borrowed")) {
            return false;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate lastRenewalOrIssue = renewalDate != null ? 
            renewalDate.toLocalDate() : issueDate.toLocalDate();
        
      
        return !today.isBefore(lastRenewalOrIssue);
    }

    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            
            
            setEnabled(value.equals("Renew Book"));
            return this;
        }
    }

   
    class ButtonEditor extends DefaultCellEditor {
        private String currentBorrowedId;
        private Connection conn;
        private DefaultTableModel model;
        private String userId;

        public ButtonEditor(JCheckBox checkBox, Connection conn, DefaultTableModel model, String userId) {
            super(checkBox);
            this.conn = conn;
            this.model = model;
            this.userId = userId;
            setClickCountToStart(1);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            currentBorrowedId = (String) table.getModel().getValueAt(row, 0);
            String actionText = (String) table.getModel().getValueAt(row, 8);
            
            JButton button = new JButton();
            button.setText(actionText);
            
            if (actionText.equals("Renew Book")) {
                button.addActionListener(e -> {
                    try {
                        
                        String sql = "UPDATE BorrowedBook SET " +
                                    "TotalRenewals = TotalRenewals, " +
                                    "RenewalsLeft = RenewalsLeft - 1, " +
                                    "Status = 'Borrowed' " + 
                                    "WHERE BorrowedID = ? AND UserID = ?";
                        
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, currentBorrowedId);
                        pstmt.setString(2, userId);
                        
                        int updated = pstmt.executeUpdate();
                        if (updated > 0) {
                            loadBorrowedBooks();
                            
                            JOptionPane.showMessageDialog(null, 
                                "Book renewed successfully! The renewal and return dates have been updated.",
                                "Renewal Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error renewing book: " + ex.getMessage(), 
                                                    "Database Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                    
                    fireEditingStopped();
                });
            }
            
            return button;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }
}