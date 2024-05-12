package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.SQliteScreen_Timedata;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.Screen_Time_fields;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.Screen_time_tracking_feature;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Manages Login page activities, signing in to an account using persistent data and navigation to the home page
 */
public class LoginController {
    @FXML
    private Button Signup;

    @FXML
    private Button Loginbtn;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField PasswordField;

    private Button activeButton = null;

    @FXML
    private Button forgotPasswordbtn;

    /* This is a function which is responsible for actually controlling how frequently the screen time data is collected */
    private void startBackgroundWindowInfo(int UserId) {
        Thread screenTimethread = new Thread(() -> {
            Screen_time_tracking_feature widowinfo = new Screen_time_tracking_feature();
            //SQLiteUserDAO dao = new SQLiteUserDAO();
            SQliteScreen_Timedata screenTimeData;
            try {
                screenTimeData = new SQliteScreen_Timedata();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // start the session and begin to record start time
            String Start_Time = widowinfo.CurrentDateTime();

            // get the current Date
            java.util.Date currentDate = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatDate.format(currentDate);


            // get the current user somehow
            if(Session_Manager.isUserLoggedIn()){
                int UserID = Session_Manager.getCurrentUser().getUserid();
                screenTimeData.InsertScreenTimeData(Start_Time, strDate, UserID, "initial");
            }

            int Screen_Time_ID = 0;
            try {
                Screen_Time_ID = screenTimeData.getLastInsertedID();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            long sessionStart = 0;
            try {
                sessionStart = System.currentTimeMillis();
                while (!Thread.interrupted()) {
                    Map<String, Long> windowTimes = widowinfo.getWindowTimeMap();
                    long sessionEnd = System.currentTimeMillis(); // Update Session end time on each cycle
                    int duration = (int) ((sessionEnd - sessionStart) / 1000); // duration in seconds
                    String currentwindow = widowinfo.getActiveWindowTitle();

                    // update the db with the current duration
                    screenTimeData.UpdateScreenTimeData(Screen_Time_ID, duration, currentwindow);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                long sessionEnd = System.currentTimeMillis();
                int finalDuration = (int) ((sessionEnd - sessionStart) / 1000);
                String endTime = widowinfo.CurrentDateTime();

                // update end time and finalize duration
                screenTimeData.finalizeScreenTimeData(Screen_Time_ID, endTime, finalDuration);

                Thread.currentThread().interrupt();

            }

        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            screenTimethread.interrupt();
            try{
                screenTimethread.join();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }));

        screenTimethread.start();
    }
    /**
     * Handles the event triggered by clicking the 'Forgot Password' button.
     * This method will prompt the user to enter their email or phone number to recover their password.
     *
     * @throws IOException if an I/O error occurs when handling the click event.
     */

    @FXML
    protected void OnForgotPasswordbtnClick() throws IOException{

        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Retrieve Password");

        // set the button types
        ButtonType submitbuttontype = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitbuttontype, ButtonType.CANCEL);

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField textField = new TextField();
        textField.setPromptText("Email/Phone number");

        vbox.getChildren().add(new Label("Please enter your email or phone number:"));
        vbox.getChildren().add(textField);

        dialog.getDialogPane().setContent(vbox);

        Platform.runLater(textField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == submitbuttontype){
                return textField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(emailOrPhone -> {
            // handle the retrieval of the password here
        });
    }

    /**
     * Handles the event triggered by clicking the 'Sign Up' button.
     * This method navigates the user to the sign-up view.
     *
     * @throws IOException if an I/O error occurs when handling the click event.
     */
    @FXML
    protected void OnSignupButtonClick() throws IOException {
        Stage stage = (Stage) Signup.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Main_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    @FXML
    private void HandleLoginAction(ActionEvent event) throws IOException{
        String email = EmailField.getText();
        String password = PasswordField.getText();
        SQLiteUserDAO dao = new SQLiteUserDAO();

        if((!dao.IsEmailCorrect(email) || !dao.IsPasswordCorrect(password) || email.isEmpty() || password.isEmpty())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Input validation Error");
            alert.setContentText("Please make sure that your email and password is in the correct form and exists.");
            alert.showAndWait();
        }
        else {

            dao = new SQLiteUserDAO();
            User user = dao.Login(email, password);

            if(dao.UserExists(user)){

                Session_Manager.setCurrentUser(user);

                startBackgroundWindowInfo(user.getUserid());

                // user is found, navigate to home page once implmented but for now go to currentsession page
                Stage stage = (Stage) Loginbtn.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Home-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
                scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Home_style.css").toExternalForm());
                stage.setResizable(false);
                stage.setScene(scene);
            }

            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Input validation Error");
                alert.setContentText("User does not exist with those credentials try again.");
                alert.showAndWait();

            }



        }


    }

}
