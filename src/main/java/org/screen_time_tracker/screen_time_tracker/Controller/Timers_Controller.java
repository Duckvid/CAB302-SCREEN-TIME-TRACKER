package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Manages Timers session activities, including enabling notifications and setting timers for how long a user should be active on a screen .
 */
public class Timers_Controller {
    @FXML
    private Button settingsPage;

    @FXML
    private Button Recommendationspage;

    @FXML Button TimersPage;

    @FXML Button CurrentSessionPage;

    @FXML
    private Button Homebtn;

    @FXML
    private Button Contactbtn;

    @FXML
    private ImageView imgview;

    @FXML
    private Button Logoutbtn;

    /**
     * Initializes the controller. This method sets up necessary state and UI components
     * for the current session view.

     */
    @FXML
    public void initialize() {
        imgview.setTranslateY(-70); // This will move the logo 10 pixels up
    }

    /**
     * Handles user logout events. This method is called when the logout button is clicked
     * and is responsible for logging out the user and transitioning to the login screen.
     *
     * @throws IOException if an I/O error occurs when loading the login view
     */
    @FXML
    protected void OnLogoutBtnClick() throws IOException{
        SQLiteUserDAO sqLiteUserDAO = new SQLiteUserDAO();
        sqLiteUserDAO.Logout();
        Stage stage = (Stage) Logoutbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Login_Styles.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);

    }

    /**
     * Handles navigation to the home page events. This method is called when the Home button is clicked
     * and is responsible for navigation the user to the home page
     * @throws IOException if an I/O error occurs when loading the Home page view
     */
    @FXML
    public void OnHomebtnClick() throws IOException {
        Stage stage = (Stage) Homebtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Home_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the home page events. This method is called when the Home button is clicked
     * and is responsible for navigation the user to the home page
     * @throws IOException if an I/O error occurs when loading the settings page view
     */
    @FXML
    protected void OnSettingsButtonClick() throws IOException {
        Stage stage = (Stage) settingsPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Settings.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Settings_Style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the recommendation page events. This method is called when the recommendation button is clicked
     * and is responsible for navigation the user to the recommendation page
     * @throws IOException if an I/O error occurs when loading the Recommendations page view
     */
    @FXML
    public void OnRecommendationsPageClick() throws IOException {
        Stage stage = (Stage) Recommendationspage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Recommendations-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Recommendations.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the Timers page events. This method is called when the Timers button is clicked
     * and is responsible for navigation of the user to the Timeers page
     * @throws IOException if an I/O error occurs when loading the Timers page view
     */
    @FXML
    public void OnTimersButtonClick() throws IOException {
        Stage stage = (Stage) TimersPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Timers-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Timers_Style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the current session page events. This method is called when the current session button is clicked
     * and is responsible for navigation of the user to the current session page
     * @throws IOException if an I/O error occurs when loading the current session page view
     */
    @FXML
    public void OnCurrentSessionBtnClick() throws IOException {
        Stage stage = (Stage) CurrentSessionPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("current_Session-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/current_Session_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);

    }

    /**
     * Handles navigation to the contact page events. This method is called when the contact button is clicked
     * and is responsible for navigation of the user to the contacts page
     * @throws IOException if an I/O error occurs when loading the Contact page view
     */
    @FXML
    public void OnContactBtnClick() throws IOException {
        Stage stage = (Stage) Contactbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Contact-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Contact_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }
}
