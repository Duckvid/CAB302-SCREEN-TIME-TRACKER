package org.screen_time_tracker.screen_time_tracker.Model.User;

/**
 * This class is used to keep track of the current session data for the screen time tracking application
 */
public class Session_Manager {

    private static User CurrentUser;

    /**
     *
     * @return true if the user is logged in false otherwise
     */
    public static boolean isUserLoggedIn(){
        return CurrentUser != null;
    }

    /**
     *
     * @return The currently active user
     */
    public static User getCurrentUser() {
        return CurrentUser;
    }

    /**
     *
     * @param currentUser the current user that is signed in
     */
    public static void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }



}
