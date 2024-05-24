package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interface for the Screen time data that handles
 * The CRUD operations for the windowinfo class with the database
 */
public interface IScreenTime {
    /**
     *
     * @return This method returns the start time in simple string Date format which correlates to the users most activity detected for the current day
     * @throws SQLException if there's a database access error
     */
    public String Most_Activity_Detected_StartTime() throws SQLException;

    /**
     *
     * @return This method returns the end time in simple string Date format which correlates to the users most activity detected for the current day
     * @throws SQLException if there's a database access error
     */

    public String Most_Activity_Detected_EndTime() throws SQLException;


    /**
     *
     * @return This method returns the start time in simple string Date format which correlates to the users least activity detected for the current day
     * @throws SQLException if there's a database access error
     */
    public String Least_Activity_Detected_StartTime() throws SQLException;

    /**
     *
     * @return This method returns the end time in simple string Date format which correlates to the users  least activity detected for the current day
     * @throws SQLException
     */
    public String Least_Activity_Detected_EndTime() throws SQLException;


    /**
     *
     * @param startTime
     * @param Date
     * @param UserID
     * @param windowTitle
     * This method will simply insert the current screen time data into the db table
     */
    public void InsertScreenTimeData(String startTime, String Date, int UserID, String windowTitle);

    /**
     *
     * @param date
     * @return This method returns a map of window durations such that it contains the durations in seconds corresponding to the active window
     * @throws SQLException
     */
    public Map<String, Map<String, Integer>> FetchWindowDurations(String date) throws SQLException;

    /**
     *
     * @param userId The ID of the current user
     * @return This method is used to return a new screen_time_fields object which contains the current screen time data for a particular user
     */
    public Screen_Time_fields ReturnScreenTimeFields(int userId);


    /**
     *
     * @param userId The ID of the current user
     * @return This method is used to return a new screen_time_fields object which contains the previous screen time data for a particular user
     */
    public Screen_Time_fields ReturnScreenTimeFieldsPrevious(int userId);

    /**
     *
     * @param screenTimeID The ID of the current screen time session
     * @param duration The duration of the current screen time session
     * @param screentitle The title of the currently active window
     * This method as the names suggests updates the screen time data currently stored in the table with the new screen time data as determine by the fields
     */
    public void UpdateScreenTimeData(int screenTimeID, int duration, String screentitle);

    /**
     *
     * @param screenTimeID The ID of the current screen time session
     * @param endTime The end time of the current screen time session
     * @param Duration The duration of the screen time session
     * This method is only called once the user has logged out of the application and thus the screen time tracking session has been concluded in which this method inserts the last obtained screen time data into the table
     */
    public void finalizeScreenTimeData(int screenTimeID, String endTime, int Duration);

    /**
     *
     * @return This method simply returns the last inserted screen_time_ID to be used later in conjunction with another methdo
     * @throws SQLException if there's a database access error

     */
    public int getLastInsertedID() throws SQLException;

    /**
     *
     * @param currentDate The current date
     * @param userID The ID of the current user
     * @return This method simply returns a list of Screen_Time_fields objects for a partiuclar user for a current day
     */
    public List<Screen_Time_fields> getallscreentimeforoneday(String currentDate, int userID);
}
