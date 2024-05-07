package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQlite_Screen_Time_data implements IScreenTime{
    private Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");

    public SQlite_Screen_Time_data() throws SQLException {

    }


    // this is a method to insert screen time data to the screen time table

    // it stores data for only the user logged in which is useful for later methods
    public void InsertScreenTimeData(String startTime, String Date, int UserID){
        String query = "INSERT INTO ScreenTimeData (Start_Time, End_Time, Duration, Date_Of_Track, Userid) VALUES (?, '', 0, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){

            pstmt.setString(1, startTime);
            pstmt.setString(2, Date);
            pstmt.setInt(3, UserID);
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


    // this method gets all the screen time data from the past 24hrs
    // this is useful for calculating averages and whatnot for just one day
    // rather than calculating avergaes and medians for the all the users screen time data
    // returns the last screentime data as a list of screentime fields
    public List<Screen_Time_fields> getallscreentimeforoneday(String currentDate) {
        List<Screen_Time_fields> screenTimeFields = new ArrayList<>();
        String query = "SELECT * FROM ScreenTimeData WHERE Date_Of_Track = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, currentDate);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int ScreenTimeID = rs.getInt("ScreenTimeID");
                String StartTime = rs.getString("Start_Time");
                String EndTime = rs.getString("End_Time");
                int Duration = rs.getInt("Duration");
                String dateOfTrack = rs.getString("Date_Of_Track");
                Screen_Time_fields Screentimedata = new Screen_Time_fields(ScreenTimeID, StartTime, EndTime, Duration, dateOfTrack);
                //System.out.println("ScreenTimeID: " + ScreenTimeID + "Date_Of_Track:" + dateOfTrack + "StartTime" + StartTime + "End TIme" + EndTime +  "Duration" + Duration);
                screenTimeFields.add(Screentimedata);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return screenTimeFields;

    }

    public void accessallScreenTimeDataForPast24hrs(){

        List<Screen_Time_fields> screentimedata;
        java.util.Date currentDate = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatDate.format(currentDate);
        screentimedata = getallscreentimeforoneday(strDate);

        for(Screen_Time_fields screenTimeData : screentimedata){
            System.out.println(screenTimeData.Start_time);
        }
    }


    @Override
    public void Calculate_Median_Start_time() {

    }

    @Override
    public void Calculate_Median_End_time() {

    }

    @Override
    public void Most_Activity_Detected() {

    }

    @Override
    public void RecommendedBreak() {

    }

    @Override
    public void Least_Activity_Detected() {

    }

    @Override
    public void Recommended_end() {

    }
}
