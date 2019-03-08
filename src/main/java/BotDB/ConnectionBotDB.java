package BotDB;
import com.mysql.jdbc.Driver;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBotDB {
    public static final String URL = "jdbc:mysql://localhost:3306/bot_db?autoReconnect=true&useSSL=false";
    public static final String USER = "root";
    public static final String PASS = "danil123";
    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection()
    {
        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }

    /*
    public static void main(String[] args) {
        //
        UserDBManager udb = new UserDBManager();

        //udb.updateUser(new User("", "aaa", "+4t34werer"));

        System.out.println(udb.getAllUsers());
        System.out.println(udb.userExists("11"));


    }
*/


}