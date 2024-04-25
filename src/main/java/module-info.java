module org.screen_time_tracker.screen_time_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires java.sql;


    opens org.screen_time_tracker.screen_time_tracker to javafx.fxml;
    exports org.screen_time_tracker.screen_time_tracker;

    exports org.screen_time_tracker.screen_time_tracker.Controller;
    opens org.screen_time_tracker.screen_time_tracker.Controller to javafx.fxml;


}
