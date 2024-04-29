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

import java.io.IOException;

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

    // Declare a Timeline object to manage the countdown timer
    private Timeline timeline;

    private class TimerBox extends VBox {
        private Timeline timerTimeline;
        private long durationLeftInSeconds;

        public TimerBox(String duration) {
            Label timerLabel = new Label("Timer: " + duration);
            timerLabel.getStyleClass().add("timer-label");

            Button pauseButton = new Button("Pause");
            pauseButton.setOnAction(e -> pauseTimer());

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteTimer(this));

            HBox timerTopBox = new HBox(timerLabel);
            timerTopBox.getStyleClass().add("timer-box-top");
            timerTopBox.setPrefHeight(50);

            HBox timerBottomBox = new HBox(pauseButton, deleteButton);
            timerBottomBox.getStyleClass().add("timer-box-bottom");
            timerBottomBox.setPrefHeight(50);

            getChildren().addAll(timerTopBox, timerBottomBox);

            // Start the countdown timer
            startCountDownTimer(timerLabel, duration);
        }

        private void startCountDownTimer(Label timerLabel, String duration) {
            String[] parts = duration.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            durationLeftInSeconds = hours * 3600 + minutes * 60 + seconds;

            timerTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        if (durationLeftInSeconds <= 0) {
                            timerTimeline.stop();
                            timerLabel.setText("Timer: 00:00:00");
                        } else {
                            long h = durationLeftInSeconds / 3600;
                            long m = (durationLeftInSeconds % 3600) / 60;
                            long s = durationLeftInSeconds % 60;
                            timerLabel.setText(String.format("Timer: %02d:%02d:%02d", h, m, s));
                            durationLeftInSeconds--;
                        }
                    })
            );
            timerTimeline.setCycleCount(Timeline.INDEFINITE);
            timerTimeline.play();
        }

        private void pauseTimer() {
            if (timerTimeline != null) {
                timerTimeline.pause();
            }
        }

        private void deleteTimer(TimerBox timerBox) {
            yourTimersBox.getChildren().remove(timerBox);
            // Stop the timer if it is running
            pauseTimer();
        }
    }

    @FXML
    private void showAddTimerPopup() {
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
        dialog.showAndWait().ifPresent(this::addTimerToYourTimers);
    }

    private void addTimerToYourTimers(String[] duration) {
        String timerDuration = String.format("%02d:%02d:%02d", Integer.parseInt(duration[0]), Integer.parseInt(duration[1]), Integer.parseInt(duration[2]));
        TimerBox timerBox = new TimerBox(timerDuration);
        yourTimersBox.getChildren().add(yourTimersBox.getChildren().size() - 1, timerBox);
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
