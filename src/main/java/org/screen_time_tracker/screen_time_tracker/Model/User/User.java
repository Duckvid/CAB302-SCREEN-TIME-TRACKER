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

    /**
     * Constructor used to create an object instance of a user
     * @param name the current users name
     * @param email the current users email
     * @param password the current users password
     * @param phonenumber the current users phonenumber
     */
    public User(String name, String email, String password, String phonenumber){
        Name = name;
        Email = email;
        Phonenumber = phonenumber;
        Password = password;
    }

    /**
     *
     * @return The name of the currently active user
     */
    public String getName() {
        return Name;
    }

    /**
     *
     * @return the current usersID
     */
    public int getUserid() {
        return Userid;
    }

    /**
     *
     * @param userid to set and interact with
     */

    public void setUserid(int userid) {
        Userid = userid;
    }

    /**
     *
     * @param name of the current user
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     *
     * @return the current users phonenumber
     */

    public String getPhonenumber() {
        return Phonenumber;
    }

    /**
     *
     * @param phonenumber of the current user
     */

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    /**
     *
     * @return the current users password
     */
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

    /**
     *
     * @return the current users email
     */

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




}
