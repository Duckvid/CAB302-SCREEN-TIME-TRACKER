package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.SQliteScreen_Timedata;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages Home page including navigation throughout the application.
 */
public class Home_Controller {
    @FXML
    private Button settingsPage;

    @FXML
    private Button Recommendationspage;

    @FXML Button TimersPage;

    @FXML Button CurrentSessionPage;

    @FXML
    private Button insightbtn;

    @FXML
    private Button recommendationsbtn;

    @FXML
    private Button Homebtn;

    @FXML
    private Button Contactbtn;

    @FXML
    private Button Logoutbtn;


    @FXML
    private ImageView imgview;


    @FXML
    private HBox barChartContainer;


    /**
     * called to append the current session data to the bar chart to reflect and visualize current session data
     * @throws SQLException if an SQL error occurs during data retrieval
     */
    private void PopulateBarChart() throws SQLException {
        SQliteScreen_Timedata sQliteScreenTimedata = new SQliteScreen_Timedata();
        StackedBarChart<String, Number> chart = new StackedBarChart<>(new CategoryAxis(), new NumberAxis());

        java.util.Date currentDate = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatDate.format(currentDate);

        // Initialize the seriesMap to store series by window titles
        Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

        // Fetch data grouped by hours and window titles
        Map<String, Map<String, Integer>> timeMap = sQliteScreenTimedata.FetchWindowDurations(strDate);

        // Prepare data for the chart
        for (Map.Entry<String, Map<String, Integer>> hourEntry : timeMap.entrySet()) {
            String hour = hourEntry.getKey();
            Map<String, Integer> windowDurations = hourEntry.getValue();
            for (Map.Entry<String, Integer> durationEntry : windowDurations.entrySet()) {
                String windowTitle = durationEntry.getKey();
                Integer duration = durationEntry.getValue();

                // Create or retrieve the series for this window title
                XYChart.Series<String, Number> series = seriesMap.computeIfAbsent(windowTitle, k -> new XYChart.Series<>());
                series.setName(windowTitle); // Set the name of series to window title for legend
                series.getData().add(new XYChart.Data<>(hour, duration));
            }
        }

        // Add all series to the chart
        chart.getData().addAll(seriesMap.values());
        barChartContainer.getChildren().clear();
        barChartContainer.getChildren().add(chart);
    }


    /**
     * Initializes the controller. This method is automatically called
     * after the FXML fields have been populated and is used to set up the initial
     * state of the controller, such as configuring UI components or loading initial data.
     * @throws SQLException if there is an issue with data obtainment
     */
    @FXML
    public void initialize() throws SQLException {

        imgview.setTranslateY(-70); // This will move the logo 10 pixels up
        PopulateBarChart();
    }

    /**
     * Handles navigation to the current session page from the home page events. This method is called when the insights button is clicked
     * and is responsible for navigation of the user to the current session page
     * @throws IOException if an I/O error occurs when loading the current session view
     */
    @FXML
    public void OnInsightBtnClick() throws IOException {
        Stage stage = (Stage) insightbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("current_Session-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/current_Session_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);

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
