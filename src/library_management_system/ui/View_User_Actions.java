package library_management_system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class View_User_Actions extends JFrame {
    private Connection conn;
    private String currentUserID;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel borrowedPanel, reservationPanel, cartPanel;

    public View_User_Actions(Connection conn, String userID) {
        this.conn = conn;
        this.currentUserID = userID;
        System.out.println("id from view action page"+userID);
        
        setTitle("Your Library Actions - " + userID);
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupUI();
        loadAllData();
        
        setVisible(true);
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Create panels for each tab
        borrowedPanel = createTablePanel();
        reservationPanel = createTablePanel();
        cartPanel = createTablePanel();
        
        // Add tabs
        tabbedPane.addTab("Borrowed Books", borrowedPanel);
        tabbedPane.addTab("Reservations", reservationPanel);
        tabbedPane.addTab("Cart Items", cartPanel);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupUI() {
        add(tabbedPane, BorderLayout.CENTER);
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadAllData());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAllData() {
        try {
            loadBorrowedBooks();
            loadReservations();
            loadCartItems();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadBorrowedBooks() throws SQLException {
        String sql = "SELECT bb.BorrowedID, b.Title, bb.IssueDate, bb.RenewalDate, " +
                     "bb.ReturnDate, bb.TotalRenewals, bb.RenewalsLeft, bb.Fine, bb.Status " +
                     "FROM BorrowedBook bb " +
                     "JOIN Book b ON bb.BookID = b.ISBN " +
                     "WHERE bb.UserID = ? " +
                     "ORDER BY bb.IssueDate DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            
            Vector<String> columns = new Vector<>();
            columns.add("Borrow ID");
            columns.add("Book Title");
            columns.add("Issue Date");
            columns.add("Renewal Date");
            columns.add("Return Date");
            columns.add("Total Renewals");
            columns.add("Renewals Left");
            columns.add("Fine");
            columns.add("Status");
            
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("BorrowedID"));
                row.add(rs.getString("Title"));
                row.add(rs.getDate("IssueDate"));
                row.add(rs.getDate("RenewalDate"));
                row.add(rs.getDate("ReturnDate"));
                row.add(rs.getInt("TotalRenewals"));
                row.add(rs.getInt("RenewalsLeft"));
                row.add(rs.getDouble("Fine"));
                row.add(rs.getString("Status"));
                data.add(row);
            }
            
            JTable table = (JTable)((JScrollPane)borrowedPanel.getComponent(0)).getViewport().getView();
            table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        }
    }

    private void loadReservations() throws SQLException {
        String sql = "SELECT r.ReservationID, b.Title, r.ReservationDate, " +
                     "r.Deadline, r.Status " +
                     "FROM Reservation r " +
                     "JOIN Book b ON r.BookID = b.ISBN " +
                     "WHERE r.UserID = ? " +
                     "ORDER BY r.ReservationDate DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            
            Vector<String> columns = new Vector<>();
            columns.add("Reservation ID");
            columns.add("Book Title");
            columns.add("Reservation Date");
            columns.add("Deadline");
            columns.add("Status");
            
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ReservationID"));
                row.add(rs.getString("Title"));
                row.add(rs.getDate("ReservationDate"));
                row.add(rs.getDate("Deadline"));
                row.add(rs.getString("Status"));
                data.add(row);
            }
            
            JTable table = (JTable)((JScrollPane)reservationPanel.getComponent(0)).getViewport().getView();
            table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        }
    }

    private void loadCartItems() throws SQLException {
        String sql = "SELECT cc.UserID, b.ISBN, cc.AddedDate, " +
                     "cc.ExpireDate, cc.Status " +
                     "FROM CartCollection cc " +
                     "JOIN Book b ON cc.BookID = b.ISBN " +
                     "WHERE cc.UserID = ? AND cc.Status = 'not finished' " +
                     "ORDER BY cc.AddedDate DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            
            Vector<String> columns = new Vector<>();
            columns.add("UserID ");
            columns.add("BookID");
            columns.add("Added Date");
            columns.add("Expire Date");
            columns.add("Status");
            
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("UserID"));
                row.add(rs.getString("ISBN"));
                row.add(rs.getDate("AddedDate"));
                row.add(rs.getDate("ExpireDate"));
                row.add(rs.getString("Status"));
                data.add(row);
            }
            
            JTable table = (JTable)((JScrollPane)cartPanel.getComponent(0)).getViewport().getView();
            table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        }
    }


}