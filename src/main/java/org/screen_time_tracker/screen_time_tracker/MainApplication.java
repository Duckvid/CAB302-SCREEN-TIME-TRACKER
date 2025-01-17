package org.screen_time_tracker.screen_time_tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Main application class that launches the user interface for the screen time tracker.
 */
public class MainApplication extends Application {

    /**
     * The width of the main application window
     */
    public static final int WIDTH = 1000;

    /**
     *  The Height of the main application window
     */
    public static final int HEIGHT = 1000;

    /**
     * The Title of the main application window
     */
    public static final String TITLE = "Screen Time Tracker";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Main_style.css").toExternalForm());
        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Main method to launch the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}