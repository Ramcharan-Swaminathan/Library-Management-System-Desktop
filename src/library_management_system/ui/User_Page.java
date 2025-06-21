package library_management_system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class User_Page extends JFrame implements ActionListener {
    private String currentUserID = "";
    private Connection conn;

    private JLabel userIDLabel, passwordLabel, membershipLabel, nameLabel, libraryLabel,
            departmentLabel, yearLabel, emailLabel, borrowedLabel, reservedLabel, fineLabel, statusLabel, searchLabel;
    
    
    private JTextField userIDField, nameField, departmentField, yearField, emailField, 
            borrowedField, reservedField, fineField, searchField;
    private JPasswordField passwordField;
    private JComboBox<String> membershipCombo, libraryCombo, statusCombo;
    
   
    private JButton addButton, viewButton, editButton, updateButton, deleteButton, 
            clearButton, exitButton, searchButton;
    
   
    private JPanel mainPanel, formPanel, buttonPanel, searchPanel;

    public User_Page(Connection conn) {
        this.conn = conn;
        setTitle("User Management Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setupPanels();
        add(mainPanel);
        setVisible(true);
    }

    private void initializeComponents() {
        
        userIDLabel = new JLabel("User ID:");
        passwordLabel = new JLabel("Password:");
        membershipLabel = new JLabel("Membership:");
        nameLabel = new JLabel("Name:");
        libraryLabel = new JLabel("Library:");
        departmentLabel = new JLabel("Department:");
        yearLabel = new JLabel("Joined Year:");
        emailLabel = new JLabel("Email:");
        borrowedLabel = new JLabel("Borrowed Books:");
        reservedLabel = new JLabel("Reserved Books:");
        fineLabel = new JLabel("Fine ($):");
        statusLabel = new JLabel("Status:");
        searchLabel = new JLabel("Search User By (UserId,UserName):");

       
        userIDField = new JTextField(15);
        passwordField = new JPasswordField(15);
        nameField = new JTextField(15);
        departmentField = new JTextField(15);
        yearField = new JTextField(15);
        emailField = new JTextField(15);
        borrowedField = new JTextField(15);
        reservedField = new JTextField(15);
        fineField = new JTextField(15);
        searchField = new JTextField(20);

     
        membershipCombo = new JComboBox<>(new String[]{"student", "faculty"});
        libraryCombo = new JComboBox<>();
        fetchLibraries(); // Load libraries from database
        statusCombo = new JComboBox<>(new String[]{"not blocked", "blocked"});

        addButton = new JButton("Add");
        viewButton = new JButton("View All");
        editButton = new JButton("Edit");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");
        searchButton = new JButton("Search");

      
        borrowedField.setText("0");
        reservedField.setText("0");
        fineField.setText("0");

        
        for (JButton button : new JButton[]{addButton, viewButton, editButton, updateButton, 
                deleteButton, clearButton, exitButton, searchButton}) {
            button.addActionListener(this);
        }
    }

    private void fetchLibraries() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT LibraryName FROM Library");
            libraryCombo.removeAllItems();
            while (rs.next()) {
                libraryCombo.addItem(rs.getString("LibraryName"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading libraries: " + ex.getMessage());
        }
    }

    private void setupPanels() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search Panel (North)
        searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Form Panel (Center)
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(gbc, 0, userIDLabel, userIDField);
        addFormRow(gbc, 1, passwordLabel, passwordField);
        addFormRow(gbc, 2, membershipLabel, membershipCombo);
        addFormRow(gbc, 3, nameLabel, nameField);
        addFormRow(gbc, 4, libraryLabel, libraryCombo);
        addFormRow(gbc, 5, departmentLabel, departmentField);
        addFormRow(gbc, 6, yearLabel, yearField);
        addFormRow(gbc, 7, emailLabel, emailField);
        addFormRow(gbc, 8, borrowedLabel, borrowedField);
        addFormRow(gbc, 9, reservedLabel, reservedField);
        addFormRow(gbc, 10, fineLabel, fineField);
        addFormRow(gbc, 11, statusLabel, statusCombo);

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

    private void addFormRow(GridBagConstraints gbc, int row, JLabel label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(field, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addButton) {
                addUser();
            } else if (e.getSource() == viewButton) {
                viewAllUsers();
            } else if (e.getSource() == editButton) {
                editUser();
            } else if (e.getSource() == updateButton) {
                updateUser();
            } else if (e.getSource() == deleteButton) {
                deleteUser();
            } else if (e.getSource() == clearButton) {
                clearFields();
            } else if (e.getSource() == exitButton) {
                System.exit(0);
            } else if (e.getSource() == searchButton) {
                searchUser();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Numeric fields must contain valid numbers!");
        }
    }

    private void addUser() throws SQLException {
        // Validate email format
        if (!emailField.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return;
        }

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        ps.setString(1, userIDField.getText());
        ps.setString(2, new String(passwordField.getPassword()));
        ps.setString(3, (String) membershipCombo.getSelectedItem());
        ps.setString(4, nameField.getText());
        ps.setString(5, (String) libraryCombo.getSelectedItem());
        ps.setString(6, departmentField.getText());
        ps.setInt(7, Integer.parseInt(yearField.getText()));
        ps.setString(8, emailField.getText());
        ps.setInt(9, Integer.parseInt(borrowedField.getText()));
        ps.setInt(10, Integer.parseInt(reservedField.getText()));
        ps.setDouble(11, Double.parseDouble(fineField.getText()));
        ps.setString(12, (String) statusCombo.getSelectedItem());

        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "User added successfully!");
        clearFields();
    }

    private void viewAllUsers() throws SQLException {
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "No users found!");
            return;
        }
        
        rs.beforeFirst();
        JTable table = new JTable(buildTableModel(rs));
        JDialog dialog = new JDialog();
        dialog.setTitle("All Users");
        dialog.add(new JScrollPane(table));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editUser() throws SQLException {
        String userID = JOptionPane.showInputDialog(this, "Enter User ID to edit:");
        if (userID == null || userID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID cannot be empty!");
            return;
        }

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
        ps.setString(1, userID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            userIDField.setText(rs.getString("UserID"));
            passwordField.setText(rs.getString("Password"));
            membershipCombo.setSelectedItem(rs.getString("MembershipType"));
            nameField.setText(rs.getString("UserName"));
            libraryCombo.setSelectedItem(rs.getString("LibraryName"));
            departmentField.setText(rs.getString("Department"));
            yearField.setText(rs.getString("JoinedYear"));
            emailField.setText(rs.getString("Email"));
            borrowedField.setText(rs.getString("NoBorrowedBooks"));
            reservedField.setText(rs.getString("NoReservedBooks"));
            fineField.setText(rs.getString("Fine"));
            statusCombo.setSelectedItem(rs.getString("Status"));

            userIDField.setEditable(false);
            currentUserID = userID;
            JOptionPane.showMessageDialog(this, "Edit fields and click Update.");
        } else {
            JOptionPane.showMessageDialog(this, "User not found!");
        }
    }

    private void updateUser() throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE Users SET Password=?, MembershipType=?, UserName=?, LibraryName=?, " +
            "Department=?, JoinedYear=?, Email=?, NoBorrowedBooks=?, NoReservedBooks=?, Fine=?, Status=? " +
            "WHERE UserID=?"
        );

        ps.setString(1, new String(passwordField.getPassword()));
        ps.setString(2, (String) membershipCombo.getSelectedItem());
        ps.setString(3, nameField.getText());
        ps.setString(4, (String) libraryCombo.getSelectedItem());
        ps.setString(5, departmentField.getText());
        ps.setInt(6, Integer.parseInt(yearField.getText()));
        ps.setString(7, emailField.getText());
        ps.setInt(8, Integer.parseInt(borrowedField.getText()));
        ps.setInt(9, Integer.parseInt(reservedField.getText()));
        ps.setDouble(10, Double.parseDouble(fineField.getText()));
        ps.setString(11, (String) statusCombo.getSelectedItem());
        ps.setString(12, currentUserID);

        int updated = ps.executeUpdate();
        if (updated > 0) {
            JOptionPane.showMessageDialog(this, "User updated!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed!");
        }
    }

    private void deleteUser() throws SQLException {
        String userID = JOptionPane.showInputDialog(this, "Enter User ID to delete:");
        if (userID == null || userID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID cannot be empty!");
            return;
        }

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE UserID = ?");
        ps.setString(1, userID);
        int deleted = ps.executeUpdate();
        if (deleted > 0) {
            JOptionPane.showMessageDialog(this, "User deleted!");
        } else {
            JOptionPane.showMessageDialog(this, "User not found!");
        }
    }

    private void searchUser() throws SQLException {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term!");
            return;
        }

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM Users WHERE UserID LIKE ? OR UserName LIKE ?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,  // Allows moving cursor backward
            ResultSet.CONCUR_READ_ONLY
        );
        ps.setString(1, "%" + searchTerm + "%");
        ps.setString(2, "%" + searchTerm + "%");
        
        ResultSet rs = ps.executeQuery();
        if (!rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(this, "No matching users found!");
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
        userIDField.setText("");
        passwordField.setText("");
        membershipCombo.setSelectedIndex(0);
        nameField.setText("");
        libraryCombo.setSelectedIndex(0);
        departmentField.setText("");
        yearField.setText("");
        emailField.setText("");
        borrowedField.setText("0");
        reservedField.setText("0");
        fineField.setText("0");
        statusCombo.setSelectedIndex(0);
        userIDField.setEditable(true);
        currentUserID = "";
    }

    private static javax.swing.table.DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columns = new Vector<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columns.add(metaData.getColumnName(i));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new javax.swing.table.DefaultTableModel(data, columns);
    }


}