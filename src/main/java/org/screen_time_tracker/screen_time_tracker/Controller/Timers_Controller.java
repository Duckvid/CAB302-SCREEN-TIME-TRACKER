package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;


import java.io.IOException;
import java.sql.SQLException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.SQliteScreen_Timedata;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.Screen_Time_fields;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;


/**
 * Manages Timers session activities, including enabling notifications and setting timers for how long a user should be active on a screen .
 */
public class Timers_Controller {
    @FXML
    public void addBreakAlarm(ActionEvent actionEvent){
        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Calculate the time 4 hours ahead
        LocalTime alarmTime = currentTime.plusHours(3);

        // Format the alarm time
        String alarmTimeString = alarmTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        // Add the alarm to your timers
        addAlarmToYourTimers(alarmTime);

        // Show confirmation message
        showAlert("Break Alarm Set", "The break alarm is set for " + alarmTimeString);
    }
    @FXML
    public void addEndAlarm(ActionEvent actionEvent){
        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Calculate the time 6 hours ahead
        LocalTime alarmTime = currentTime.plusHours(6);

        // Format the alarm time
        String alarmTimeString = alarmTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        // Add the alarm to your timers
        addAlarmToYourTimers(alarmTime);

        // Show confirmation message
        showAlert("End Alarm Set", "The end alarm is set for " + alarmTimeString);
    }

    public void addAlarmToYourTimers(LocalTime alarmTime){
        // Calculate the seconds until the alarm
        long secondsToAlarm = LocalTime.now().until(alarmTime, ChronoUnit.SECONDS);

        // Create a TimerBox for the alarm
        TimerBox timerBox = new TimerBox(secondsToAlarm, false);

        // Add the new TimerBox to the VBox
        yourTimersBox.getChildren().add(timerBox);
    }

    @FXML
    private Button settingsPage;

    @FXML
    private Button Recommendationspage;

    @FXML
    private Button TimersPage;

    @FXML
    private Button CurrentSessionPage;

    @FXML
    private Button homeBtn;

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
    private void showAddAlarmPopup(){
        // Add Alarm
        Label alarmLabel = new Label("Alarm Label");
        yourTimersList.add(alarmLabel);

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Set Alarm");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create and configure the ChoiceBox elements
        ChoiceBox<String> hourChoiceBox = new ChoiceBox<>();
        hourChoiceBox.getItems().addAll(getTimeString(24));
        hourChoiceBox.setValue("0");

        ChoiceBox<String> minuteChoiceBox = new ChoiceBox<>();
        minuteChoiceBox.getItems().addAll(getTimeString(60));
        minuteChoiceBox.setValue("0");

        // Create the VBox and add the ChoiceBox elements to it
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(
                new Label("Set Alarm Time:"),
                new Label("Hours:"), hourChoiceBox,
                new Label("Minutes:"), minuteChoiceBox
        );

        // Set the content of the dialog pane
        dialog.getDialogPane().setContent(vbox);

        // Request focus on the hour ChoiceBox by default
        hourChoiceBox.requestFocus();

        // Convert the result to a timer name when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                int hours = Integer.parseInt(hourChoiceBox.getValue());
                int minutes = Integer.parseInt(minuteChoiceBox.getValue());
                return new String[]{String.valueOf(hours), String.valueOf(minutes)};
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(time -> addAlarmTime(time));
    }

    private String[] getTimeString(int max){
        String[] timeStrings = new String[max];
        for (int i = 0; i < max; i++) {
            timeStrings[i] = String.valueOf(i);
        }
        return timeStrings;
    }

    private void addAlarmTime(String[] time){
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Calculate the difference between the alarm time and the current time
        long secondsToAlarm = (hours - currentTime.getHour()) * 3600 +
                (minutes - currentTime.getMinute()) * 60 -
                currentTime.getSecond();

        // If the calculated difference is negative, add 24 hours to it
        if (secondsToAlarm < 0) {
            secondsToAlarm += 24 * 3600;
        }

        // Format the alarm time
        String alarmTime = String.format("%02d:%02d", hours, minutes);

        // Create a new TimerBox for the alarm
        TimerBox timerBox = new TimerBox(secondsToAlarm, false);

        // Add the new TimerBox to the VBox
        yourTimersBox.getChildren().add(timerBox);

        // Check if the timer has reached 0
        if (secondsToAlarm == 0) {
            // Show notification
            timerBox.showAlert("Your alarm went off!", "Your alarm with time " + alarmTime + " went off!");
        }
    }

