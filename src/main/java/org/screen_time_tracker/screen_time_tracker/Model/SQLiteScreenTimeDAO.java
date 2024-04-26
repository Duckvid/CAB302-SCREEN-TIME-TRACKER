package org.screen_time_tracker.screen_time_tracker.Model;

import org.screen_time_tracker.screen_time_tracker.Model.User.IUsersDetails;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteScreenTimeDAO implements IUsersDetails {

    private Connection connection;

    public SQLiteScreenTimeDAO(Connection connection){
        this.connection = connection;
        createUsersTable();
        //createScreenTimeTable();
    }

    public SQLiteScreenTimeDAO(){
        this(SqliteConnection.getInstance());
    }

    /**
     * This method is responsible for creating the Users table to store account info
     * It uses a simple SQL query to define a table with a userID, email_Address and password
     */
    public void createUsersTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS Users ("
                    + "Userid INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "Name VARCHAR NOT NULL,"
                    + "phone VARCHAR NOT NULL,"
                    + "password VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for creating the Users table to store account info
     * It uses a simple SQL query to define a table with a userID, email_Address and password
     *//*private void createScreenTimeTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS Users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "password VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    // this is just a useful method for testing
    public void ClearUsersTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE * FROM Users");
        statement.executeUpdate();
    }

    @Override
    public void UpdatePassword(String currentPassword) {

    }

    @Override
    public void UpdateEmail(String currentEmail) {

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
                int UserID = resultSet.getInt("Userid");
                String Name = resultSet.getString("Name");
                String Phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(Name, email, password, Phone);
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
        String specialChars = "[!$#.]";

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
                String Name = resultSet.getString("Name");
                String Phone = resultSet.getString("phone");
                String userEmail = resultSet.getString("email");
                String userPassword = resultSet.getString("password");

                return new User(Name, userEmail, userPassword, Phone);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }


}
