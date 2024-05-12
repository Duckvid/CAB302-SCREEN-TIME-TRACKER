package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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


public class Timers_Controller {

    @FXML
    private Button settingsPage;

    @FXML
    private Button Recommendationspage;

    @FXML
    private Button TimersPage;

    @FXML
    private Button CurrentSessionPage;

    @FXML
    private Button Homebtn;

    @FXML
    private Button Contactbtn;

    @FXML
    private ImageView imgview;

    @FXML
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
            // Convert seconds to hours, minutes, and seconds
            int h = (int) (seconds / 3600);
            int m = (int) ((seconds % 3600) / 60);
            int s = (int) (seconds % 60);

            String duration = String.format("%02d:%02d:%02d", h, m, s);

            timerLabel = new Label((isTimer ? "Timer: " : "Alarm: ") + duration);
            timerLabel.getStyleClass().add("timer-label");

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> editTimer());

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteTimer(this));

            HBox timerTopBox = new HBox(timerLabel);
            timerTopBox.getStyleClass().add("timer-box-top");
            timerTopBox.setPrefHeight(50);

            HBox timerBottomBox = new HBox(editButton, deleteButton);
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
    private void showAddAlarmPopup() {
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
        dialog.showAndWait().ifPresent(data -> addAlarmToYourTimers(data));
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



    private String[] getTimeString(int limit) {
        String[] timeString = new String[limit];
        for (int i = 0; i < limit; i++) {
            timeString[i] = String.valueOf(i);
        }
        return timeString;
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

    public void HandleLoginAction(ActionEvent actionEvent) {
    }
}
