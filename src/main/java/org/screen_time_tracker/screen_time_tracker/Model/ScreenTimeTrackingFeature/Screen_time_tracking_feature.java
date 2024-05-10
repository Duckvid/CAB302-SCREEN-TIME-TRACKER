package org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Screen_time_tracking_feature {

    private boolean ISwindows;

    private boolean isMac;

    public Screen_time_tracking_feature(){
        String OSname = System.getProperty("os.name").toLowerCase();
        ISwindows = OSname.contains("win");
        isMac = OSname.contains("mac");
    }
    private Map<String, Long> windowTimeMap = new HashMap<>();

    private Map<String, Long> lastActiveTimeMap = new HashMap<>();
    private String LastActiveWindowTitle;
    private Long LastTimeChecked = System.currentTimeMillis();


    public String getActiveWindowTitle(){
        if(isMac){
            try{
                String[] cmd = {"osascript", "-e", "tell application \"System Events\" to get the name of the first process whose frontmost is true"};
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                return input.readLine();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            final User32 user32 = User32.INSTANCE;
            HWND hwnd = user32.GetForegroundWindow();

            if(hwnd != null){
                char[] windowtext = new char[512];
                user32.GetWindowText(hwnd, windowtext, 512);
                return Native.toString(windowtext).trim();
            }

        }
        return null;
    }

    // method to retrieve the current date and time using the Calendar class
    public String CurrentDateTime(){
        String NewTimeString = null;

        // this will try and obtain the current date and time using an instance of the Calendar and Date clases
        try {
            Calendar calendar = Calendar.getInstance();
            Date CurrentDate = calendar.getTime();
            SimpleDateFormat currentTime12hr = new SimpleDateFormat("hh:mm a");
            NewTimeString = currentTime12hr.format(CurrentDate);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return NewTimeString;
    }

    // it will keep track of what window is currently in use, as well as how long that window is being used for
    public Map<String, Long> getWindowTimeMap(){
        String currentWindowTitle = getActiveWindowTitle();
        long currentTime = System.currentTimeMillis();

        if(currentWindowTitle != null && !currentWindowTitle.isEmpty()){
            if(LastActiveWindowTitle == null || LastActiveWindowTitle.equals(currentWindowTitle)) {
                if(LastActiveWindowTitle != null){
                    updateWindowTime(LastActiveWindowTitle, currentTime - LastTimeChecked);
                }
                LastActiveWindowTitle = currentWindowTitle;
            }

            else{
                updateWindowTime(LastActiveWindowTitle, currentTime - LastTimeChecked);
            }

            LastTimeChecked = currentTime;
        }
        return new HashMap<>(windowTimeMap);
    }

    private String categorizeWindowTitle(String windowTitle) {
        String titleLower = windowTitle.toLowerCase();
        if (titleLower.contains("word") || titleLower.contains("excel")) {
            return "Work";
        } else if (titleLower.contains("facebook") || titleLower.contains("whatsapp")) {
            return "Social";
        } else if (titleLower.contains("chrome") || titleLower.contains("firefox")) {
            return "Browsing";  // Adding a new category for web browsing
        } else {
            return "Other";
        }
    }

    private void updateWindowTime(String windowTitle, long timeSpent) {
        Long totalSpent = windowTimeMap.getOrDefault(windowTitle, 0L) + timeSpent;
        windowTimeMap.put(windowTitle, totalSpent);
        lastActiveTimeMap.put(windowTitle, timeSpent); // track the last active time
    }






}
