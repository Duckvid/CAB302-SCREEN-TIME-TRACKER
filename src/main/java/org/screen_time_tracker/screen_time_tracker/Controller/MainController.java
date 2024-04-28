package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.SQlite_Screen_Time_data;
import org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature.Screen_time_tracking_feature;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class MainController {

        @FXML
        private TextField NameField;

        @FXML
        private TextField EmailField;

        @FXML
        private TextField PasswordField;

        @FXML
        private TextField PhoneField;

        @FXML
        private Button signupbtn;

        @FXML
        private Button Login;


        @FXML
        public void initialize(){
            startBackgroundWindowInfo();
        }

        /* This is a function which is responsible for actually controlling how frequently the screen time data is collected */
        private void startBackgroundWindowInfo() {
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
                screenTimeData.InsertScreenTimeData(Start_Time);
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
        private void HandleSignUpAction(ActionEvent event) throws IOException {
            String name = NameField.getText();
            String email = EmailField.getText();
            String Password = PasswordField.getText();
            String Phonenumber = PhoneField.getText();
            SQLiteUserDAO dao = new SQLiteUserDAO();
            // this is some simple input validation to ensure that the input fields cannot be null
            // This will output a simple alert type popup to notify users to fix their input
            if(name.isEmpty() || email.isEmpty() || Password.isEmpty() || Phonenumber.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Input validation Error");
                alert.setContentText("Please enter all fields None of the fields can be empty");
                alert.showAndWait();
            }

            // this condition validates to make sure the password is correct and contains the neccessary characters
            else if(!dao.IsPasswordCorrect(Password)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Input validation Error");
                alert.setContentText("Please make sure that your password contains 8 character, 1 special character, 1 number and 1 capitcal letter.");
                alert.showAndWait();
            }

            else if(!dao.IsEmailCorrect(email)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Input validation Error");
                alert.setContentText("Please make sure that your email is in the correct form.");
                alert.showAndWait();
            }
            else if(!dao.IsNameValid(name)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Input validation Error");
                alert.setContentText("Please make sure that your name is in the correct form.");
                alert.showAndWait();
            }
            else if(!dao.IsNumberValid(Phonenumber)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Input validation Error");
                alert.setContentText("Please make sure that your phonenumber is in the correct form.");
                alert.showAndWait();
            }


            // If their input is not null then a new user is created with the input data and sent to the database
            // The user is then redirected to ideally the homepage but that has not been created yet so for now will just be
            // redirected to the current session page

            else{

                User newUser = new User(name, email, Password, Phonenumber);
                dao.RegisterAccount(newUser);

                // navigate to the home page once implmented but for now goto currentSession page
                Stage stage = (Stage) signupbtn.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Home-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
                scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Home_style.css").toExternalForm());
                stage.setResizable(false);
                stage.setScene(scene);

            }
        }

        @FXML
        protected void OnLoginButtonClick() throws IOException{
            Stage stage = (Stage) Login.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Login_Styles.css").toExternalForm());
            stage.setResizable(false);
            stage.setScene(scene);
        }


}