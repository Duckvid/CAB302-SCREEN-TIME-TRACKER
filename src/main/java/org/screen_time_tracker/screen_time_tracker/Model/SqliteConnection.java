package org.screen_time_tracker.screen_time_tracker.Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is responsible for establishing a connection with the database
 */
public class SqliteConnection {


    private static Connection instance = null;



    // This is establishing a connection to the database and creating an instance of this connection
    // If there is no database by that name it will create a new one

    /**
     * This is establishing a connection to the database and creating an instance of this connection
     * If there is no database by that name it will create a new one
     */
    private SqliteConnection(){
        String url = "jdbc:sqlite:ScreenTimeTracker.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     *
     * @return The instance of the current connection
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }

}
