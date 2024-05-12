package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import java.util.Map;

/**
 * Interface for the Screen time data that handles
 * The CRUD operations for the Screen_time_tracking_feature class with the database
 */
public interface IScreenTimeTracking {

    /**
     *
     * @return This method obtains the title of the current active application from the users machine then returns this
     */
    public String getActiveWindowTitle();


    /**
     *
     * @return This method simply returns the current date and time
     */
    public String CurrentDateTime();

    /**
     *
     * @return This method returns which window is in use and how long that window has been in use for
     */
    public Map<String, Long> getWindowTimeMap();

    /**
     * Updates the currently active window
     * @param windowTitle The title of the currently active window
     * @param timeSpent The duration of the time spent at the current window
     *
     */

    public void updateWindowTime(String windowTitle, long timeSpent);



}
