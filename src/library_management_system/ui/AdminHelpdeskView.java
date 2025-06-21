package library_management_system.ui;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;

public class AdminHelpdeskView extends JFrame {
    private Connection conn;
    private JTable helpdeskTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusComboBox;

    public AdminHelpdeskView(Connection connection) {
        this.conn = connection;
        initComponents();
        setTitle("Admin - Helpdesk Management");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

private void initComponents() {
    // Main panel with border layout
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    
    // Table model setup
    String[] columnNames = {"Request ID", "User ID", "Request Date", "Issue Type", "Description", "Status", "Response", "Response Date", "Actions"};
    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 8; // Only the Actions column is editable
        }
    };
    
    helpdeskTable = new JTable(tableModel);
    helpdeskTable.setRowHeight(30);
    helpdeskTable.getColumnModel().getColumn(4).setPreferredWidth(300);
    helpdeskTable.getColumnModel().getColumn(6).setPreferredWidth(250);
    helpdeskTable.getColumnModel().getColumn(8).setPreferredWidth(130);
    
    // Add button renderer and editor
    helpdeskTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
    helpdeskTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), conn, tableModel));
    
    // Wrap table in scroll pane
    JScrollPane scrollPane = new JScrollPane(helpdeskTable);
    
    // Create a container panel for the table
    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.add(scrollPane, BorderLayout.CENTER);
    
    // Status filter panel
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filterPanel.add(new JLabel("Filter by Status:"));
    
    statusComboBox = new JComboBox<>(new String[]{"All", "Pending", "In Progress", "Resolved"});
    statusComboBox.addActionListener(e -> loadHelpdeskData());
    filterPanel.add(statusComboBox);
    
    // Title panel
    JPanel titlePanel = new JPanel();
    titlePanel.add(new JLabel("Helpdesk Request Management", JLabel.CENTER));
    
    // Button panel
    JPanel buttonPanel = new JPanel();
    JButton refreshButton = new JButton("Refresh Requests");
    refreshButton.addActionListener(e -> loadHelpdeskData());
    buttonPanel.add(refreshButton);
    
    // Add components to main panel with proper layout
    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(filterPanel, BorderLayout.PAGE_START);
    mainPanel.add(tablePanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    // Set content pane
    getContentPane().add(mainPanel);
    
    // Load data and make visible
    loadHelpdeskData();
    setVisible(true);
}

    private void loadHelpdeskData() {
        System.out.println("Attempting to load data...");
        try {
            tableModel.setRowCount(0);
            
            String statusFilter = (String) statusComboBox.getSelectedItem();
            String sql = "SELECT * FROM HelpdeskRequests";
            
            if (!"All".equals(statusFilter)) {
                sql += " WHERE Status = ?";
            }
            sql += " ORDER BY RequestDate DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            if (!"All".equals(statusFilter)) {
                pstmt.setString(1, statusFilter);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int requestId = rs.getInt("RequestID");
                String userId = rs.getString("UserID");
                Date requestDate = rs.getDate("RequestDate");
                String issueType = rs.getString("IssueType");
                String description = rs.getString("Description");
                String status = rs.getString("Status");
                String response = rs.getString("Response");
                Date responseDate = rs.getDate("ResponseDate");
                
                Object[] rowData = {
                    requestId,
                    userId,
                    requestDate,
                    issueType,
                    description,
                    status,
                    response,
                    responseDate,
                    "Manage Request"
                };
                
                tableModel.addRow(rowData);
            }
              System.out.println("Loaded " + tableModel.getRowCount() + " rows");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading helpdesk requests: " + ex.getMessage(), 
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
        String status = (String) table.getModel().getValueAt(row, 5); // Status column
        setText((value == null) ? "" : value.toString());
        setEnabled(!"Resolved".equals(status)); // Disable if resolved
        return this;
    }
    }

    // Button editor for the table
    class ButtonEditor extends DefaultCellEditor {
        private int currentRequestId;
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
        
        String status = (String) table.getModel().getValueAt(row, 5);
        if ("Resolved".equals(status)) {
            return new JLabel("Resolved"); // Show label instead of button
        }
        
        currentRequestId = (int) table.getModel().getValueAt(row, 0);
        JButton button = new JButton("Manage Request");
        button.addActionListener(e -> {
            showRequestManagementDialog(row);
            fireEditingStopped();
        });
        return button;
    }

        private void showRequestManagementDialog(int row) {
            JDialog dialog = new JDialog(AdminHelpdeskView.this, "Manage Helpdesk Request", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(500, 400);
            
            // Current request details
            String currentStatus = (String) model.getValueAt(row, 5);
            String currentResponse = (String) model.getValueAt(row, 6);
            
            JPanel detailsPanel = new JPanel(new GridLayout(6, 1, 5, 5));
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            detailsPanel.add(new JLabel("Request ID: " + currentRequestId));
            detailsPanel.add(new JLabel("Current Status: " + currentStatus));
            
            // Status update
            JPanel statusPanel = new JPanel();
            statusPanel.add(new JLabel("Update Status:"));
            
            JComboBox<String> statusUpdateCombo = new JComboBox<>(
                new String[]{"Pending", "In Progress", "Resolved"});
            statusUpdateCombo.setSelectedItem(currentStatus);
            statusPanel.add(statusUpdateCombo);
            detailsPanel.add(statusPanel);
            
            // Response text area
            detailsPanel.add(new JLabel("Response:"));
            JTextArea responseArea = new JTextArea(currentResponse != null ? currentResponse : "");
            responseArea.setLineWrap(true);
            responseArea.setWrapStyleWord(true);
            JScrollPane responseScroll = new JScrollPane(responseArea);
            detailsPanel.add(responseScroll);
            
            dialog.add(detailsPanel, BorderLayout.CENTER);
            
            // Action buttons
            JPanel buttonPanel = new JPanel();
            JButton saveButton = new JButton("Save Changes");
            saveButton.addActionListener(e -> {
                try {
                    String newStatus = (String) statusUpdateCombo.getSelectedItem();
                    String newResponse = responseArea.getText().trim();
                    Date currentDate = Date.valueOf(LocalDate.now());
                    
                    String sql = "UPDATE HelpdeskRequests SET Status = ?, Response = ?, " +
                                "ResponseDate = ? WHERE RequestID = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newStatus);
                    pstmt.setString(2, newResponse.isEmpty() ? null : newResponse);
                    pstmt.setDate(3, "Resolved".equals(newStatus) ? currentDate : null);
                    pstmt.setInt(4, currentRequestId);
                    
                    int updated = pstmt.executeUpdate();
                    if (updated > 0) {
                        model.setValueAt(newStatus, row, 5);
                        model.setValueAt(newResponse.isEmpty() ? null : newResponse, row, 6);
                        model.setValueAt("Resolved".equals(newStatus) ? currentDate : null, row, 7);
                        JOptionPane.showMessageDialog(dialog, "Request updated successfully!");
                        dialog.dispose();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error updating request: " + ex.getMessage(), 
                                              "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setLocationRelativeTo(AdminHelpdeskView.this);
            dialog.setVisible(true);
        }

        public Object getCellEditorValue() {
            return "Manage Request";
        }
    }

}