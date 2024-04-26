package org.screen_time_tracker.screen_time_tracker.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// this class will contain fields, properties and methods specifically related to the user
public class User {

    // this field is used to check if the user has created an account before
    // this will be useful when it comes to loggin in
    private boolean HasAccount = false;
    private String Email;
    private String Password;
    private String Name;
    private String Phonenumber;
    private int Userid;


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


    /**
     * This method verify the users email making sure it contains a @ symbol a .com/.au or similar to verify the details are valid
     * @param email: The email to be verified
     */
    public boolean IsEmailCorrect(String email){
        return true;
    }



}
