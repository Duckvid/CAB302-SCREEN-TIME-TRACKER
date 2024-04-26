package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;

import java.io.IOException;

public class Settings_Controller {

    @FXML
    private Button btnAccountInfo;

    @FXML
    private Button btnSecurity;

    @FXML
    private Button btnNotifications;

    @FXML
    private Button btnPermissions;

    @FXML
    private Button btnAppearance;

    private Button activeButton = null;

    @FXML
    private Button settingsPage;

    @FXML
    private Button Recommendationspage;

    @FXML Button TimersPage;

    @FXML Button CurrentSessionPage;

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

    public void handleAccountInfo(ActionEvent actionEvent) throws IOException  {
        Onclickofanybtn(btnAccountInfo);
    }

    public void handleSecurity(ActionEvent actionEvent) throws IOException {
        Onclickofanybtn(btnSecurity);
    }

    public void handleNotifications(ActionEvent actionEvent) throws IOException  {
        Onclickofanybtn(btnNotifications);
    }

    public void handlePermissions(ActionEvent actionEvent) throws IOException {
        Onclickofanybtn(btnPermissions);
    }

    public void handleAppearance(ActionEvent actionEvent) throws IOException {
        Onclickofanybtn(btnAppearance);
    }


    @FXML
    protected void Onclickofanybtn(Button button) throws IOException{
        if(activeButton != null){
            activeButton.getStyleClass().remove("button-active");
        }
        button.getStyleClass().add("button-active");
        activeButton = button;
    }


}
