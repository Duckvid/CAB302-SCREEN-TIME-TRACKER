package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;
import org.screen_time_tracker.screen_time_tracker.Model.User.IUsersDetails;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interface for the Screen time data that handles
 * The CRUD operations for the windowinfo class with the database
 */
public interface IScreenTime {

    /**
     * This method will likely be used for the recommendations page to calculate the median start time of a user
     * I haven't determined what will be the parameter but I assume something like a list of start times
     */
    public void Calculate_Median_Start_time();

    /**
     * This method will likely be used for the recommendations page to calculate the median End time of a user
     * I haven't determined what will be the parameter but I assume something like a list of End times
     */
    public void Calculate_Median_End_time();

    /**
     *
     * @return This method returns the start time in simple string Date format which correlates to the users most activity detected for the current day
     * @throws SQLException
     */
    public String Most_Activity_Detected_StartTime() throws SQLException;

    /**
     *
     * @return This method returns the end time in simple string Date format which correlates to the users most activity detected for the current day
     * @throws SQLException
     */

    public String Most_Activity_Detected_EndTime() throws SQLException;


    /**
     *
     * @return This method returns the start time in simple string Date format which correlates to the users least activity detected for the current day
     * @throws SQLException
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
     * @param userId
     * @return This method is used to return a new screen_time_fields object which contains the current screen time data for a particular user
     */
    public Screen_Time_fields ReturnScreenTimeFields(int userId);


    /**
     *
     * @param userId
     * @return This method is used to return a new screen_time_fields object which contains the previous screen time data for a particular user
     */
    public Screen_Time_fields ReturnScreenTimeFieldsPrevious(int userId);

    /**
     *
     * @param screenTimeID
     * @param duration
     * @param screentitle
     * This method as the names suggests updates the screen time data currently stored in the table with the new screen time data as determine by the fields
     */
    public void UpdateScreenTimeData(int screenTimeID, int duration, String screentitle);

    /**
     *
     * @param screenTimeID
     * @param endTime
     * @param Duration
     * This method is only called once the user has logged out of the application and thus the screen time tracking session has been concluded in which this method inserts the last obtained screen time data into the table
     */
    public void finalizeScreenTimeData(int screenTimeID, String endTime, int Duration);

    /**
     *
     * @return This method simply returns the last inserted screen_time_ID to be used later in conjunction with another methdo
     * @throws SQLException
     */
    public int getLastInsertedID() throws SQLException;

    /**
     *
     * @param currentDate
     * @param userID
     * @return This method simply returns a list of Screen_Time_fields objects for a partiuclar user for a current day
     * @return This is used to determine which session out the many that the user may have started throughout the day contains the most active duration, least active etc.
     */
    public List<Screen_Time_fields> getallscreentimeforoneday(String currentDate, int userID);
}
