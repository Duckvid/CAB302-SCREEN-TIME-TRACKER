import org.junit.jupiter.api.*;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.SqliteConnection;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class tests {
    private SQLiteUserDAO dao;

    private Connection connection;
    private User user;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
        connection.setAutoCommit(false); // start each test with a new transaction
        dao = new SQLiteUserDAO(connection);
        user = new User("Test", "Testemail@gmail.com", "Password.1!", "041612345");
    }

    @AfterEach
    void Tear_down() throws SQLException{

        connection.rollback(); // roll back the transaction to ensure the test does not alter  the table

        // close the in-memory database connection
        connection.close();
    }

    @Test
    void TestCorrectPassword(){
        boolean actual = dao.IsPasswordCorrect("Password.1!");
        assertTrue(actual, "The password validation failed.");

    }

    @Test
    public void TestDBconnection(){
        Connection conn = SqliteConnection.getInstance();
        assertEquals(true, conn != null);
    }

    @Test
    void IncorrectPassword(){
        boolean actual = dao.IsPasswordCorrect("password.1");
        assertFalse(actual, "The password validation failed.");

    }

    @Test
    void TestCorrectEmail(){
        boolean actual = dao.IsEmailCorrect("exampleemail@gmail.com");
        assertTrue(actual, "The email validation failed.");
    }

    @Test
    void IncorrectEmail(){
        boolean actual = dao.IsPasswordCorrect("example_email.com");
        assertFalse(actual, "The email validation failed.");

    }

    @Test
    void Test_newUser_Added_toDB() {
        dao.RegisterAccount(user);

        // verify that the user has been assigned an ID, indicating they were inserted
        assertTrue(user.getUserid() > 0, "User ID should be assigned after registration");
    }

    @Test
    void TestLoginMethod() {


        dao.Login(user.getEmail(), user.getPassword());

        // verify that the user has been logged in with existing details
        assertTrue(user.getUserid() > 0, "User ID should exist to allow the user to login ");
    }

    @Test
    void TestGetUserID() {
        user.setUserid(1);

        //verify that the correct userID is returned
        assertEquals(1, user.getUserid(), "User ID should be equal to 1 in this test case");
    }

    @Test
    void TestGetUserName() {
        user.setName("Test");

        // verify that the correct username is returned
        assertEquals("Test", user.getName(), "User Name should be equal to 'Test' in this example");
    }

    @Test
    void TestGetUserEmail() {
        user.setEmail("exampleemail@gmail.com");

        // verify that the correct username is returned
        assertEquals("exampleemail@gmail.com", user.getEmail(), "User email should be equal to 'exampleemail@gmail.com' in this example");
    }

    @Test
    void TestGetUserPassword() {
        user.setPassword("Password1.!");

        // verify that the correct username is returned
        assertEquals("Password1.!", user.getPassword(), "User Password should be equal to 'Password1.!' in this example");
    }

    @Test
    void TestGetUserPhone() {
        user.setPhonenumber("041234567");

        // verify that the correct username is returned
        assertEquals("041234567", user.getPhonenumber(), "User Phone should be equal to '041234567' in this example");
    }


    @Test
    void TestSetUserID() {
        user.setUserid(2);

        assertEquals(2, user.getUserid(), "User ID should now be equal to 2 in this test case");
    }

    @Test
    void TestSetUserEmail() {
        user.setEmail("Newexampleemail@gmail.com");

        assertEquals("Newexampleemail@gmail.com", user.getEmail(), "User email should now be equal to '\"Newexampleemail@gmail.com\"' in this test case");
    }

    @Test
    void TestSetUserName() {
        user.setName("TestName2");

        assertEquals("TestName2", user.getName(), "User name should now be equal to 'TestName2' in this test case");
    }

    @Test
    void TestSetUserPassword() {
        user.setPassword("Password1.$");

        assertEquals("Password1.$", user.getPassword(), "User Password should now be equal to 'Password1.$' in this test case");
    }

    @Test
    void TestSetUserPhone() {
        user.setPhonenumber("123456789");

        assertEquals("123456789", user.getPhonenumber(), "User phone should now be equal to '123456789' in this test case");
    }

    @Test
    void TestCreateUsersTable() throws SQLException{
        dao.createUsers_And_Screen_Time_Data_Table();

        Statement checktablestatement = connection.createStatement();
        ResultSet resultSet = checktablestatement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Users'");

        // verify that the users table is being created
        assertTrue(resultSet.next(), "The users table should exist after creation");
    }

    @Test
    void TestDeleteAccount() throws SQLException {
        dao.DeleteAccount(user);
        String query = "SELECT * FROM Users where Userid = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, user.getUserid());
        ResultSet resultSet = pstmt.executeQuery();

        // verify that the user is being deleted
        assertTrue(!resultSet.next(), "The user should be deleted from the users table");

    }
}