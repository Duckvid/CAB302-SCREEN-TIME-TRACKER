package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;


import javafx.scene.chart.XYChart;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SQliteScreen_Timedata implements IScreenTime{
    private final Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");

    public SQliteScreen_Timedata() throws SQLException {

    }


    // this is a method to insert screen time data to the screen timetable

    // it stores data for only the user logged in which is useful for later methods
    public void InsertScreenTimeData(String startTime, String Date, int UserID, String windowTitle){
        String query = "INSERT INTO ScreenTimeData (Start_Time, End_Time, Duration, Date_Of_Track, Userid, WindowTitle) VALUES (?, '', 0, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){

            pstmt.setString(1, startTime);
            pstmt.setString(2, Date);
            pstmt.setInt(3, UserID);
            pstmt.setString(4, windowTitle);
            pstmt.executeUpdate();
        }
        //catch any exceptions or errors in the insertion process
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Map<String, Integer> FetchWindowDurations(String date) throws SQLException {
        Map<String, Integer> durations = new HashMap<>();
        String query = "SELECT WindowTitle, SUM(Duration) AS TotalDuration FROM ScreenTimeData WHERE Date_Of_Track = ? GROUP BY WindowTitle";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                durations.put(rs.getString("WindowTitle"), rs.getInt("TotalDuration"));
            }
        }
        return durations;
    }

    // this method returns a screen time fields object
    // it only returns the most recently added object this useful to only obtain the most current session data
    // rather than the earliest
    public Screen_Time_fields ReturnScreenTimeFields(int userId){

        String query = "SELECT * FROM ScreenTimeData WHERE Userid = ? ORDER BY ScreenTimeID DESC LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int ScreenTimeID = resultSet.getInt("ScreenTimeID");
                String Start_Time = resultSet.getString("Start_Time");
                String End_Time = resultSet.getString("End_Time");
                int Duration = resultSet.getInt("Duration");
                String Date_Of_Track = resultSet.getString("Date_Of_Track");
                return new Screen_Time_fields(ScreenTimeID, Start_Time, End_Time, Duration, Date_Of_Track);

            }
        }
        //catch any exceptions or errors in the insertion process
        catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    // this method returns a screen time fields object
    // it only returns the previously added object this is useful to only obtain the most current session data
    // rather than the earliest
    public Screen_Time_fields ReturnScreenTimeFieldsPrevious(int userId) {

        String query = "SELECT * FROM ScreenTimeData WHERE Userid = ? ORDER BY ScreenTimeID DESC LIMIT 1 OFFSET 1";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int ScreenTimeID = resultSet.getInt("ScreenTimeID");
                String Start_Time = resultSet.getString("Start_Time");
                String End_Time = resultSet.getString("End_Time");
                int Duration = resultSet.getInt("Duration");
                String Date_Of_Track = resultSet.getString("Date_Of_Track");
                return new Screen_Time_fields(ScreenTimeID, Start_Time, End_Time, Duration, Date_Of_Track);

            }
        }
        //catch any exceptions or errors in the insertion process
        catch(SQLException e){
            e.printStackTrace();
        }

        return null;

    }

    public void UpdateScreenTimeData(int screenTimeID, int duration, String screentitle){
        String query = "UPDATE ScreenTimeData SET Duration = ?, WindowTitle = ? WHERE ScreenTimeID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)){
            pstmt.setInt(1, duration);
            pstmt.setString(2, screentitle);
            pstmt.setInt(3, screenTimeID);
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
    // rather than calculating averages and medians for the all the users screen time data
    // returns the last screen time data as a list of screen time fields
    public List<Screen_Time_fields> getallscreentimeforoneday(String currentDate, int userID) {
        List<Screen_Time_fields> screenTimeFields = new ArrayList<>();
        String query = "SELECT * FROM ScreenTimeData WHERE Date_Of_Track = ? AND Userid = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, currentDate);
            pstmt.setInt(2, userID);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int ScreenTimeID = rs.getInt("ScreenTimeID");
                String StartTime = rs.getString("Start_Time");
                String EndTime = rs.getString("End_Time");
                int Duration = rs.getInt("Duration");
                String dateOfTrack = rs.getString("Date_Of_Track");
                Screen_Time_fields Screentimedata = new Screen_Time_fields(ScreenTimeID, StartTime, EndTime, Duration, dateOfTrack);
                //System.out.println("ScreenTimeID: " + " " +  ScreenTimeID + "Date_Of_Track:" + dateOfTrack + "StartTime" + StartTime + "End TIme" + EndTime +  "Duration" + Duration);
                screenTimeFields.add(Screentimedata);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return screenTimeFields;

    }
    
    @Override
    public void Calculate_Median_Start_time() {

    }

    @Override
    public void Calculate_Median_End_time() {

    }

    @Override
    public int Most_Activity_Detected_Duration() throws SQLException {
        // compare the durations of the screen tracking sessions over the past 24hrs

        // initialize a variable to store the greatest duration
        int maxDuration = -1;

        // initialize a list to be used later
        List<Screen_Time_fields> field24hr = new ArrayList<>();

        // first need to extract all data from past 24hrs for current user
        // get the current user
        if(Session_Manager.isUserLoggedIn()) {
            int UserID = Session_Manager.getCurrentUser().getUserid();

            // get the current date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);

            // create screen time data object
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            // create a list of all screen time data from past 24hrs
            field24hr = sQliteScreenTimeData.getallscreentimeforoneday(strDate, UserID);

            // find the greatest duration
            // iterate through the list
            for(Screen_Time_fields screentimeRecord : field24hr){
                if(screentimeRecord.getDuration() > maxDuration) {
                    maxDuration = screentimeRecord.getDuration();
                }
            }

        }

        return maxDuration;
        
        // Done
    }

    @Override
    public String Most_Activity_Detected_StartTime() throws SQLException {
        // initialize a variable to store the greatest duration
        int maxDuration = -1;
        String maxStartTime = "";
        // initialize a list to be used later
        List<Screen_Time_fields> field24hr = new ArrayList<>();

        // first need to extract all data from past 24hrs for current user
        // get the current user
        if(Session_Manager.isUserLoggedIn()) {
            int UserID = Session_Manager.getCurrentUser().getUserid();

            // get the current date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);

            // create screen time data object
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields maxScreentimeRecord = null;

            // create a list of all screen time data from past 24hrs
            field24hr = sQliteScreenTimeData.getallscreentimeforoneday(strDate, UserID);

            // find the greatest duration
            // iterate through the list
            for(Screen_Time_fields screentimeRecord : field24hr){
                if(screentimeRecord.getDuration() > maxDuration) {
                    maxDuration = screentimeRecord.getDuration();
                    maxScreentimeRecord = screentimeRecord;
                }
            }

            if(maxScreentimeRecord != null){
                maxStartTime = maxScreentimeRecord.getStart_time();
            }
        }

        return maxStartTime;
    }

    @Override
    public String Most_Activity_Detected_EndTime() throws SQLException {
        // initialize a variable to store the greatest duration
        int maxDuration = -1;
        String maxEndTime = "";
        // initialize a list to be used later
        List<Screen_Time_fields> field24hr = new ArrayList<>();

        // first need to extract all data from past 24hrs for current user
        // get the current user
        if(Session_Manager.isUserLoggedIn()) {
            int UserID = Session_Manager.getCurrentUser().getUserid();

            // get the current date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);

            // create screen time data object
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields maxScreentimeRecord = null;

            // create a list of all screen time data from past 24hrs
            field24hr = sQliteScreenTimeData.getallscreentimeforoneday(strDate, UserID);

            // find the greatest duration
            // iterate through the list
            for(Screen_Time_fields screentimeRecord : field24hr){
                if(screentimeRecord.getDuration() > maxDuration) {
                    maxDuration = screentimeRecord.getDuration();
                    maxScreentimeRecord = screentimeRecord;
                }
            }

            if(maxScreentimeRecord != null){
                maxEndTime = maxScreentimeRecord.getEnd_time();
            }
        }

        return maxEndTime;
    }


    @Override
    public int Least_Activity_Detected() throws SQLException{
        // compare the durations of the screen tracking sessions over the past 24hrs

        // initialize a variable to store the Smallest duration
        int LeastDuration = -1;

        // initialize a list to be used later
        List<Screen_Time_fields> field24hr = new ArrayList<>();

        // first need to extract all data from past 24hrs for current user
        // get the current user
        if(Session_Manager.isUserLoggedIn()) {
            int UserID = Session_Manager.getCurrentUser().getUserid();

            // get the current date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);

            // create screen time data object
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            // create a list of all screen time data from past 24hrs
            field24hr = sQliteScreenTimeData.getallscreentimeforoneday(strDate, UserID);

            // find the greatest duration
            // iterate through the list
            for(Screen_Time_fields screentimeRecord : field24hr){
                if(screentimeRecord.getDuration() < LeastDuration) {
                    LeastDuration = screentimeRecord.getDuration();
                }
            }

        }

        return LeastDuration;

        // Done
    }

    @Override
    public String Least_Activity_Detected_StartTime() throws SQLException {
        // initialize a variable to store the greatest duration
        int LeastDuration = -1;
        String SmallestStartTime = "";
        // initialize a list to be used later
        List<Screen_Time_fields> field24hr = new ArrayList<>();

        // first need to extract all data from past 24hrs for current user
        // get the current user
        if(Session_Manager.isUserLoggedIn()) {
            int UserID = Session_Manager.getCurrentUser().getUserid();

            // get the current date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);

            // create screen time data object
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields maxScreentimeRecord = null;

            // create a list of all screen time data from past 24hrs
            field24hr = sQliteScreenTimeData.getallscreentimeforoneday(strDate, UserID);

            // find the greatest duration
            // iterate through the list
            for(Screen_Time_fields screentimeRecord : field24hr){
                if(screentimeRecord.getDuration() > LeastDuration) {
                    LeastDuration = screentimeRecord.getDuration();
                    maxScreentimeRecord = screentimeRecord;
                }
            }

            if(maxScreentimeRecord != null){
                SmallestStartTime = maxScreentimeRecord.getStart_time();
            }
        }

        return SmallestStartTime;
    }

    @Override
    public String Least_Activity_Detected_EndTime() throws SQLException {
        // initialize a variable to store the greatest duration
        int LeastDuration = -1;
        String SmallestEndTime = "";
        // initialize a list to be used later
        List<Screen_Time_fields> field24hr = new ArrayList<>();

        // first need to extract all data from past 24hrs for current user
        // get the current user
        if(Session_Manager.isUserLoggedIn()) {
            int UserID = Session_Manager.getCurrentUser().getUserid();

            // get the current date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);

            // create screen time data object
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields maxScreentimeRecord = null;

            // create a list of all screen time data from past 24hrs
            field24hr = sQliteScreenTimeData.getallscreentimeforoneday(strDate, UserID);

            // find the greatest duration
            // iterate through the list
            for(Screen_Time_fields screentimeRecord : field24hr){
                if(screentimeRecord.getDuration() > LeastDuration) {
                    LeastDuration = screentimeRecord.getDuration();
                    maxScreentimeRecord = screentimeRecord;
                }
            }

            if(maxScreentimeRecord != null){
                SmallestEndTime = maxScreentimeRecord.getEnd_time();
            }
        }

        return SmallestEndTime;
    }

}