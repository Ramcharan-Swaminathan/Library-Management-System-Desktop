package library_management_system;

import db.DBConnection;
import ui.Book_Page;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import library_management_system.ui.AdminFeedbackView;
import library_management_system.ui.AdminHelpdeskView;
import library_management_system.ui.Admin_Option_Page;
import library_management_system.ui.HelpdeskPage;
import library_management_system.ui.Login_Page;
import library_management_system.ui.Main_Page;

public class Library_Main {
    public static void main(String[] args) {
        try {

            Connection conn = DBConnection.getConnection();    
            new Main_Page(conn).setVisible(true);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
