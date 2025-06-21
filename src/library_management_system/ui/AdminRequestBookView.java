package library_management_system.ui;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;

public class AdminRequestBookView extends JFrame {
    private Connection conn;
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public AdminRequestBookView(Connection connection) {
        this.conn = connection;
        initComponents();
        setTitle("Admin - View Book Requests");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        
        String[] columnNames = {"Book ID", "Title", "Author", "Request Type", "Request Date", 
                              "Response Date", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        
        requestTable = new JTable(tableModel);
        requestTable.setRowHeight(30);
        requestTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
        requestTable.getColumnModel().getColumn(2).setPreferredWidth(150); 
        
       
        requestTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        requestTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), conn, tableModel));
        
        JScrollPane scrollPane = new JScrollPane(requestTable);
        
     
        mainPanel.add(new JLabel("Book Request Management", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
      
        JButton refreshButton = new JButton("Refresh Requests");
        refreshButton.addActionListener(e -> loadRequestData());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
       
        loadRequestData();
    }

    private void loadRequestData() {
        try {
           
            tableModel.setRowCount(0);
            
            String sql = "SELECT r.BookID, d.Title, d.Author, r.RequestType, " +
                         "r.RequestedDate, r.ResponseDate, r.RequestStatus " +
                         "FROM RequestBook r " +
                         "JOIN RequestBookWithDetails d ON r.BookID = d.BookID " +
                         "AND r.RequestedDate = d.RequestDate " +
                         "ORDER BY r.RequestedDate DESC";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String bookId = rs.getString("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String requestType = rs.getString("RequestType");
                Date requestDate = rs.getDate("RequestedDate");
                Date responseDate = rs.getDate("ResponseDate");
                String status = rs.getString("RequestStatus");
                
                Object[] rowData = {
                    bookId,
                    title,
                    author,
                    requestType,
                    requestDate,
                    responseDate,
                    status,
                    "Manage Request"
                };
                
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading requests: " + ex.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

   
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

   
    class ButtonEditor extends DefaultCellEditor {
        private String currentBookId;
        private Date currentRequestDate;
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
            
            currentBookId = (String) table.getModel().getValueAt(row, 0);
            currentRequestDate = (Date) table.getModel().getValueAt(row, 4);
            
            JButton button = new JButton();
            button.setText(value == null ? "" : value.toString());
            button.addActionListener(e -> {
                showRequestManagementDialog(row);
                fireEditingStopped();
            });
            
            return button;
        }

        private void showRequestManagementDialog(int row) {
            JDialog dialog = new JDialog(AdminRequestBookView.this, "Manage Book Request", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 250);
            dialog.setLocationRelativeTo(AdminRequestBookView.this);
            
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
           
            formPanel.add(new JLabel("Status:"));
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Approved"});
            String currentStatus = (String) model.getValueAt(row, 6);
            statusCombo.setSelectedItem(currentStatus);
            formPanel.add(statusCombo);
            
       
            formPanel.add(new JLabel("Response Date:"));
            JTextField dateField = new JTextField();
            Date currentResponseDate = (Date) model.getValueAt(row, 5);
            if (currentResponseDate != null) {
                dateField.setText(currentResponseDate.toString());
            }
            formPanel.add(dateField);
            
         
            JButton currentDateButton = new JButton("Set to Today");
            currentDateButton.addActionListener(e -> 
                dateField.setText(Date.valueOf(LocalDate.now()).toString()));
            formPanel.add(currentDateButton);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
           
            JPanel buttonPanel = new JPanel();
            JButton saveButton = new JButton("Save Changes");
            saveButton.addActionListener(e -> {
                try {
                    String newStatus = (String) statusCombo.getSelectedItem();
                    Date responseDate = dateField.getText().isEmpty() ? null : 
                                      Date.valueOf(dateField.getText());
                    
                    String sql = "UPDATE RequestBook SET RequestStatus = ?, ResponseDate = ? " +
                                "WHERE BookID = ? AND RequestedDate = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, newStatus);
                    pstmt.setDate(2, responseDate);
                    pstmt.setString(3, currentBookId);
                    pstmt.setDate(4, currentRequestDate);
                    
                    int updated = pstmt.executeUpdate();
                    if (updated > 0) {
                      
                        model.setValueAt(newStatus, row, 6);
                        model.setValueAt(responseDate, row, 5);
                        JOptionPane.showMessageDialog(dialog, "Request updated successfully!");
                        dialog.dispose();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error updating request: " + ex.getMessage(), 
                                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD", 
                                                "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }

        public Object getCellEditorValue() {
            return "";
        }
    }

}