package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1522:XE";
    private static final String USER = "YOUR_DB_USERNAME";
    private static final String PASSWORD = "YOUR_DB_PASSWORD";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        

        System.out.println(" Connected to DB URL: " + conn.getMetaData().getURL());
        System.out.println(" Logged in as: " + conn.getMetaData().getUserName());
    
        return conn;
    }
  
}
