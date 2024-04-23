package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.HelloApplication;

import java.io.IOException;

public class HelloController {

    @FXML
    private Button SubmitButton;


    @FXML
    protected void OnSubmitButtonClick() throws IOException{
        Stage stage = (Stage) SubmitButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("current_Session-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/current_Session_style.css").toExternalForm());
        stage.setScene(scene);

    }




}