    private VBox addTimerPopup;

    @FXML
    private TextField timerNameField;

    @FXML
    private Button addTimerBtn;

    @FXML
    private Label yourTimersLabel;

    @FXML
    private VBox yourTimersBox;

    private ObservableList<Label> yourTimersList = FXCollections.observableArrayList(new Label());


    // Declare a Timeline object to manage the countdown timer
    private Timeline timeline;
    private long durationLeftInSeconds;




    private class TimerBox extends VBox {
        private Timeline timerTimeline;
        private long durationLeftInSeconds;
        private boolean isTimer;
        private boolean isPaused;
        private Label timerLabel;

        public TimerBox(long seconds, boolean isTimer) {
            this.isTimer = isTimer;
            this.isPaused = false;

            // Convert seconds to hours, minutes, and seconds
            int h = (int) (seconds / 3600);
            int m = (int) ((seconds % 3600) / 60);
            int s = (int) (seconds % 60);

            String duration = String.format("%02d:%02d:%02d", h, m, s);

            timerLabel = new Label((isTimer ? "Timer: " : "Alarm: "));
            timerLabel.getStyleClass().add("timer-label");

            Button primaryButton = new Button(isTimer ? "Pause" : "Edit");
            if (isTimer) {
                primaryButton.setOnAction(e -> pauseTimer());
            } else {
                primaryButton.setOnAction(e -> editTimer());
            }

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteTimer(this));

            HBox timerTopBox = new HBox(timerLabel);
            timerTopBox.getStyleClass().add("timer-box-top");
            timerTopBox.setPrefHeight(50);

            HBox timerBottomBox = new HBox(primaryButton, deleteButton);
            timerBottomBox.getStyleClass().add("timer-box-bottom");
            timerBottomBox.setPrefHeight(50);

            getChildren().addAll(timerTopBox, timerBottomBox);

            // Start the countdown timer
            startCountDownTimer(timerLabel, seconds);
        }

        public Label getTimerLabel() {
            return timerLabel;
        }

