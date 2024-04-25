package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.CurrentSession.WindowInfo;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteScreenTimeDAO;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;
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
        Thread thread = new Thread(() -> {
            WindowInfo widowinfo = new WindowInfo();
            try {
                while (!Thread.interrupted()) {
                    Map<String, Long> windowTimes = widowinfo.getWindowTimeMap();
                    //windowTimes.forEach((title, time) -> System.out.println(title + ": " + (time / 1000) + "s " + "Current Date and time is: " + widowinfo.CurrentDateTime()));
                    Thread.sleep(1000);
                }
            }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();

                }

        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void HandleSignUpAction(ActionEvent event) throws IOException {
        String name = NameField.getText();
        String email = EmailField.getText();
        String Password = PasswordField.getText();
        String Phonenumber = PhoneField.getText();

        // validate input to make sure its not empty
        User newUser = new User(name, email, Password, Phonenumber);
        SQLiteScreenTimeDAO dao = new SQLiteScreenTimeDAO();
        dao.RegisterAccount(newUser);

        // navigate to the home page once implmented but for now goto currentSession page
        Stage stage = (Stage) signupbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("current_Session-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/current_Session_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
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