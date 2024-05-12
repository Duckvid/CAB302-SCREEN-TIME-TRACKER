package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import java.util.Map;

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
     *
     * @param windowTitle
     * @param timeSpent
     *
     */

    public void updateWindowTime(String windowTitle, long timeSpent);



}
