package org.screen_time_tracker.screen_time_tracker.Model.User;

// this class will contain fields, properties and methods specifically related to the user
public class User {

    // this field is used to check if the user has created an account before
    // this will be useful when it comes to loggin in
    private boolean HasAccount = false;
    private String Email;
    private String Password;

    public User(){

    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isHasAccount() {
        return HasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        HasAccount = hasAccount;
    }


}
