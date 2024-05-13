package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;


import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


/**
 * This class handles dtabase operations related to screen time data
 * Implementing methods defined in the IScreenTime interface
 */


public class SQliteScreen_Timedata implements IScreenTime{
    private final Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");


    public SQliteScreen_Timedata() throws SQLException {

    }


    // this is a method to insert screen time data to the screen timetable

    // it stores data for only the user logged in which is useful for later methods

    @Override
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

    @Override
    public Map<String, Map<String, Integer>> FetchWindowDurations(String date) throws SQLException {
        Map<String, Map<String, Integer>> durationsByHour = new HashMap<>();
        String query = "SELECT Start_Time, WindowTitle, SUM(Duration) AS TotalDuration " +
                "FROM ScreenTimeData WHERE Date_Of_Track = ? " +
                "GROUP BY Start_Time, WindowTitle";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String startTime = rs.getString("Start_Time");
                String windowTitle = rs.getString("WindowTitle");
                int totalDuration = rs.getInt("TotalDuration");

                String hour = convertTo24HourFormat(startTime).substring(0, 2) + ":00"; // Convert and extract hour

                Map<String, Integer> hourMap = durationsByHour.computeIfAbsent(hour, k -> new HashMap<>());
                hourMap.put(windowTitle, totalDuration);
            }
        }
        return durationsByHour;
    }


    /**
     * Converts a 12-hour formatted time string to 24-hour format.
     *
     * @param twelveHourTime The time string in 12-hour format (e.g., "02:00 PM").
     * @return The converted time string in 24-hour format (e.g., "14:00").
     */

    public static String convertTo24HourFormat(String twelveHourTime) {
        // Define the 12-hour format
        SimpleDateFormat twelveHourFormat = new SimpleDateFormat("hh:mm a");
        // Define the 24-hour format
        SimpleDateFormat twentyFourHourFormat = new SimpleDateFormat("HH:mm");

        try {
            // Parse the 12-hour format and format it to 24-hour format
            return twentyFourHourFormat.format(twelveHourFormat.parse(twelveHourTime));
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if there's a parsing error
        }
    }

    // this method returns a screen time fields object
    // it only returns the most recently added object this useful to only obtain the most current session data
    // rather than the earliest
    @Override
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

    /**
     * Calculates the median time from a list of time strings.
     *
     * @param times A list of time strings.
     * @return The median time as a string.
     */
    public String calculateMedianTime(List<String> times) {
        Collections.sort(times);
        int middle = times.size() / 2;
        if (times.size() % 2 == 1) {
            return times.get(middle);
        } else {
            // Assume the time format is HH:mm and we need to average two times
            LocalTime time1 = LocalTime.parse(times.get(middle - 1), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime time2 = LocalTime.parse(times.get(middle), DateTimeFormatter.ofPattern("HH:mm"));
            long seconds = (time1.toSecondOfDay() + time2.toSecondOfDay()) / 2;
            return LocalTime.ofSecondOfDay(seconds).toString();
        }
    }

    /**
     * Retrieves the median start time for a specific user.
     *
     * @param userId The user ID for which to retrieve the median start time.
     * @return The median start time as a string.
     * @throws SQLException if a database access error occurs.
     */

    public String getMedianStartTime(int userId) throws SQLException {
        List<String> startTimes = new ArrayList<>();
        String query = "SELECT Start_Time FROM ScreenTimeData WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                startTimes.add(resultSet.getString("Start_Time"));
            }
        }
        return calculateMedianTime(startTimes);
    }


    /**
     * Retrieves the median end time for a specific user.
     *
     * @param userId The user ID for which to retrieve the median end time.
     * @return The median end time as a string.
     * @throws SQLException if a database access error occurs.
     */

    public String getMedianEndTime(int userId) throws SQLException {
        List<String> endTimes = new ArrayList<>();
        String query = "SELECT End_Time FROM ScreenTimeData WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                endTimes.add(resultSet.getString("End_Time"));
            }
        }
        return calculateMedianTime(endTimes);
    }

    // this method returns a screen time fields object
    // it only returns the previously added object this is useful to only obtain the most current session data
    // rather than the earliest
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
