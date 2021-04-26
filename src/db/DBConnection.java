package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql://";
    private static final String ipAddress = "wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ08dBD";
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;
    private static final String MYSQLJBCDriver = "com.mysql.cj.jdbc.Driver";
    private static final String username = "U08dBD";
    private static final String pass = "53689258768";
    private static Connection conn = null;

    public static Connection startConnection(){
        try{
            Class.forName(MYSQLJBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, pass);
            System.out.println("Connected to DB.");
        }catch(SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(){
        try{
            conn.close();
        }catch(Exception e){

        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
