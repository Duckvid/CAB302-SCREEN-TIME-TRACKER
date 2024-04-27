package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import java.sql.*;

public class SQlite_Screen_Time_data {
    private Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");

    public SQlite_Screen_Time_data() throws SQLException {

    }

    public void InsertScreenTimeData(String startTime){
        String query = "INSERT INTO ScreenTimeData (Start_Time, End_Time, Duration) VALUES (?, '', 0)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, startTime);
            pstmt.executeUpdate();
        }
        //catch any exceptions or errors in the insertion process
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void UpdateScreenTimeData(int screenTimeID, int duration){
        String query = "UPDATE ScreenTimeData SET Duration = ? WHERE ScreenTimeID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setInt(1, duration);
            pstmt.setInt(2, screenTimeID);
            pstmt.executeUpdate();
        }
        //catch any exceptions or errors in the insertion process
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void finalizeScreenTimeData(int screenTimeID, String endTime, int Duration){
        String query = "UPDATE ScreenTimeData SET End_Time = ?, Duration = ? WHERE ScreenTimeID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setString(1, endTime);
            pstmt.setInt(2, Duration);
            pstmt.setInt(3, screenTimeID);
            pstmt.executeUpdate();
        }
        //catch any exceptions or errors in the insertion process
        catch(SQLException e){
            e.printStackTrace();
        }
    }



    public int getLastInsertedID() throws SQLException {
        String query = "SELECT last_insert_rowid() AS id";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()){
            if(resultSet.next()){
                return resultSet.getInt("id");

            }else{
                return -1;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
    }




}
