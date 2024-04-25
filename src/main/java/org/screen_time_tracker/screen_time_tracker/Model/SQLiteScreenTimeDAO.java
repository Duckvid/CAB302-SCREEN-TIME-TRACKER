package org.screen_time_tracker.screen_time_tracker.Model;

import org.screen_time_tracker.screen_time_tracker.Model.User.IUsersDetails;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteScreenTimeDAO implements IUsersDetails {

    private Connection connection;

    public SQLiteScreenTimeDAO(){
        connection = SqliteConnection.getInstance();
        createUsersTable();
        //createScreenTimeTable();
    }

    /**
     * This method is responsible for creating the Users table to store account info
     * It uses a simple SQL query to define a table with a userID, email_Address and password
     */
    private void createUsersTable() {
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


    @Override
    public void UpdatePassword(User currentPassword) {

    }

    @Override
    public void UpdateEmail(User currentEmail) {

    }

    @Override
    public void DeleteAccount(User account) {

    }

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
