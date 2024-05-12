package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import java.util.List;

// this class just houses some fields and methods that I think will be useful for implemting features in the other pages
// particularly the recommendations page, current Session page and the home page
public class Screen_Time_fields {

    public String Start_time;

    private String End_time;
    private int Duration;
    private int ScreenTimeID;
    private String RecommendedBreak;

    private String MostActivityDetected;

    private String LeastActivityDetected;
    private String DateOfTrack;


    public Screen_Time_fields(int screenTimeID, String start_time, String end_time, int duration, String dateOfTrack){
        ScreenTimeID = screenTimeID;
        Start_time = start_time;
        End_time = end_time;
        Duration = duration;
        DateOfTrack = dateOfTrack;
    }

    public String getDateOfTrack() {
        return DateOfTrack;
    }

    public void setDateOfTrack(String dateOfTrack) {
        DateOfTrack = dateOfTrack;
    }


    public int getScreenTimeID() {
        return ScreenTimeID;
    }

    public void setScreenTimeID(int screenTimeID) {
        ScreenTimeID = screenTimeID;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }


    public String getStart_time() {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getEnd_time() {
        return End_time;
    }

    public void setEnd_time(String end_time) {
        End_time = end_time;
    }

    public String getRecommendedBreak() {
        return RecommendedBreak;
    }

    public void setRecommendedBreak(String recommendedBreak) {
        RecommendedBreak = recommendedBreak;
    }

    public String getMostActivityDetected() {
        return MostActivityDetected;
    }

    public void setMostActivityDetected(String mostActivityDetected) {
        MostActivityDetected = mostActivityDetected;
    }

    public String getLeastActivityDetected() {
        return LeastActivityDetected;
    }

    public void setLeastActivityDetected(String leastActivityDetected) {
        LeastActivityDetected = leastActivityDetected;
    }





}
