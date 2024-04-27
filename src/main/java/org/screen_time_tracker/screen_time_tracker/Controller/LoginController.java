package org.screen_time_tracker.screen_time_tracker.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.screen_time_tracker.screen_time_tracker.MainApplication;
import org.screen_time_tracker.screen_time_tracker.Model.SQLiteUserDAO;
import org.screen_time_tracker.screen_time_tracker.Model.User.User;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML
    private Button Signup;

    @FXML
    private Button Loginbtn;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField PasswordField;

    private Button activeButton = null;

    @FXML
    private Button forgotPasswordbtn;

    @FXML
    protected void OnForgotPasswordbtnClick() throws IOException{

        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Retrieve Password");

        // set the button types
        ButtonType submitbuttontype = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitbuttontype, ButtonType.CANCEL);

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField textField = new TextField();
        textField.setPromptText("Email/Phone number");

        vbox.getChildren().add(new Label("Please enter your email or phone number:"));
        vbox.getChildren().add(textField);

        dialog.getDialogPane().setContent(vbox);

        Platform.runLater(textField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == submitbuttontype){
                return textField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(emailOrPhone -> {
            // handle the retrieval of the password here
        });
    }


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
        SQLiteUserDAO dao = new SQLiteUserDAO();

        // this is some simple input validation to ensure that the input fields cannot be null
        // This will output a simple alert type popup to notify users to fix their input
        if(email.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Input validation Error");
            alert.setContentText("Please enter all fields None of the fields can be empty");
            alert.showAndWait();
        }

        // this condition validates to make sure the password is correct and contains the neccessary characters
        else if(!dao.IsPasswordCorrect(password)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Input validation Error");
            alert.setContentText("Please make sure that your password contains 8 character, 1 special character, 1 number and 1 capitcal letter.");
            alert.showAndWait();
        }

        else if(!dao.IsEmailCorrect(email)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Input validation Error");
            alert.setContentText("Please make sure that your email is in the correct form.");
            alert.showAndWait();
        }
        else {

            dao = new SQLiteUserDAO();
            User user = dao.Login(email, password);
            // user is found, navigate to home page once implmented but for now go to currentsession page
            Stage stage = (Stage) Loginbtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), MainApplication.WIDTH, MainApplication.HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/org/screen_time_tracker/screen_time_tracker/styles/Home_style.css").toExternalForm());
            stage.setResizable(false);
            stage.setScene(scene);
        }

    }

}
