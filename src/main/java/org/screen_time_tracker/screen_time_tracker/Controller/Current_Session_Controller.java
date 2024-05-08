package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.SQliteScreen_Timedata;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.Screen_Time_fields;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Current_Session_Controller {

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

    @FXML
    private Label starttime;

    @FXML
    private Label Recommendationbreaktext;

    @FXML
    private Label RecommendedEnd;

    @FXML
    private Label MostActivityText;

    @FXML
    private Label LeastActivityText;
    @FXML
    private Label ComparisonText;

    public void appendStartTIme() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();

        if(currentUser != null){
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields screenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            if(screenTimeFields != null){
                String currentText = starttime.getText();
                starttime.setText(currentText + "\n" + screenTimeFields.getStart_time());
            }

        }

    }

    public void appendEndTIme() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();

        if(currentUser != null){
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields screenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            if(screenTimeFields != null){
                String currentText = RecommendedEnd.getText();

                // Start time
                String StartTimeString = screenTimeFields.getStart_time();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                LocalTime starttime = LocalTime.parse(StartTimeString, formatter);

                // add 4 hours to the start time
                LocalTime RecommendedEndTIme = starttime.plus(6, ChronoUnit.HOURS);
                String RecommendedEndTimeString = RecommendedEndTIme.format(formatter);

                RecommendedEnd.setText(currentText + "\n" + RecommendedEndTimeString);
            }

        }

    }

    public void appendRecommendedBreakTIme() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();

        if(currentUser != null){
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields screenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            if(screenTimeFields != null){
                String currentText = Recommendationbreaktext.getText();

                // Start time
                String StartTimeString = screenTimeFields.getStart_time();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                LocalTime starttime = LocalTime.parse(StartTimeString, formatter);

                // add 4 hours to the start time
                LocalTime RecommendedbreakTIme = starttime.plus(3, ChronoUnit.HOURS);
                String RecommendedbreakTimeString = RecommendedbreakTIme.format(formatter);

                Recommendationbreaktext.setText(currentText + "\n" + RecommendedbreakTimeString);
            }

        }

    }

    public void appendMostActivity() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();

        if(currentUser != null){
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields screenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            if(screenTimeFields != null){
                String currentText = MostActivityText.getText();
                String MaxStartTime = sQliteScreenTimeData.Most_Activity_Detected_StartTime();
                String MaxEndTime = sQliteScreenTimeData.Most_Activity_Detected_EndTime();
                MostActivityText.setText(currentText + "\n" + MaxStartTime + " - " + MaxEndTime);
            }

        }

    }

    public void appendLeastActivity() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();

        if(currentUser != null){
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields screenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            if(screenTimeFields != null){
                String currentText = LeastActivityText.getText();
                String smallestStartTime = sQliteScreenTimeData.Least_Activity_Detected_StartTime();
                String smallestEndTime = sQliteScreenTimeData.Least_Activity_Detected_EndTime();
                LeastActivityText.setText(currentText + "\n" + smallestStartTime + " - " + smallestEndTime);
            }

        }

    }

    public void appendComparison() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();

        if(currentUser != null){
            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields screenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            if(screenTimeFields != null){
                String currentText = ComparisonText.getText();
                ComparisonText.setText(currentText + " " + screenTimeFields.getStart_time());
            }

        }

    }

    @FXML
    public void initialize() throws SQLException {
        imgview.setTranslateY(-70); // This will move the logo 10 pixels up
        appendStartTIme();
        appendEndTIme();
        appendLeastActivity();
        appendMostActivity();
        appendRecommendedBreakTIme();
        appendComparison();


    }

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
    public void OnHomebtnClick() throws IOException {
        Stage stage = (Stage) Homebtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Home_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }
    @FXML
    protected void OnSettingsButtonClick() throws IOException {
        Stage stage = (Stage) settingsPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Settings.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Settings_Style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }


    public void OnRecommendationsPageClick() throws IOException {
        Stage stage = (Stage) Recommendationspage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Recommendations-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Recommendations.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    public void OnTimersButtonClick() throws IOException {
        Stage stage = (Stage) TimersPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Timers-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Timers_Style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    public void OnCurrentSessionBtnClick() throws IOException {
        Stage stage = (Stage) CurrentSessionPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("current_Session-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/current_Session_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);

    }

    public void OnContactBtnClick() throws IOException {
        Stage stage = (Stage) Contactbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Contact-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Contact_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }
}
