package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages current session activities, including tracking and displaying user activity.
 */
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

    @FXML
    private HBox barChartContainer;


    /**
     * called to append the current session data to the bar chart to reflect and visualize current session data
     * @throws SQLException if an SQL error occurs during data retrieval
     */

    private static Map<String, XYChart.Series<String, Number>> accumulatedSeriesMap = new HashMap<>();

    private void PopulateBarChart() throws SQLException {
        SQliteScreen_Timedata sQliteScreenTimedata = new SQliteScreen_Timedata();
        java.util.Date currentDate = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatDate.format(currentDate);

        // Fetch only new data if possible or all data and filter it
        Map<String, Map<String, Integer>> timeMap = sQliteScreenTimedata.FetchWindowDurations(strDate);

        // Merge new data with existing data in accumulatedSeriesMap
        mergeData(timeMap);

        // Rebuild chart only if necessary (e.g., first load or if there's new data)
        if (barChartContainer.getChildren().isEmpty() || !timeMap.isEmpty()) {
            StackedBarChart<String, Number> chart = new StackedBarChart<>(new CategoryAxis(), new NumberAxis());
            chart.getData().addAll(accumulatedSeriesMap.values());
            barChartContainer.getChildren().setAll(chart); // Replace old chart with new one
        }
    }

    private void mergeData(Map<String, Map<String, Integer>> newTimeMap) {
        for (Map.Entry<String, Map<String, Integer>> hourEntry : newTimeMap.entrySet()) {
            String hour = hourEntry.getKey();
            Map<String, Integer> windowDurations = hourEntry.getValue();
            for (Map.Entry<String, Integer> durationEntry : windowDurations.entrySet()) {
                String windowTitle = durationEntry.getKey();
                Integer duration = durationEntry.getValue();
                XYChart.Series<String, Number> series = accumulatedSeriesMap.computeIfAbsent(windowTitle, k -> new XYChart.Series<>());
                series.setName(windowTitle);
                // Update or add new data point
                Optional<XYChart.Data<String, Number>> existingData = series.getData().stream()
                        .filter(d -> d.getXValue().equals(hour)).findFirst();
                if (existingData.isPresent()) {
                    existingData.get().setYValue(existingData.get().getYValue().intValue() + duration);
                } else {
                    series.getData().add(new XYChart.Data<>(hour, duration));
                }
            }
        }
    }


    /**
     * Called to append the start time of the current session to the UI.
     * This method retrieves the start time from the session data and updates the UI accordingly.
     *
     * @throws SQLException if an SQL error occurs during data retrieval
     */

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


    /**
     * Called to append the end time of the current session to the UI.
     * This method retrieves the end time from the session data and updates the UI accordingly.
     *
     * @throws SQLException if an SQL error occurs during data retrieval
     */

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

    /**
     * Called to append the recommended break time of the current session to the UI.
     * This method retrieves the break time by applying a mathematical procedure to the start time from the session data and updates the UI accordingly.
     *
     * @throws SQLException if an SQL error occurs during data retrieval
     */


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

    /**
     * Called to append the most activity duration of the current session to the UI.
     * This method retrieves the most activity duration from the session data and updates the UI accordingly.
     *
     * @throws SQLException if an SQL error occurs during data retrieval
     */


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


    /**
     * Called to append the least activity of the current session to the UI.
     * This method retrieves the least duration from the session data and updates the UI accordingly.
     *
     * @throws SQLException if an SQL error occurs during data retrieval
     */

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

    /**
     * Called to append the Comparison text fields of the current session to the UI.
     * This method retrieves the current session and previous session durations from the session data and updates the UI accordingly.
     *
     * @throws SQLException if an SQL error occurs during data retrieval
     */


    public void appendComparison() throws SQLException {
        User currentUser = Session_Manager.getCurrentUser();
        int UserID = currentUser.getUserid();
        if(currentUser != null){

            SQliteScreen_Timedata sQliteScreenTimeData = new SQliteScreen_Timedata();

            Screen_Time_fields CurrentscreenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFields(currentUser.getUserid());

            Screen_Time_fields previousScreenTimeFields = sQliteScreenTimeData.ReturnScreenTimeFieldsPrevious(UserID);

            if(CurrentscreenTimeFields != null && previousScreenTimeFields != null){
                int currentSessionDuration = CurrentscreenTimeFields.getDuration();

                int previousSessionDuration = previousScreenTimeFields.getDuration();


                double PercentageOfDifference = (double) (currentSessionDuration - previousSessionDuration) /previousSessionDuration * 100;

                int PercentageOfDifferenceInt = (int) Math.round(PercentageOfDifference);

                String currentText = ComparisonText.getText();

                if(currentSessionDuration < previousSessionDuration){

                    ComparisonText.setText(currentText + "\n" + "You have been" + PercentageOfDifferenceInt + "%" + " less active this \n session compared to your \n last session." );
                }

                else{

                    ComparisonText.setText(currentText + "\n" + "You have been" + PercentageOfDifferenceInt + "%" + " more active this \n session compared to your \n last session." );

                }


            }else{
                String currentText = ComparisonText.getText();
                ComparisonText.setText(currentText);
            }

        }

    }


    /**
     * Initializes the controller. This method sets up necessary state and UI components
     * for the current session view.
     *
     * @throws SQLException if an SQL error occurs during initialization
     */

    @FXML
    public void initialize() throws SQLException {
        imgview.setTranslateY(-70); // This will move the logo 10 pixels up
        appendStartTIme();
        appendEndTIme();
        appendLeastActivity();
        appendMostActivity();
        appendRecommendedBreakTIme();
        appendComparison();
        PopulateBarChart();

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
