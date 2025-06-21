package library_management_system.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import library_management_system.ui.View_User_Actions;

public class User_Action_Page extends JFrame implements ActionListener {
    private Connection conn;
    private String currentUserID;
    private String currentBookID;

    private JTextField searchField;
    private JButton searchButton;
    private JButton View_Page;
    private JTextArea bookDetailsArea;
    private JButton borrowButton, reserveButton, addToCartButton;

    public User_Action_Page(Connection conn, String userID) {
        this.conn = conn;
        this.currentUserID = userID;
        System.out.println("id from action page: " + userID);

        setTitle("Library Book Management - User Actions");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setupUI();
        setVisible(true);
    }

    private void initializeComponents() {
        searchField = new JTextField(30);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        View_Page = new JButton("View_Page");  
        View_Page.addActionListener(this); 

        bookDetailsArea = new JTextArea(10, 50);
        bookDetailsArea.setEditable(false);
        bookDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        borrowButton = new JButton("Borrow Book");
        reserveButton = new JButton("Make Reservation");
        addToCartButton = new JButton("Add to Cart");
        
        borrowButton.addActionListener(this);
        reserveButton.addActionListener(this);
        addToCartButton.addActionListener(this);
        
        setActionButtonsEnabled(false);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search by ISBN or Title:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(View_Page);

        JScrollPane bookScrollPane = new JScrollPane(bookDetailsArea);
        bookScrollPane.setBorder(BorderFactory.createTitledBorder("Book Details"));

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        actionPanel.add(borrowButton);
        actionPanel.add(reserveButton);
        actionPanel.add(addToCartButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(bookScrollPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setActionButtonsEnabled(boolean enabled) {
        borrowButton.setEnabled(enabled);
        reserveButton.setEnabled(enabled);
        addToCartButton.setEnabled(enabled);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == searchButton) {
                searchBooks();
            } else if (e.getSource() == borrowButton) {
                borrowBook();
            } else if (e.getSource() == reserveButton) {
                reserveBook();
            } else if (e.getSource() == addToCartButton) {
                addToCart();
            } else if (e.getSource() == View_Page) {
                Move_TO_ViewPage();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchBooks() throws SQLException {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term");
            return;
        }
    
        String sql = "SELECT b.ISBN, b.Title, b.PublisherID, b.LibraryName, " +
                     "b.BookGroup, b.TotalCopies, b.AvailableCopies, " +
                     "b.NoReservation, b.RowNo, b.RackNo, b.Avg_Rating, " +
                     "LISTAGG(a.AuthorName, ', ') WITHIN GROUP (ORDER BY a.AuthorName) AS Authors, " +
                     "COUNT(r.BookID) AS active_reservations " +
                     "FROM Book b " +
                     "LEFT JOIN AuthorWithBook awb ON b.ISBN = awb.BookID " +
                     "LEFT JOIN Author a ON awb.AuthorID = a.AuthorID " +
                     "LEFT JOIN Reservation r ON b.ISBN = r.BookID AND r.Status = 'Reserved' " +
                     "WHERE (b.ISBN LIKE ? OR b.Title LIKE ?) " +
                     "GROUP BY b.ISBN, b.Title, b.PublisherID, b.LibraryName, " +
                     "b.BookGroup, b.TotalCopies, b.AvailableCopies, " +
                     "b.NoReservation, b.RowNo, b.RackNo, b.Avg_Rating " +
                     "FETCH FIRST 1 ROWS ONLY";
    
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                currentBookID = rs.getString("ISBN");
                StringBuilder details = new StringBuilder();
                
                details.append(String.format("ISBN: %s\n", currentBookID));
                details.append(String.format("Title: %s\n", rs.getString("Title")));
                details.append(String.format("Authors: %s\n", rs.getString("Authors")));
                details.append(String.format("Publisher: %s\n", rs.getString("PublisherID")));
                details.append(String.format("Library: %s\n", rs.getString("LibraryName")));
                details.append(String.format("Group: %s\n", rs.getString("BookGroup")));
                details.append(String.format("Total Copies: %d\n", rs.getInt("TotalCopies")));
                details.append(String.format("Available Copies: %d\n", rs.getInt("AvailableCopies")));
                details.append(String.format("Active Reservations: %d\n", rs.getInt("active_reservations")));
                details.append(String.format("Row: %s\n", rs.getString("RowNo")));
                details.append(String.format("Rack: %s\n", rs.getString("RackNo")));
                details.append(String.format("Average Rating: %.1f\n", rs.getDouble("Avg_Rating")));
                
                String authorIds = getAuthorIds(currentBookID);
                details.append(String.format("Author IDs: %s\n", authorIds));
                
                bookDetailsArea.setText(details.toString());
                setActionButtonsEnabled(true);
            } else {
                bookDetailsArea.setText("No available books found matching your search.");
                setActionButtonsEnabled(false);
            }
        }
    }
    
    private String getAuthorIds(String bookId) throws SQLException {
        String sql = "SELECT LISTAGG(a.AuthorID, ', ') WITHIN GROUP (ORDER BY a.AuthorID) AS AuthorIDs " +
                     "FROM AuthorWithBook awb " +
                     "JOIN Author a ON awb.AuthorID = a.AuthorID " +
                     "WHERE awb.BookID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("AuthorIDs") : "None";
        }
    }

