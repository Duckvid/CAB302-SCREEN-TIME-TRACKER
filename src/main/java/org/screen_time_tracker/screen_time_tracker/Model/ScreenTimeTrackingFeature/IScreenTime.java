package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;
import org.screen_time_tracker.screen_time_tracker.Model.User.IUsersDetails;

import java.util.List;

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

    public void Most_Activity_Detected();



}
