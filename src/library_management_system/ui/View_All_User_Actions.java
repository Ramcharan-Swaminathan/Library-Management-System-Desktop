package library_management_system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class View_All_User_Actions extends JFrame {
    private Connection conn;

    private JTabbedPane tabbedPane;
    private JPanel borrowedPanel, reservationPanel, cartPanel;

    public View_All_User_Actions(Connection conn) {
        this.conn = conn;

        setTitle("All Users - Library Actions");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setupUI();
        loadAllData();

        setVisible(true);
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        borrowedPanel = createTablePanel();
        reservationPanel = createTablePanel();
        cartPanel = createTablePanel();

        tabbedPane.addTab("All Borrowed Books", borrowedPanel);
        tabbedPane.addTab("All Reservations", reservationPanel);
        tabbedPane.addTab("All Cart Items", cartPanel);
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

        JButton refreshButton = new JButton("Refresh All");
        refreshButton.addActionListener(e -> loadAllData());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAllData() {
        try {
            loadAllBorrowedBooks();
            loadAllReservations();
            loadAllCartItems();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadAllBorrowedBooks() throws SQLException {
        String sql = "SELECT bb.BorrowedID, u.UserID, u.UserName, b.Title, bb.IssueDate, bb.RenewalDate, " +
                     "bb.ReturnDate, bb.TotalRenewals, bb.RenewalsLeft, bb.Fine, bb.Status " +
                     "FROM BorrowedBook bb " +
                     "JOIN Book b ON bb.BookID = b.ISBN " +
                     "JOIN Users u ON bb.UserID = u.UserID " +
                     "ORDER BY bb.IssueDate DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            Vector<String> columns = new Vector<>();
            columns.add("Borrow ID");
            columns.add("User ID");
            columns.add("User Name");
            columns.add("Book Title");
            columns.add("Issue Date");
            columns.add("Renewed Date");
            columns.add("Return Date");
            columns.add("Total Renewals");
            columns.add("Renewals Left");
            columns.add("Fine");
            columns.add("Status");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("BorrowedID"));
                row.add(rs.getString("UserID"));
                row.add(rs.getString("UserName"));
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

    private void loadAllReservations() throws SQLException {
        String sql = "SELECT r.ReservationID, u.UserID, u.UserName, b.Title, r.ReservationDate, " +
                     "r.Deadline, r.Status " +
                     "FROM Reservation r " +
                     "JOIN Book b ON r.BookID = b.ISBN " +
                     "JOIN Users u ON r.UserID = u.UserID " +
                     "ORDER BY r.ReservationDate DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            Vector<String> columns = new Vector<>();
            columns.add("Reservation ID");
            columns.add("User ID");
            columns.add("User Name");
            columns.add("Book Title");
            columns.add("Reservation Date");
            columns.add("Deadline");
            columns.add("Status");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ReservationID"));
                row.add(rs.getString("UserID"));
                row.add(rs.getString("UserName"));
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

    private void loadAllCartItems() throws SQLException {
        String sql = "SELECT cc.UserID, u.UserName, b.Title, cc.AddedDate, " +
                     "cc.ExpireDate, cc.Status " +
                     "FROM CartCollection cc " +
                     "JOIN Book b ON cc.BookID = b.ISBN " +
                     "JOIN Users u ON cc.UserID = u.UserID " +
                     "WHERE cc.Status = 'not finished' " +
                     "ORDER BY cc.AddedDate DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            Vector<String> columns = new Vector<>();
            
            columns.add("User ID");
            columns.add("User Name");
            columns.add("Book Title");
            columns.add("Added Date");
            columns.add("Expire Date");
            columns.add("Status");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("UserID"));
                row.add(rs.getString("UserName"));
                row.add(rs.getString("Title"));
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