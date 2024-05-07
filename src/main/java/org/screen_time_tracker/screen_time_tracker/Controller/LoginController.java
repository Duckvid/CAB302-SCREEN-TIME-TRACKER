package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.SQlite_Screen_Time_data;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.Screen_time_tracking_feature;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
            SQlite_Screen_Time_data screenTimeData;
            try {
                screenTimeData = new SQlite_Screen_Time_data();
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
                screenTimeData.InsertScreenTimeData(Start_Time, strDate, UserID);
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

                    // update the db with the current duration
                    screenTimeData.UpdateScreenTimeData(Screen_Time_ID, duration);

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
