package org.screen_time_tracker.screen_time_tracker.Model.CurrentSession;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;

import java.text.SimpleDateFormat;
import java.util.*;

public class WindowInfo {
    private Map<String, Long> windowTimeMap = new HashMap<>();

    private Map<String, Long> lastActiveTimeMap = new HashMap<>();
    private String LastActiveWindowTitle;
    private Long LastTimeChecked = System.currentTimeMillis();

    
    // method to retrieve the current date and time using the Calendar class
    public String CurrentDateTime(){
        String NewTimeString = null;

        // this will try and obtain the current date and time using an instance of the Calendar and Date clases
        try {
            Calendar calendar = Calendar.getInstance();
            Date CurrentDate = calendar.getTime();
            SimpleDateFormat currentTime12hr = new SimpleDateFormat("EEE, MMM dd, yyyy hh:mm a");
            NewTimeString = currentTime12hr.format(CurrentDate);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return NewTimeString;
    }

    // this method is responsible for tracking the users screen time accross the different windows.
    // it will keep track of what window is currently in use, as well as how long that window is being used for
    public Map<String, Long> getWindowTimeMap(){
        final User32 user32 = User32.INSTANCE;
        HWND hwnd = user32.GetForegroundWindow();
        char[] windowtext = new char[512];
        user32.GetWindowText(hwnd, windowtext, 512);
        String currentWindowTitle = Native.toString(windowtext).trim();
        long currentTime = System.currentTimeMillis();
        
        if(!currentWindowTitle.isEmpty()){
            if(LastActiveWindowTitle == null || LastActiveWindowTitle.equals(currentWindowTitle)) {
                updateWindowTime(currentWindowTitle, currentTime - LastTimeChecked);
                }

            else{
                updateWindowTime(LastActiveWindowTitle, currentTime - LastTimeChecked);
                LastActiveWindowTitle = currentWindowTitle;
            }

                LastTimeChecked = currentTime;
            }
            return new HashMap<>(windowTimeMap);
        }

    private void updateWindowTime(String windowTitle, long timeSpent) {
        Long totalSpent = windowTimeMap.getOrDefault(windowTitle, 0L) + timeSpent;
        windowTimeMap.put(windowTitle, totalSpent);
        lastActiveTimeMap.put(windowTitle, timeSpent); // track the last active time
    }






}
