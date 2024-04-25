package org.screen_time_tracker.screen_time_tracker.Model.User;

// this class is responsible for declaring some method signatures used to alter the users account in some way
// for allowing users to register, and for allowing users to login
public interface IUsersDetails {
    /**
     * Updates the users password
     * @param currentPassword to update
     */

    public void UpdatePassword(User currentPassword);

    /**
     * Updates the users email associated with their account
     * @param currentEmail to update
     */
    public void UpdateEmail(User currentEmail);

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
    public void Login(User email, User password);

}
