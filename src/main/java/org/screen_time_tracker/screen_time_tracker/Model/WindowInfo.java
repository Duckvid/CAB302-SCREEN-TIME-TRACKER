package org.screen_time_tracker.screen_time_tracker.Model;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowInfo {
    private Map<String, Long> windowTimeMap = new HashMap<>();

    private Map<String, Long> lastActiveTimeMap = new HashMap<>();
    private String LastActiveWindowTitle;
    private Long LastTimeChecked = System.currentTimeMillis();
    public Map<String, Long> getWindowTimeMap(){
        final User32 user32 = User32.INSTANCE;
        HWND hwnd = user32.GetForegroundWindow();
        char[] windowtext = new char[512];
        user32.GetWindowText(hwnd, windowtext, 512);
        String currentWindowTitle = Native.toString(windowtext).trim();

        if(!currentWindowTitle.isEmpty()){
            long currentTime = System.currentTimeMillis();
            if(LastActiveWindowTitle != null && !LastActiveWindowTitle.equals(currentWindowTitle)) {
                updateWindowTime(LastActiveWindowTitle, currentTime - LastTimeChecked);
                }
                LastActiveWindowTitle = currentWindowTitle;
                LastTimeChecked = currentTime;
            }
            return new HashMap<>(windowTimeMap);
        }

    private void updateWindowTime(String windowTitle, long timeSpent) {
        if (lastActiveTimeMap.containsKey(windowTitle)) {
            long totalSpent = windowTimeMap.getOrDefault(windowTitle, 0L) + timeSpent;
            windowTimeMap.put(windowTitle, totalSpent);
        } else {
            windowTimeMap.put(windowTitle, timeSpent);
            lastActiveTimeMap.put(windowTitle, timeSpent);
        }
    }






}
