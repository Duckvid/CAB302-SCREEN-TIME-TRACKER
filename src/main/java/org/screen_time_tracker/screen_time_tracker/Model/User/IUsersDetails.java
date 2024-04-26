package org.screen_time_tracker.screen_time_tracker.Model.User;

import java.util.List;

// this class is responsible for declaring some method signatures used to alter the users account in some way
// for allowing users to register, and for allowing users to login
public interface IUsersDetails {
    /**
     * Updates the users password
     * @param currentPassword to update
     */

    public void UpdatePassword(String currentPassword);

    /**
     * Updates the users email associated with their account
     * @param currentEmail to update
     */
    public void UpdateEmail(String currentEmail);

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



}
