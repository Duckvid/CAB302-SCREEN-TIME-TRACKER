package org.screen_time_tracker.screen_time_tracker.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
// this class will contain fields, properties and methods specifically related to the user
public class User{

    // this field is used to check if the user has created an account before
    // this will be useful when it comes to loggin in
    private boolean HasAccount = false;
    private String Email;
    private String Password;
    private String Name;
    private String Phonenumber;
    private int Userid;
    private  boolean IsLoggedIn = false;

    public User(String name, String email, String password, String phonenumber){
        Name = name;
        Email = email;
        Phonenumber = phonenumber;
        Password = password;
    }
    public String getName() {
        return Name;
    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }


    public String getPassword() {
        return Password;
    }

    /**
     * Updates the users password
     * @param password to update
     */
    public void setPassword(String password) {
        Password = password;
    }



    public String getEmail() {
        return Email;
    }


    /**
     * Updates the users email associated with their account
     * @param email to update
     */
    public void setEmail(String email) {
        Email = email;
    }


    public boolean isHasAccount() {
        return HasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        HasAccount = hasAccount;
    }


    public boolean isLoggedIn() {
        return IsLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        IsLoggedIn = loggedIn;
    }


}
