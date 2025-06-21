package library_management_system.ui;

import db.DBConnection;
import ui.Book_Page;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import library_management_system.ui.User_Action_Page;
import library_management_system.ui.User_Page;
import library_management_system.ui.View_All_User_Actions;
import library_management_system.ui.View_User_Actions;
import library_management_system.ui.FeedbackPage;

public class Admin_Option_Page extends javax.swing.JFrame {
private String Current_AdminId;
    public Admin_Option_Page(String adminId) {
        Current_AdminId=adminId;
        initComponents();
        setTitle("Admin_Option_Page");
        setSize(540, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollBar2 = new javax.swing.JScrollBar();
        view_RequestBook_but = new javax.swing.JButton();
        View_Transaction_but = new javax.swing.JButton();
        Book_Actions_but = new javax.swing.JButton();
        BookTransaction_but = new javax.swing.JButton();
        View_Feedback_but = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        User_Action_but = new javax.swing.JButton();
        View_Helpdesk_but=new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        view_RequestBook_but.setText("View Request Book");
        view_RequestBook_but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_RequestBook_butActionPerformed(evt);
            }
        });

        View_Transaction_but.setText("View Transaction");
        View_Transaction_but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_Transaction_butActionPerformed(evt);
            }
        });

        Book_Actions_but.setText("Book Actions");
        Book_Actions_but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Book_Actions_butActionPerformed(evt);
            }
        });

        BookTransaction_but.setText("Book Transaction");
        BookTransaction_but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BookTransaction_butActionPerformed(evt);
            }
        });

        View_Feedback_but.setText("View Feedback");
        View_Feedback_but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_Feedback_butActionPerformed(evt);
            }
        });
        
        View_Helpdesk_but.setText("View Helpdesk Request");
        View_Helpdesk_but.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        View_Helpdesk_butActionPerformed(evt);
    }
});

        jLabel1.setFont(new java.awt.Font("Century", 3, 14)); 
        jLabel1.setText("Select The Options!");

        User_Action_but.setText("User Actions");
        User_Action_but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                User_Action_butActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
       layout.setHorizontalGroup(
    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(Book_Actions_but, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addComponent(View_Transaction_but, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
        .addGap(30, 30, 30)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(User_Action_but, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addComponent(View_Feedback_but, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
        .addGap(30, 30, 30)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(BookTransaction_but, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
            .addComponent(view_RequestBook_but, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
        .addContainerGap(30, Short.MAX_VALUE))
    .addGroup(layout.createSequentialGroup()
        .addGap(160, 160, 160)
        .addComponent(jLabel1)
        .addGap(0, 0, Short.MAX_VALUE))
    .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(View_Helpdesk_but, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
);

layout.setVerticalGroup(
    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(layout.createSequentialGroup()
        .addGap(24, 24, 24)
        .addComponent(jLabel1)
        .addGap(32, 32, 32)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(Book_Actions_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(BookTransaction_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(User_Action_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(50, 50, 50)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(View_Feedback_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(view_RequestBook_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(View_Transaction_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(50, 50, 50)
        .addComponent(View_Helpdesk_but, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(30, Short.MAX_VALUE))
);

        pack();
    }// </editor-fold>

    private void openSubWindow(JFrame subWindow) {
    this.setVisible(false); // Hide the admin page

    subWindow.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent e) {
            setVisible(true); // Show admin page again when sub-window is closed
        }
    });

    subWindow.setVisible(true);
}

    private void Book_Actions_butActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection conn = DBConnection.getConnection();
        Book_Page bookPage = new Book_Page(conn);
        openSubWindow(bookPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void User_Action_butActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection conn = DBConnection.getConnection();
         User_Page userPage = new User_Page(conn);
        openSubWindow(userPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BookTransaction_butActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection conn = DBConnection.getConnection();
          User_Action_Page actionPage = new User_Action_Page(conn, Current_AdminId);
        openSubWindow(actionPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void View_Transaction_butActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection conn = DBConnection.getConnection();
        View_All_User_Actions viewPage = new View_All_User_Actions(conn);
        openSubWindow(viewPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void View_Feedback_butActionPerformed(java.awt.event.ActionEvent evt) {
        try {

Connection conn = DBConnection.getConnection();
        AdminFeedbackView view = new AdminFeedbackView(conn);
        openSubWindow(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void view_RequestBook_butActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection conn = DBConnection.getConnection();
        AdminRequestBookView view = new AdminRequestBookView(conn);
        openSubWindow(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void View_Helpdesk_butActionPerformed(java.awt.event.ActionEvent evt) {                                                 
    try {
        Connection conn = DBConnection.getConnection();
        AdminHelpdeskView helpdeskView = new AdminHelpdeskView(conn);
        openSubWindow(helpdeskView);
    } catch (Exception e) {
        e.printStackTrace();
    }
}  



    // Variables declaration - do not modify
    private javax.swing.JButton BookTransaction_but;
    private javax.swing.JButton Book_Actions_but;
    private javax.swing.JButton User_Action_but;
    private javax.swing.JButton View_Feedback_but;
    private javax.swing.JButton View_Transaction_but;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollBar jScrollBar2;
    private javax.swing.JButton view_RequestBook_but;
    private javax.swing.JButton View_Helpdesk_but;
    // End of variables declaration
}
