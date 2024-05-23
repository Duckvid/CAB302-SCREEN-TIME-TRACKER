/**
 * This module defines the structure and dependencies for the Screen Time Tracker application.
 * It includes necessary JavaFX components for the user interface, JNA for native access,
 * and JDBC for database interactions. This module configuration ensures that all parts
 * of the application are properly exposed and available to the JavaFX runtime, as well
 * as to the testing frameworks used.
 */

module org.screen_time_tracker.screen_time_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires java.sql;
    requires java.desktop;


    opens org.screen_time_tracker.screen_time_tracker to javafx.fxml;
    exports org.screen_time_tracker.screen_time_tracker;

    exports org.screen_time_tracker.screen_time_tracker.Controller;
    opens org.screen_time_tracker.screen_time_tracker.Controller to javafx.fxml;

    // Exports the Model package so it can be accessed by other modules
    exports org.screen_time_tracker.screen_time_tracker.Model;

    // If your tests use reflection (e.g., Mockito, JUnit), you need to open the Model package to unnamed module
    exports org.screen_time_tracker.screen_time_tracker.Model.User;
    opens org.screen_time_tracker.screen_time_tracker.Model to javafx.fxml, org.junit.jupiter.api;
    exports org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature;
    opens org.screen_time_tracker.screen_time_tracker.Model.ScreenTimeTrackingFeature to javafx.fxml, org.junit.jupiter.api;

}