    private void borrowBook() throws SQLException {
        // Check if user can borrow more books
        if (!canUserBorrowMore()) {
            JOptionPane.showMessageDialog(this, 
                "You have reached the maximum number of borrowed books (5).\n" +
                "Please return some books before borrowing new ones.",
                "Borrowing Limit Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if book is available
        if (!isBookAvailable(currentBookID)) {
            JOptionPane.showMessageDialog(this, 
                "This book is no longer available for borrowing.",
                "Book Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generate a unique BorrowedID
        String borrowedId = generateBorrowedId();
        
        // Insert into BorrowedBook (virtual columns will be auto-calculated)
        String sql = "INSERT INTO BorrowedBook " +
                     "(BorrowedID, UserID, BookID, IssueDate, TotalRenewals, RenewalsLeft, Status) " +
                     "VALUES (?, ?, ?, CURRENT_DATE, 3, 3, 'Borrowed')";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, borrowedId);
            ps.setString(2, currentUserID);
            ps.setString(3, currentBookID);
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                // Get the generated dates to show to user
                String datesSql = "SELECT RenewalDate, ReturnDate FROM BorrowedBook WHERE BorrowedID = ?";
                try (PreparedStatement datesPs = conn.prepareStatement(datesSql)) {
                    datesPs.setString(1, borrowedId);
                    ResultSet rs = datesPs.executeQuery();
                    
                    if (rs.next()) {
                        Date renewalDate = rs.getDate("RenewalDate");
                        Date returnDate = rs.getDate("ReturnDate");
                        
                        updateBookAvailability(currentBookID, -1);
                        
                        JOptionPane.showMessageDialog(this, 
                            "Book borrowed successfully!\n\n" +
                            "First renewal date: " + renewalDate + "\n" +
                            "Return date: " + returnDate + "\n\n" +
                            "You have 3 renewals available.",
                            "Borrowing Successful", JOptionPane.INFORMATION_MESSAGE);
                        
                        resetAfterAction();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to borrow book.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean canUserBorrowMore() throws SQLException {
        String sql = "SELECT COUNT(*) AS current_borrowed FROM BorrowedBook " +
                     "WHERE UserID = ? AND Status IN ('Borrowed', 'Overdue')";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentUserID);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt("current_borrowed") < 5;
        }
    }

    private boolean isBookAvailable(String bookId) throws SQLException {
        String sql = "SELECT AvailableCopies FROM Book WHERE ISBN = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt("AvailableCopies") > 0;
        }
    }

    private String generateBorrowedId() throws SQLException {
        String sql = "SELECT MAX(TO_NUMBER(BorrowedID)) + 1 AS new_id FROM BorrowedBook";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getString("new_id") != null) {
                return String.format("%010d", rs.getLong("new_id"));
            }
        }
        return "1000000001";
    }

    private void reserveBook() throws SQLException {
        if (!isBookAvailable(currentBookID)) {
            JOptionPane.showMessageDialog(this, 
                "This book is not available for reservation.",
                "Book Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Generate a unique reservation ID
        String reservationId = generateReservationId();
        LocalDate deadline = LocalDate.now().plusDays(7);
        
        String sql = "INSERT INTO Reservation " +
                     "(ReservationID, UserID, BookID, ReservationDate, Deadline, Status) " +
                     "VALUES (?, ?, ?, CURRENT_DATE, TO_DATE(?, 'YYYY-MM-DD'), 'Reserved')";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reservationId);
            ps.setString(2, currentUserID);
            ps.setString(3, currentBookID);
            ps.setString(4, deadline.toString());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Book reserved successfully!\nReservation ID: " + reservationId + 
                    "\nExpires: " + deadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    "Reservation Successful", JOptionPane.INFORMATION_MESSAGE);
                resetAfterAction();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reserve book.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String generateReservationId() throws SQLException {
        // Get the maximum existing ID and increment it
        String sql = "SELECT MAX(TO_NUMBER(SUBSTR(ReservationID, 2))) + 1 AS new_id FROM Reservation";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getString("new_id") != null) {
                return "R" + String.format("%03d", rs.getInt("new_id"));
            }
        }
        return "R001"; // Fallback if no reservations exist yet
    }
    private void addToCart() throws SQLException {
        LocalDate expireDate = LocalDate.now().plusDays(3);
        
        String sql = "INSERT INTO CartCollection " +
                     "(UserID, BookID, AddedDate, ExpireDate, Status) " +
                     "VALUES (?, ?, CURRENT_DATE, TO_DATE(?, 'YYYY-MM-DD'), 'not finished')";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currentUserID);
            ps.setString(2, currentBookID);
            ps.setString(3, expireDate.toString());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Book added to cart successfully!\nExpires: " + 
                    expireDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
                resetAfterAction();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book to cart.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateBookAvailability(String bookId, int change) throws SQLException {
        String sql = "UPDATE Book SET AvailableCopies = AvailableCopies + ? WHERE ISBN = ? AND AvailableCopies > 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, change);
            ps.setString(2, bookId);
            ps.executeUpdate();
        }
    }

    private void resetAfterAction() {
        searchField.setText("");
        bookDetailsArea.setText("");
        setActionButtonsEnabled(false);
        currentBookID = null;
    }
     
    private void Move_TO_ViewPage() {
        new View_User_Actions(conn, currentUserID);
    }
}
