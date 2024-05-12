package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import java.util.List;

/**
 * Represents various screen time tracking fields and related attributes used within the application.
 * This class is crucial for managing and storing session details such as start time, end time, duration, and activity levels.
 */
public class Screen_Time_fields {
    /** The start time of a screen time session in a standard time format. */
    public String Start_time;

    /** The end time of a screen time session in a standard time format. */
    private String End_time;

    /** The duration of a screen time session in minutes. */
    private int Duration;

    /** The unique identifier for the screen time session. */
    private int ScreenTimeID;

    /** A recommended break time based on the duration of the session. */
    private String RecommendedBreak;

    /** The detected time with the most activity during the session. */
    private String MostActivityDetected;

    /** The detected time with the least activity during the session. */
    private String LeastActivityDetected;

    /** The tracking date of the session. */
    private String DateOfTrack;

    /**
     * Constructs a new instance of Screen_Time_fields with specified parameters.
     *
     * @param screenTimeID The unique ID of the screen time.
     * @param start_time The starting time of the screen usage.
     * @param end_time The ending time of the screen usage.
     * @param duration The total duration of screen usage.
     * @param dateOfTrack The date when the screen time was tracked.
     */
    public Screen_Time_fields(int screenTimeID, String start_time, String end_time, int duration, String dateOfTrack){
        ScreenTimeID = screenTimeID;
        Start_time = start_time;
        End_time = end_time;
        Duration = duration;
        DateOfTrack = dateOfTrack;
    }
    /** @return the session's duration. */
    public int getDuration() {
        return Duration;
    }

    /** @return the session's start time. */
    public String getStart_time() {
        return Start_time;
    }
    /** @return the session's end time. */
    public String getEnd_time() {
        return End_time;
    }






}
