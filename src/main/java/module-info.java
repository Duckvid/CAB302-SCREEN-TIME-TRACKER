module org.screen_time_tracker.screen_time_tracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.screen_time_tracker.screen_time_tracker to javafx.fxml;
    exports org.screen_time_tracker.screen_time_tracker;
}