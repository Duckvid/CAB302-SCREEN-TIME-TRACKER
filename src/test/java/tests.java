import org.junit.jupiter.api.*;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteScreenTimeDAO;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class tests {
    private SQLiteScreenTimeDAO dao;

    private Connection connecton;
    private User user;

    @BeforeEach
    void setUp() throws SQLException {
        connecton = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
        connecton.setAutoCommit(false); // start each test with a new transaction
        dao = new SQLiteScreenTimeDAO(connecton);
        user = new User("Test", "Testemail@gmail.com", "Password.1!", "041612345");
    }

    @AfterEach
    void Teardown() throws SQLException{

        connecton.rollback(); // roll back the transaction to ensure the test does not alter  the table

        // close the in-memory database connection
        connecton.close();
    }

    @Test
    void TestCorrectPassword(){
        boolean actual = dao.IsPasswordCorrect("Password.1!");
        assertEquals(true, actual, "The password validation failed.");

    }

    @Test
    void IncorrectPassword(){
        boolean actual = dao.IsPasswordCorrect("password.1");
        assertEquals(false, actual, "The password validation failed.");

    }

    @Test
    void TestCorrectEmail(){
        boolean actual = dao.IsEmailCorrect("exampleemail@gmail.com");
        assertEquals(true, actual, "The email validation failed.");
    }

    @Test
    void IncorrectEmail(){
        boolean actual = dao.IsPasswordCorrect("emapleemail.com");
        assertEquals(false, actual, "The email validation failed.");

    }

    @Test
    void newUserAddedtoDB() throws SQLException{
        dao.RegisterAccount(user);

        // verify that the user has been assigned an ID, indicating they were inserted
        assertTrue(user.getUserid() > 0, "User ID should be assigned after registration");
    }



}