package org.screen_time_tracker.screen_time_tracker.Model.User;

public class Session_Manager {

    private static User CurrentUser;

    public static boolean isUserLoggedIn(){
        return CurrentUser != null;
    }
    public static User getCurrentUser() {
        return CurrentUser;
    }

    public static void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }



}
