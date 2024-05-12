package org.screen_time_tracker.screen_time_tracker.Model.User;

import java.util.List;

/**
 * This interface is responsible for defining the methods associated with the user
 * Particularly around CRUD operations involving the user
 */
public interface IUsersDetails {

    /**
     * Delete the users account
     * @param account to be deleted
     */

    public void DeleteAccount(User account);

    /**
     * Adds a new user to the database
     * @param user to add
     */
    public void RegisterAccount(User user);

    /**
     * Allows the user to sign in to their existing account using their details
     * @param email: The email used to sign in
     * @param password: The password used to sign in
     */
    public User Login(String email, String password);

    /**
     * returns all the users contained within the users table
     */

    public List<User> getAllUsers();

    /**
     * This method is used to verify the users Password to make sure its the correct length
     * minimum length of 8 characters
     * contains 1 special character
     * contains 1 captical
     * contains at least one number
     * @param Password: The password to be verified
     */
    public boolean IsPasswordCorrect(String Password);

    /**
     * This method verify the users email making sure it contains a @ symbol a .com/.au or similar to verify the details are valid
     * @param email: The email to be verified
     */
    public boolean IsEmailCorrect(String email);


    /**
     *
     * @param user
     * @returns true if the user exists in the db
     */
    public boolean UserExists(User user);

    /**
     * This method is responsible for creating the users and Screen_Time_Data_Table in the db
     */
    public void createUsers_And_Screen_Time_Data_Table();

    /**
     * This method is responsible for allowing the users to logout of their account thereby stopping the current screen time tracking session
     */
    public void Logout();

    /**
     *
     * @param name
     * @return This method returns true if the provided name is valid as according to the requirements in the code
     */

    public boolean IsNameValid(String name);

    /**
     *
     * @param Phonenumber
     * @return return This method returns true if the provided phonenumber is valid as according to the requirements in the code
     */

    public boolean IsNumberValid(String Phonenumber);

}
