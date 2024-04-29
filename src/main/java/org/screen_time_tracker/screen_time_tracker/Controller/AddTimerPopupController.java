package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTimerPopupController {

    @FXML
    private TextField timerNameField;

    public String getTimerName() {
        return timerNameField.getText();
    }

    public void saveTimer() {
        String timerName = getTimerName();
        // Save the timer here
        hideAddTimerPopup();
    }

    public void hideAddTimerPopup() {
        timerNameField.getScene().getWindow().hide();
    }
}
