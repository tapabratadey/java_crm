package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection configuration
 */

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

    /**
     * Starts the DB connection
     * @return the session
     */
    public static Connection startConnection(){
        try{
            Class.forName(MYSQLJBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, pass);
            /**
             * LAMBDA EXPRESSION
             * Justification: I chose to use Lambda Expressing here because
             * it let me use thread instance's start method more compactly
             * in one liner. Or else I would have had to write it as:
             * Thread thread = new Thread(){
             *     public void run(){
             *       System.out.println("Thread Running");
             *     }
             *   }
             * thread.start();
             */
            new Thread(() -> System.out.println("Connected to DB!")).start();
        }catch(SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Closes the DB connection
     * LAMBDA EXPRESSION
     * Justification: I chose to use LAMBDA expression (e.getMessage) to print
     * the Exception. It will get the description of the exception.
     *
     */
    //
    public static void closeConnection(){
        try{
            conn.close();
        }catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }

    /**
     * Gets the session
     * @return
     */
    public static Connection getConnection() {
        return conn;
    }
}