        private void startCountDownTimer(Label timerLabel, long seconds) {
            durationLeftInSeconds = seconds;

            timerTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        if (durationLeftInSeconds <= 0) {
                            timerTimeline.stop();
                            timerLabel.setText((isTimer ? "Timer: " : "Alarm: ") + "00:00:00");
                        } else {
                            long h = durationLeftInSeconds / 3600;
                            long m = (durationLeftInSeconds % 3600) / 60;
                            long s = durationLeftInSeconds % 60;
                            timerLabel.setText(String.format((isTimer ? "Timer: %02d:%02d:%02d" : "Alarm: %02d:%02d:%02d"), h, m, s));
                            durationLeftInSeconds--;
                        }
                    })
            );
            timerTimeline.setCycleCount(Timeline.INDEFINITE);
            timerTimeline.play();
        }

        private void editTimer() {
            Dialog<String[]> dialog = new Dialog<>();
            dialog.setTitle("Edit Alarm");

            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create and configure the ChoiceBox elements
            ChoiceBox<String> hourChoiceBox = new ChoiceBox<>();
            hourChoiceBox.getItems().addAll(getTimeString(24));
            hourChoiceBox.setValue("0");

            ChoiceBox<String> minuteChoiceBox = new ChoiceBox<>();
            minuteChoiceBox.getItems().addAll(getTimeString(60));
            minuteChoiceBox.setValue("0");

            // Create the VBox and add the ChoiceBox elements to it
            VBox vbox = new VBox();
            vbox.setSpacing(10);
            vbox.getChildren().addAll(
                    new Label("Edit Alarm Time:"),
                    new Label("Hours:"), hourChoiceBox,
                    new Label("Minutes:"), minuteChoiceBox
            );

            // Set the content of the dialog pane
            dialog.getDialogPane().setContent(vbox);

            // Request focus on the hour ChoiceBox by default
            hourChoiceBox.requestFocus();

            // Convert the result to a timer name when the save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    int hours = Integer.parseInt(hourChoiceBox.getValue());
                    int minutes = Integer.parseInt(minuteChoiceBox.getValue());
                    return new String[]{String.valueOf(hours), String.valueOf(minutes)};
                }
                return null;
            });

            // Show the dialog and wait for the user's response
            dialog.showAndWait().ifPresent(time -> updateAlarmTime(time, this));
        }

        private void updateAlarmTime(String[] time, TimerBox timerBox) {
            int hours = Integer.parseInt(time[0]);
            int minutes = Integer.parseInt(time[1]);

            // Get the current time
            LocalTime currentTime = LocalTime.now();

            // Calculate the difference between the alarm time and the current time
            long secondsToAlarm = (hours - currentTime.getHour()) * 3600 +
                    (minutes - currentTime.getMinute()) * 60 -
                    currentTime.getSecond();

            // If the calculated difference is negative, add 24 hours to it
            if (secondsToAlarm < 0) {
                secondsToAlarm += 24 * 3600;
            }

            // Stop the previous timer timeline
            timerTimeline.stop();

            // Format the alarm time
            String alarmTime = String.format("%02d:%02d", hours, minutes);
            timerBox.updateAlarmLabel(alarmTime);

            // Update timerBox countdown
            startCountDownTimer(timerBox.getTimerLabel(), secondsToAlarm);

            // Check if the timer has reached 0
            if (secondsToAlarm == 0) {
                // Show notification
                showAlert("Your alarm went off!", "Your alarm with time " + alarmTime + " went off!");
            }
        }

        private void showAlert(String title, String content) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

        private void updateAlarmLabel(String alarmTime) {
            timerLabel.setText("Alarm: " + alarmTime);
        }

        private void deleteTimer(TimerBox timerBox) {
            yourTimersBox.getChildren().remove(timerBox);
            // Stop the timer if it is running
            pauseTimer();
        }

        private void pauseTimer() {
            if (isPaused) {
                timerTimeline.play();
                isPaused = false;
            } else {
                timerTimeline.pause();
                isPaused = true;
            }
        }
    }



    @FXML
    private void editTimer(TimerBox timerBox) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Edit Alarm");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create and configure the ChoiceBox elements
        ChoiceBox<String> hourChoiceBox = new ChoiceBox<>();
        hourChoiceBox.getItems().addAll(getTimeString(24));
        hourChoiceBox.setValue("0");

        ChoiceBox<String> minuteChoiceBox = new ChoiceBox<>();
        minuteChoiceBox.getItems().addAll(getTimeString(60));
        minuteChoiceBox.setValue("0");

        // Create the VBox and add the ChoiceBox elements to it
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(
                new Label("Edit Alarm Time:"),
                new Label("Hours:"), hourChoiceBox,
                new Label("Minutes:"), minuteChoiceBox
        );

        // Set the content of the dialog pane
        dialog.getDialogPane().setContent(vbox);

        // Request focus on the hour ChoiceBox by default
        hourChoiceBox.requestFocus();

        // Convert the result to a timer name when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                int hours = Integer.parseInt(hourChoiceBox.getValue());
                int minutes = Integer.parseInt(minuteChoiceBox.getValue());
                return new String[]{String.valueOf(hours), String.valueOf(minutes)};
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(time -> updateAlarmTime(time, timerBox));
    }

    private void updateAlarmTime(String[] time, TimerBox timerBox) {
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Calculate the difference between the alarm time and the current time
        long secondsToAlarm = (hours - currentTime.getHour()) * 3600 +
                (minutes - currentTime.getMinute()) * 60 -
                currentTime.getSecond();

        // If the calculated difference is negative, add 24 hours to it
        if (secondsToAlarm < 0) {
            secondsToAlarm += 24 * 3600;
        }

        // Format the alarm time
        String alarmTime = String.format("%02d:%02d", hours, minutes);
        timerBox.updateAlarmLabel(alarmTime);

        // Update timerBox countdown
        timerBox.startCountDownTimer(timerBox.getTimerLabel(), secondsToAlarm);
    }



    private void addAlarmToYourTimers(String[] time) {
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        // Get the current time
        LocalTime currentTime = LocalTime.now();

        // Calculate the difference between the alarm time and the current time
        long secondsToAlarm = (hours - currentTime.getHour()) * 3600 +
                (minutes - currentTime.getMinute()) * 60 -
                currentTime.getSecond();

        // If the calculated difference is negative, add 24 hours to it
        if (secondsToAlarm < 0) {
            secondsToAlarm += 24 * 3600;
        }

        // Format the alarm time
        String alarmTime = String.format("%02d:%02d", hours, minutes);
        Label alarmLabel = new Label("Alarm: " + alarmTime);
        yourTimersList.add(alarmLabel);

        // Create and add the timer box
        TimerBox timerBox = new TimerBox(secondsToAlarm, false);
        yourTimersBox.getChildren().add(yourTimersBox.getChildren().size() - 1, timerBox);
    }

    @FXML
    private void showAddTimerPopup() {
        Label timerLabel = new Label("Timer Label");
        yourTimersList.add(timerLabel);

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Add Timer");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create and configure the ChoiceBox elements
        ChoiceBox<String> hourChoiceBox = new ChoiceBox<>();
        hourChoiceBox.getItems().addAll(getTimeString(24));
        hourChoiceBox.setValue("0");

        ChoiceBox<String> minuteChoiceBox = new ChoiceBox<>();
        minuteChoiceBox.getItems().addAll(getTimeString(60));
        minuteChoiceBox.setValue("0");

        ChoiceBox<String> secondChoiceBox = new ChoiceBox<>();
        secondChoiceBox.getItems().addAll(getTimeString(60));
        secondChoiceBox.setValue("0");

        // Create the VBox and add the ChoiceBox elements to it
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(
                new Label("Enter Timer Duration:"),
                new Label("Hours:"), hourChoiceBox,
                new Label("Minutes:"), minuteChoiceBox,
                new Label("Seconds:"), secondChoiceBox
        );

        // Set the content of the dialog pane
        dialog.getDialogPane().setContent(vbox);

        // Request focus on the hour ChoiceBox by default
        hourChoiceBox.requestFocus();

        // Convert the result to a timer name when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                int hours = Integer.parseInt(hourChoiceBox.getValue());
                int minutes = Integer.parseInt(minuteChoiceBox.getValue());
                int seconds = Integer.parseInt(secondChoiceBox.getValue());
                return new String[]{String.valueOf(hours), String.valueOf(minutes), String.valueOf(seconds)};
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(data -> addTimerToYourTimers(data));
    }

    private void addTimerToYourTimers(String[] duration) {
        int hours = Integer.parseInt(duration[0]);
        int minutes = Integer.parseInt(duration[1]);
        int seconds = Integer.parseInt(duration[2]);
        long totalSeconds = hours * 3600 + minutes * 60 + seconds;
        String timerDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        TimerBox timerBox = new TimerBox(totalSeconds, true); // Convert to seconds
        yourTimersBox.getChildren().add(yourTimersBox.getChildren().size() - 1, timerBox);
    }


    @FXML
    private Label Recommendationbreaktext;

    @FXML
    private Label RecommendedEnd;
    @FXML
    private Button addBreakAlarmBtn;

    @FXML
    private Button addEndAlarmBtn;

    // This method will be called when the "Add Alarm +" button for break time is clicked
    @FXML
    public void addBreakAlarm() {
        String breakTime = Recommendationbreaktext.getText();
        if (breakTime != null && !breakTime.isEmpty()) {
            showAlert("Alarm Set", "Break time alarm set for: " + breakTime);
        }
    }

    // This method will be called when the "Add Alarm +" button for end time is clicked
    @FXML
    private void addEndAlarm() {
        String endTime = RecommendedEnd.getText();
        if (endTime != null && !endTime.isEmpty()) {
            showAlert("Alarm Set", "End time alarm set for: " + endTime);
        }
    }

    // Utility method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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



    @FXML
    private void pauseTimer(TimerBox timerBox) {
        if (timerBox != null) {
            timerBox.pauseTimer();
        }
    }

    @FXML
    private void deleteTimer(TimerBox timerBox) {
        yourTimersBox.getChildren().remove(timerBox);
    }

    @FXML
    private void hideAddTimerPopup() {
        addTimerPopup.setVisible(false);
    }

    @FXML
    public void initialize() {
        imgview.setTranslateY(-70); // This will move the logo 10 pixels up
        yourTimersBox.getChildren().addAll(yourTimersList);

        try {
            appendRecommendedBreakTIme();
            appendEndTIme();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        Stage stage = (Stage) homeBtn.getScene().getWindow();
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