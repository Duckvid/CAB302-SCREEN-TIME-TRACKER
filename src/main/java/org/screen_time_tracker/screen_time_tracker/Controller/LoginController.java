package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button Signup;

    @FXML
    protected void OnSignupButtonClick() throws IOException {
        Stage stage = (Stage) Signup.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Main_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

}
