package org.screen_time_tracker.screen_time_tracker.Model;

import javafx.scene.control.Alert;
import org.screen_time_tracker.screen_time_tracker.Model.User.IUsersDetails;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages Timers session activities, including enabling notifications and setting timers for how long a user should be active on a screen .
 */
public class SQLiteUserDAO implements IUsersDetails {


    private Connection connection;

    /**
     *
     * @param connection used to establish a connection with the database
     */
    public SQLiteUserDAO(Connection connection){
        this.connection = connection;
        createUsers_And_Screen_Time_Data_Table();
    }

    /**
     * Used later for  tests to create a mock database
     */
    public SQLiteUserDAO(){
        this(SqliteConnection.getInstance());
    }

    /**
     * This method is responsible for creating the Users table to store account info
     * It uses a simple SQL query to define a table with a userID, email_Address and password
     */

    /**
     * This method is also  responsible for creating the Users table to store account info
     * It uses a simple SQL query to define a table with a userID, email_Address and password
     */

    @Override
    public void createUsers_And_Screen_Time_Data_Table() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS Users ("
                    + "Userid INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Name VARCHAR NOT NULL,"
                    + "phone VARCHAR NOT NULL,"
                    + "password VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL,"
                    + "IsLoggedIn BOOLEAN DEFAULT 0"
                    + ")";
            String Query = "CREATE TABLE IF NOT EXISTS ScreenTimeData ("
                    + "ScreenTimeID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Start_Time VARCHAR NOT NULL,"
                    + "End_Time VARCHAR NOT NULL,"
                    + "Duration INTEGER NOT NULL,"
                    + "Date_Of_Track VARCHAR NOT NULL,"
                    + "UserID INT NOT NULL,"
                    + "WindowTitle VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
            statement.execute(Query);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Although not used yet this can be used on the settings page to simply remove the users details as this is also one of our should have user storys
    @Override
    public void DeleteAccount(User account) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Users WHERE Userid = ?");
            statement.setInt(1, account.getUserid());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method handles adding persistency to the application as user data is inserted into the database
    @Override
    public void RegisterAccount(User user) {
        String sql = "INSERT INTO Users (Name, phone, password, email) VALUES (?, ?, ?, ?)";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhonenumber());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setUserid(generatedKeys.getInt(1));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    // This method is not being used yet but become useful at some point it simply returns all the users data
    // Could use this to potentially visualize to the user where their screen time data stacks up against other users
    // As in do they follow the trend of screen time usage or are they an outlier
    @Override
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();

        try{
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Users";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String Name = resultSet.getString("Name");
                String Phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(Name, email, password, Phone);
                users.add(user);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean IsPasswordCorrect(String Password) {
        // check the lenth of the password

        // using regex to check for special character
        String specialChars = "[^A-Za-z0-9]";

        // using regex to check for numbers
        String nums = "[0-9]";

        // using regex to check for capitcal letter
        String Caps = "[A-Z]";

        if(Password.length() < 8) {return false;}

        if (!Password.matches(".*" + specialChars + ".*")) {return false;}

        if (!Password.matches(".*" + nums + ".*")) {return false;}

        if (!Password.matches(".*" + Caps + ".*")) {return false;}

        return true; // password meets all requirments
    }

    @Override
    public boolean IsEmailCorrect(String email) {
        // check that the email has an '@' symbol
        // check that the email has some .prefix as in .com .au .org etc

        String regex = "^(.+)@(.+)$";

        if(!email.matches(regex)){return false;}

        return true;
    }

    // This method is used to verify the users login credentials .
    // As seen by the sql query it first checks if the users email and password exist in the data table then return the user
    // with those credentials this is used in the login controller page
    @Override
    public User Login(String email, String password) {
        String query = "SELECT * FROM Users where email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                // user found with matching credentials
                int userID = resultSet.getInt("Userid");
                String Name = resultSet.getString("Name");
                String Phone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                User user = new User(Name, userEmail, userPassword, Phone);
                user.setUserid(userID);


                String updateQuery = "UPDATE Users SET IsLoggedIn = 1 WHERE Userid = ?";

                // update login status
                try(PreparedStatement updatepstmt = connection.prepareStatement(updateQuery)){
                    updatepstmt.setInt(1, userID);
                    updatepstmt.executeUpdate();
                }
                return user;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    // locate user by email
    public User ForgotPassword(String email) {
        String query = "SELECT * FROM Users where email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, email);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                // user found with matching credentials
                int userID = resultSet.getInt("Userid");
                String Name = resultSet.getString("Name");
                String Phone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");
                User user = new User(Name, userEmail, userPassword, Phone);
                user.setUserid(userID);


                String updateQuery = "UPDATE Users SET IsLoggedIn = 1 WHERE Userid = ?";

                // update login status
                try(PreparedStatement updatepstmt = connection.prepareStatement(updateQuery)){
                    updatepstmt.setInt(1, userID);
                    updatepstmt.executeUpdate();
                }
                return user;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }


    // a method to check if the user is currently logged in or not
    @Override
    public boolean UserExists(User user){
        if (user == null) return false;
        return IsUserLoggedIn(user.getEmail(), user.getPassword());
    }

    // Simplify the login check


    /**
     *
     * @param email of the current user to check if they are logged in
     * @param password  of the current user to check if they are logged in
     * @return true if the user is currently logged in
     */
    public boolean IsUserLoggedIn(String email, String password){
        String query = "SELECT IsLoggedIn FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.next() && resultSet.getBoolean("IsLoggedIn");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void Logout() {
        User currentUser = Session_Manager.getCurrentUser();
        if (currentUser != null) {
            int userId = currentUser.getUserid();
            String updateQuery = "UPDATE Users SET IsLoggedIn = 0 WHERE Userid = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Logout Error");
                alert.setHeaderText("Error Logging Out");
                alert.setContentText("An error occurred while trying to log out. Please try again.");
                alert.showAndWait();
            } finally {
                // Ensure the current user is removed from the session regardless of whether the database update succeeds or fails
                Session_Manager.setCurrentUser(null);
            }
        }
    }



    // Name can not start with any special character

    @Override
    public boolean IsNameValid(String name) {
        if(name.matches(".*"+"^[^A-Za-z0-9]"+".*")){return false;}
        return true;
    }

    @Override
    public boolean IsNumberValid(String Phonenumber){
        if(Phonenumber.matches(".*"+"[A-Za-z]|[^A-Za-z0-9]"+".*")){return false;}
        return true;
    }
}
