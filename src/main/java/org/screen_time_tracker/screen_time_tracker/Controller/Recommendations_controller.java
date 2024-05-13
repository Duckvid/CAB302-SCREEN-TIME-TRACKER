package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.random;

public class Recommendations_controller {
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
    private Label start_time;

    @FXML
    private Label medianstarttimes;

    @FXML
    private Label medianendtimes;

    @FXML
    private Label mostactivitydetected;

    @FXML
    private Label leastactivitydetected;

    @FXML
    private Label medianactivitydetected;

    @FXML
    private Label breaktimess;

    @FXML
    private Label recommendend;
    @FXML
    public void initialize() {
        imgview.setTranslateY(-70);

        // This will move the logo 10 pixels up
        start_time.setText("Your Start Time: "+getStartTime());;
        medianstarttimes.setText("Your median start time: "+getmedianStartTime());
        medianendtimes.setText("Your median end time: "+ getmedianEndTime());
        mostactivitydetected.setText("Most Activity Detected: "+'\n'+MostActivitydetected() );
        leastactivitydetected.setText("Least Activity Detected: "+'\n'+LeastActivitydetected());
        medianactivitydetected.setText("Median activity detected: "+'\n'+getmedianStartTime()+'-'+getmedianEndTime());
        breaktimess.setText("Recommended Break times: "+ breaktime());
        recommendend.setText("Recommend End times: "+ recommendendtime());
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

    public String getStartTime() {
        String starttime = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM ScreenTimeData";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                starttime = resultSet.getString("Start_Time");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return starttime;
    }
    public String getmedianStartTime() {
        ArrayList<String> medianstarttimes= new ArrayList<String>();
        String median_start_time = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM ScreenTimeData";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                medianstarttimes.add(resultSet.getString("Start_Time"));


            }
            Collections.sort(medianstarttimes);
            if(medianstarttimes.size()%2==0){
                median_start_time= medianstarttimes.get(((medianstarttimes.size()/2)+((medianstarttimes.size()/2)-1))/2);
            }
            else {
                median_start_time= medianstarttimes.get((medianstarttimes.size()/2)-1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return median_start_time;
    }
    public String getmedianEndTime() {
        ArrayList<String> medianendtimes= new ArrayList<String>();
        String median_end_time = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM ScreenTimeData";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                medianendtimes.add(resultSet.getString("End_Time"));


            }
            Collections.sort(medianendtimes);
            if(medianendtimes.size()%2==0){
                median_end_time= medianendtimes.get(((medianendtimes.size()/2)+((medianendtimes.size()/2)-1))/2);
            }
            else {
                median_end_time= medianendtimes.get((medianendtimes.size()/2)-1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return median_end_time;
}
      public String MostActivitydetected(){
          String starttime = null;
          String endtime=null;
          try {
              Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
              Statement statement = connection.createStatement();
              String query = "SELECT Start_Time, End_Time FROM ScreenTimeData WHERE Duration = (SELECT MAX(Duration) FROM ScreenTimeData)";
              ResultSet resultSet = statement.executeQuery(query);
              while (resultSet.next()) {

                  starttime = resultSet.getString("Start_Time");
                   endtime = resultSet.getString("End_Time");

              }
          } catch (Exception e) {
              e.printStackTrace();
          }
          return starttime+'-'+endtime;
      }
    public String LeastActivitydetected(){
        String starttime = null;
        String endtime=null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
            Statement statement = connection.createStatement();
            String query = "SELECT Start_Time, End_Time FROM ScreenTimeData WHERE Duration = (SELECT MIN(Duration) FROM ScreenTimeData)";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                starttime = resultSet.getString("Start_Time");
                endtime = resultSet.getString("End_Time");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return starttime+'-'+endtime;
    }
    public String breaktime() {
        ArrayList<String> Breaktimes = new ArrayList<String>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
            Statement statement = connection.createStatement();
            String query = "SELECT Start_Time, End_Time FROM ScreenTimeData ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                Breaktimes.add(resultSet.getString("Start_Time"));
                Breaktimes.add(resultSet.getString("End_Time"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String breaktime= Breaktimes.get((int) random()*Breaktimes.size());
       return breaktime;
    }
    public String recommendendtime() {
        ArrayList<String> endtimes = new ArrayList<String>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:ScreenTimeTracker.db");
            Statement statement = connection.createStatement();
            String query = "SELECT End_Time FROM ScreenTimeData ";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                endtimes.add(resultSet.getString("End_Time"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String endtimess= endtimes.get((int) random()*endtimes.size());
        return endtimess;
    }
}


