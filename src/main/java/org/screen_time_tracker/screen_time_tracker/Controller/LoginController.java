package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteScreenTimeDAO;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button Signup;

    @FXML
    private Button Loginbtn;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField PasswordField;

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

        // validate input here

        SQLiteScreenTimeDAO dao = new SQLiteScreenTimeDAO();
        User user = dao.Login(email, password);

        if(user != null){
            // user is found, navigate to home page once implmented but for now go to currentsession page
            Stage stage = (Stage) Loginbtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("current_Session-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/current_Session_style.css").toExternalForm());
            stage.setResizable(false);
            stage.setScene(scene);
        }
    }

}
