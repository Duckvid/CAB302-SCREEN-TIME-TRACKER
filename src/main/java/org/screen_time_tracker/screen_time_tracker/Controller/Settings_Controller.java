package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import java.io.IOException;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.screen_time_tracker.screen_time_tracker.Model.User.Session_Manager;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;

/**
 * Controller for managing application settings.
 * Provides functionality to manage account information, appearance settings, security settings, and more.
 */
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

    @FXML
    private Button TimersPage;

    @FXML
    private Button CurrentSessionPage;

    @FXML
    private Button Homebtn;

    @FXML
    private Button Contactbtn;

    @FXML
    private ImageView imgview;

    @FXML
    private Button Logoutbtn;

    /**
     * Initializes the controller. This method sets up necessary state and UI components
     * for the current session view.
     */
    @FXML
    private Label settingsTitleLabel;

    @FXML
    private VBox notificationsContainer;

    @FXML
    private VBox appearanceContainer;

    @FXML
    private VBox accountInfoContainer;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    public void initialize() throws IOException {
        // Other initialization code...
        // Fetch the current user from the session manager
        User currentUser = Session_Manager.getCurrentUser();
        if (currentUser != null) {
            // Populate the UI fields with the user's information
            nameLabel.setText(currentUser.getName());
            emailLabel.setText(currentUser.getEmail());
            passwordLabel.setText("******"); // You might want to avoid displaying the password directly
        }
    }



    /**
     * Handles user logout events. This method is called when the logout button is clicked
     * and is responsible for logging out the user and transitioning to the login screen.
     *
     * @throws IOException if an I/O error occurs when loading the login view
     */
    @FXML
    protected void OnLogoutBtnClick() throws IOException{
        SQLiteUserDAO sqLiteUserDAO = new SQLiteUserDAO();
        sqLiteUserDAO.Logout();
        Stage stage = (Stage) Logoutbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Login_Styles.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);

    }

    /**
     * Handles navigation to the home page events. This method is called when the Home button is clicked
     * and is responsible for navigation the user to the home page
     * @throws IOException if an I/O error occurs when loading the Home page view
     */
    @FXML
    public void OnHomebtnClick() throws IOException {
        Stage stage = (Stage) Homebtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Home_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    @FXML
    protected void OnSettingsButtonClick() throws IOException {
        Stage stage = (Stage) settingsPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Settings.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Settings_Style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the recommendation page events. This method is called when the recommendation button is clicked
     * and is responsible for navigation the user to the recommendation page
     * @throws IOException if an I/O error occurs when loading the Recommendations page view
     */
    @FXML
    public void OnRecommendationsPageClick() throws IOException {
        Stage stage = (Stage) Recommendationspage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Recommendations-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Recommendations.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the Timers page events. This method is called when the Timers button is clicked
     * and is responsible for navigation of the user to the Timeers page
     * @throws IOException if an I/O error occurs when loading the Timers page view
     */
    @FXML
    public void OnTimersButtonClick() throws IOException {
        Stage stage = (Stage) TimersPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Timers-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Timers_Style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles navigation to the current session page events. This method is called when the current session button is clicked
     * and is responsible for navigation of the user to the current session page
     * @throws IOException if an I/O error occurs when loading the current session page view
     */
    @FXML
    public void OnCurrentSessionBtnClick() throws IOException {
        Stage stage = (Stage) CurrentSessionPage.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("current_Session-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/current_Session_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }
    /**
     * Handles navigation to the contact page events. This method is called when the contact button is clicked
     * and is responsible for navigation of the user to the contacts page
     * @throws IOException if an I/O error occurs when loading the Contact page view
     */
    @FXML
    public void OnContactBtnClick() throws IOException {
        Stage stage = (Stage) Contactbtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Contact-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Contact_style.css").toExternalForm());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    /**
     * Handles the action to manage account information.
     *
     * @param actionEvent The event triggered by clicking the Account Info button.
     * @throws IOException if an error occurs during navigation.
     */
    public void handleAccountInfo(ActionEvent actionEvent) throws IOException {
        setActiveButton(btnAccountInfo, "Account info");
        showChecklist(accountInfoContainer);

    }

    /**
     * Handles the action to configure security settings.
     *
     * @param actionEvent The event triggered by clicking the Security button.
     * @throws IOException if an error occurs during navigation.
     */
    public void handleSecurity(ActionEvent actionEvent) throws IOException {
        setActiveButton(btnSecurity, "Security");
        hideAllChecklists();
    }


    /**
     * Handles the action to manage notification settings.
     *
     * @param actionEvent The event triggered by clicking the Notifications button.
     * @throws IOException if an error occurs during navigation.
     */

    public void handleNotifications(ActionEvent actionEvent) throws IOException {
        setActiveButton(btnNotifications, "Notifications");
        showChecklist(notificationsContainer);

    }

    /**
     * Handles the action to adjust permissions settings.
     *
     * @param actionEvent The event triggered by clicking the Permissions button.
     * @throws IOException if an error occurs during navigation.
     */

    public void handlePermissions(ActionEvent actionEvent) throws IOException {
        setActiveButton(btnPermissions, "Permissions");
        hideAllChecklists();
    }

    /**
     * Handles the action to customize appearance settings.
     *
     * @param actionEvent The event triggered by clicking the Appearance button.
     * @throws IOException if an error occurs during navigation.
     */
    public void handleAppearance(ActionEvent actionEvent) throws IOException {
        setActiveButton(btnAppearance, "Appearance");
        showChecklist(appearanceContainer);
    }

    /**
     * Handles button click events by applying an active style to the button.
     *
     * @param button The button that was clicked.
     * @throws IOException if an error occurs when applying the style.
     */

    @FXML
    protected void setActiveButton(Button button, String title) {
        if (button == null) {
            System.err.println("Button is null, cannot set active button.");
            return;
        }
        if (activeButton != null) {
            activeButton.getStyleClass().remove("button-active");
        }
        button.getStyleClass().add("button-active");
        activeButton = button;
        settingsTitleLabel.setText(title);
    }

    private void showChecklist(VBox checklistContainer) {
        hideAllChecklists();
        checklistContainer.setVisible(true);
    }

    private void hideAllChecklists() {
        accountInfoContainer.setVisible(false);
        notificationsContainer.setVisible(false);
        appearanceContainer.setVisible(false);
    }

}