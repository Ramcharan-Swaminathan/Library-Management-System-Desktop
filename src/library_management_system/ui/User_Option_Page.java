package library_management_system.ui;

import db.DBConnection;
import ui.Book_Page;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import library_management_system.ui.User_Action_Page;
import library_management_system.ui.View_All_User_Actions;
import library_management_system.ui.View_User_Actions;
import library_management_system.ui.FeedbackPage;
public class User_Option_Page extends javax.swing.JFrame {
    private String Current_userId;

    public User_Option_Page(String userID) {
        Current_userId=userID;
        initComponents();
        setTitle("User_Option_Page");
        setSize(540, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jLabel1 = new javax.swing.JLabel();
        Book_Actionbut = new javax.swing.JButton();
        View_Accountbut = new javax.swing.JButton();
        Book_renewalbut = new javax.swing.JButton();
        Request_Bookbut = new javax.swing.JButton();
        Give_Feedbackbut = new javax.swing.JButton();
        Help_Deskbut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Century", 3, 14)); // NOI18N
        jLabel1.setText("Select The  Options!");

        Book_Actionbut.setText("Book_Action");
        Book_Actionbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Book_ActionbutActionPerformed(evt);
            }
        });

        View_Accountbut.setText("View_Account");
        View_Accountbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_AccountbutActionPerformed(evt);
            }
        });

        Book_renewalbut.setText("Book_Renewal");
        Book_renewalbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Book_renewalbutActionPerformed(evt);
            }
        });

        Request_Bookbut.setText("Request_Book");
        Request_Bookbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Request_BookbutActionPerformed(evt);
            }
        });

        Give_Feedbackbut.setText("Give_Feedback");
        Give_Feedbackbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Give_FeedbackbutActionPerformed(evt);
            }
        });

        Help_Deskbut.setText("Help_Desk");
        Help_Deskbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Help_DeskbutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Book_Actionbut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Request_Bookbut, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Give_Feedbackbut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(View_Accountbut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Book_renewalbut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Help_Deskbut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Book_Actionbut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(View_Accountbut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Book_renewalbut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Request_Bookbut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Give_Feedbackbut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Help_Deskbut, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
    private void Book_ActionbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Book_ActionbutActionPerformed
        try {
            Connection conn = DBConnection.getConnection();
            User_Action_Page user_Action_Page=new User_Action_Page(conn, Current_userId);
           openSubWindow(user_Action_Page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_Book_ActionbutActionPerformed

    private void View_AccountbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_View_AccountbutActionPerformed
        // TODO add your handling code here:
                try {
            Connection conn = DBConnection.getConnection();
            View_User_Actions view_User_Actions=new View_User_Actions(conn, Current_userId);
            openSubWindow(view_User_Actions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_View_AccountbutActionPerformed

    private void Book_renewalbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Book_renewalbutActionPerformed
        // TODO add your handling code here:
                try {
            Connection conn = DBConnection.getConnection();
  Renewal_Page renewal_Page = new Renewal_Page(conn, Current_userId);
            openSubWindow(renewal_Page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_Book_renewalbutActionPerformed

    private void Request_BookbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Request_BookbutActionPerformed
        // TODO add your handling code here:
                try {
            Connection conn = DBConnection.getConnection();
            RequestBook_Page requestBook_Page = new RequestBook_Page(conn, Current_userId);
            openSubWindow(requestBook_Page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_Request_BookbutActionPerformed

    private void Give_FeedbackbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Give_FeedbackbutActionPerformed
        // TODO add your handling code here:
                try {
            Connection conn = DBConnection.getConnection();
            FeedbackPage feedbackPage=new FeedbackPage(conn,Current_userId);
            openSubWindow(feedbackPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_Give_FeedbackbutActionPerformed

    private void Help_DeskbutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Help_DeskbutActionPerformed
        // TODO add your handling code here:
                try {
            Connection conn = DBConnection.getConnection();
            HelpdeskPage helpdeskpage=new HelpdeskPage(conn, Current_userId);
            openSubWindow(helpdeskpage);
     
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_Help_DeskbutActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Book_Actionbut;
    private javax.swing.JButton Book_renewalbut;
    private javax.swing.JButton Give_Feedbackbut;
    private javax.swing.JButton Help_Deskbut;
    private javax.swing.JButton Request_Bookbut;
    private javax.swing.JButton View_Accountbut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollBar jScrollBar1;
    // End of variables declaration//GEN-END:variables
}
