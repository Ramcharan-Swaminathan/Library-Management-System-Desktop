package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class Book_Page extends JFrame implements ActionListener {
    String currentIsbn = "";
    private JLabel isbnLabel, titleLabel, publisherLabel, libraryLabel, groupLabel,
            totalLabel, availableLabel, reserveLabel, rowLabel, rackLabel, ratingLabel, searchLabel;
    private JTextField isbnField, titleField, publisherField, libraryField, groupField,
            totalField, availableField, reserveField, rowField, rackField, ratingField, searchField;
    private JButton addButton, viewButton, editButton, updateButton, deleteButton, clearButton, exitButton, searchButton;
    private JPanel mainPanel, formPanel, buttonPanel, searchPanel;

    private Connection conn;

    public Book_Page(Connection conn) {
        this.conn = conn;

        setTitle("Book Management Dashboard");
        setSize(900, 650); // Increased height to accommodate search panel
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setupPanels();
        add(mainPanel);
        setVisible(true);
    }

    private void initializeComponents() {
     
        isbnLabel = new JLabel("ISBN:");
        titleLabel = new JLabel("Title:");
        publisherLabel = new JLabel("Publisher ID:");
        libraryLabel = new JLabel("Library Name:");
        groupLabel = new JLabel("Book Group:");
        totalLabel = new JLabel("Total Copies:");
        availableLabel = new JLabel("Available Copies:");
        reserveLabel = new JLabel("No. of Reservations:");
        rowLabel = new JLabel("Row No:");
        rackLabel = new JLabel("Rack No:");
        ratingLabel = new JLabel("Avg Rating:");
        searchLabel = new JLabel("Search Book By (ISBN or Title):");

        
        isbnField = new JTextField(15);
        titleField = new JTextField(15);
        publisherField = new JTextField(15);
        libraryField = new JTextField(15);
        groupField = new JTextField(15);
        totalField = new JTextField(15);
        availableField = new JTextField(15);
        reserveField = new JTextField(15);
        rowField = new JTextField(15);
        rackField = new JTextField(15);
        ratingField = new JTextField(15);
        searchField = new JTextField(20);

        
        addButton = new JButton("Add");
        viewButton = new JButton("View All");
        editButton = new JButton("Edit");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");
        searchButton = new JButton("Search");

        
        for (JButton button : new JButton[]{addButton, viewButton, editButton, updateButton, 
                deleteButton, clearButton, exitButton, searchButton}) {
            button.addActionListener(this);
        }
    }

    private void setupPanels() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(gbc, 0, isbnLabel, isbnField);
        addFormRow(gbc, 1, titleLabel, titleField);
        addFormRow(gbc, 2, publisherLabel, publisherField);
        addFormRow(gbc, 3, libraryLabel, libraryField);
        addFormRow(gbc, 4, groupLabel, groupField);
        addFormRow(gbc, 5, totalLabel, totalField);
        addFormRow(gbc, 6, availableLabel, availableField);
        addFormRow(gbc, 7, reserveLabel, reserveField);
        addFormRow(gbc, 8, rowLabel, rowField);
        addFormRow(gbc, 9, rackLabel, rackField);
        addFormRow(gbc, 10, ratingLabel, ratingField);

 
        buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormRow(GridBagConstraints gbc, int row, JLabel label, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(field, gbc);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addButton) {
                addBook();
            } else if (e.getSource() == viewButton) {
                viewAllBooks();
            } else if (e.getSource() == editButton) {
                editBook();
            } else if (e.getSource() == updateButton) {
                updateBook();
            } else if (e.getSource() == deleteButton) {
                deleteBook();
            } else if (e.getSource() == clearButton) {
                clearFields();
            } else if (e.getSource() == exitButton) {
                System.exit(0);
            } else if (e.getSource() == searchButton) {
                searchBook();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addBook() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Book VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, isbnField.getText());
        ps.setString(2, titleField.getText());
        ps.setString(3, publisherField.getText());
        ps.setString(4, libraryField.getText());
        ps.setString(5, groupField.getText());
        ps.setInt(6, Integer.parseInt(totalField.getText()));
        ps.setInt(7, Integer.parseInt(availableField.getText()));
        ps.setInt(8, Integer.parseInt(reserveField.getText()));
        ps.setString(9, rowField.getText());
        ps.setString(10, rackField.getText());
        ps.setDouble(11, Double.parseDouble(ratingField.getText()));
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Book added to database.");
        clearFields();
    }

    private void viewAllBooks() throws SQLException {
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Book");
        countRs.next();
        int totalRecords = countRs.getInt("total");

        if (totalRecords == 0) {
            JOptionPane.showMessageDialog(this, "No records found in Book table!");
            return;
        }

        ResultSet rs = stmt.executeQuery("SELECT * FROM Book");
        JTable table = new JTable(buildTableModel(rs));
        JDialog dialog = new JDialog();
        dialog.setTitle("Book Records (Total: " + totalRecords + ")");
        dialog.add(new JScrollPane(table));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editBook() throws SQLException {
        String isbn = JOptionPane.showInputDialog(this, "Enter ISBN to edit:");
        if (isbn == null || isbn.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ISBN cannot be empty.");
            return;
        }
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Book WHERE ISBN = ?");
        ps.setString(1, isbn);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            isbnField.setText(rs.getString("ISBN"));
            titleField.setText(rs.getString("Title"));
            publisherField.setText(rs.getString("PublisherID"));
            libraryField.setText(rs.getString("LibraryName"));
            groupField.setText(rs.getString("BookGroup"));
            totalField.setText(rs.getString("TotalCopies"));
            availableField.setText(rs.getString("AvailableCopies"));
            reserveField.setText(rs.getString("NoReservation"));
            rowField.setText(rs.getString("RowNo"));
            rackField.setText(rs.getString("RackNo"));
            ratingField.setText(rs.getString("Avg_Rating"));

            isbnField.setEditable(false);
            currentIsbn = isbn;
            JOptionPane.showMessageDialog(this, "Edit the fields and click Update to save changes.");
        } else {
            JOptionPane.showMessageDialog(this, "ISBN not found.");
        }
    }

    private void updateBook() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE Book SET Title=?, PublisherID=?, LibraryName=?, BookGroup=?, TotalCopies=?, " +
                "AvailableCopies=?, NoReservation=?, RowNo=?, RackNo=?, Avg_Rating=? WHERE ISBN=?");

        ps.setString(1, titleField.getText());
        ps.setString(2, publisherField.getText());
        ps.setString(3, libraryField.getText());
        ps.setString(4, groupField.getText());
        ps.setInt(5, Integer.parseInt(totalField.getText()));
        ps.setInt(6, Integer.parseInt(availableField.getText()));
        ps.setInt(7, Integer.parseInt(reserveField.getText()));
        ps.setString(8, rowField.getText());
        ps.setString(9, rackField.getText());
        ps.setDouble(10, Double.parseDouble(ratingField.getText()));
        ps.setString(11, currentIsbn);

        int updated = ps.executeUpdate();
        if (updated > 0)
            JOptionPane.showMessageDialog(this, "Book updated.");
        else
            JOptionPane.showMessageDialog(this, "Update failed.");

        clearFields();
        isbnField.setEditable(true);
    }

    private void deleteBook() throws SQLException {
        String isbn = JOptionPane.showInputDialog(this, "Enter ISBN to delete:");
        PreparedStatement ps = conn.prepareStatement("DELETE FROM Book WHERE ISBN = ?");
        ps.setString(1, isbn);
        int deleted = ps.executeUpdate();
        if (deleted > 0)
            JOptionPane.showMessageDialog(this, "Book deleted.");
        else
            JOptionPane.showMessageDialog(this, "ISBN not found.");
    }

    private void searchBook() throws SQLException {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term!");
            return;
        }

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM Book WHERE ISBN LIKE ? OR Title LIKE ?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        );
        ps.setString(1, "%" + searchTerm + "%");
        ps.setString(2, "%" + searchTerm + "%");
        
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "No matching books found!");
            return;
        }
        
        rs.beforeFirst();
        JTable table = new JTable(buildTableModel(rs));
        JDialog dialog = new JDialog();
        dialog.setTitle("Search Results for: " + searchTerm);
        dialog.add(new JScrollPane(table));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void clearFields() {
        for (JTextField field : new JTextField[]{isbnField, titleField, publisherField, libraryField,
                groupField, totalField, availableField, reserveField, rowField, rackField, ratingField,searchField}) {
            field.setText("");
        }
        isbnField.setEditable(true);
        currentIsbn = "";
    }

    private static javax.swing.table.DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++)
            columnNames.add(metaData.getColumnName(i));

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int i = 1; i <= columnCount; i++)
                vector.add(rs.getObject(i));
            data.add(vector);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}