package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.*;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Repository.UserRepository;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StartUpController implements Initializable{
    @FXML
    private AnchorPane loginPane, signUpPane, otpPane;
    @FXML private ImageView logo;
    @FXML private VBox containerRight, loginError, signUpError;
    @FXML private TextField usernameSU, emailSU, usernameLI;
    @FXML private PasswordField passwordSU, conPasswordSU, passwordLI;
    @FXML private Hyperlink signUpHyperLink;
    @FXML private Text usernameSUMessage, emailSUMessage, passwordSUMessage, confirmPasswordSUMessage;
    @FXML private Text loginErrorMessage,signUpErrorMessage;
    @FXML private Button nextSignUpButton;

    private static boolean startup = true;
    private boolean usernameValidate = false, emailValidate = false;
    private boolean passwordValidate = false, confirmPasswordValidate = false;

    Animation animation = new Animation();

    private ThreadPool threadPoolLI, threadPoolSUUsername, threadPoolSUEmail, threadPoolSU;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(startup) {
            logo.setTranslateX(300); // Move logo to the centre
            // Set Login container opacity to 0

            animation.fade(logo, 0.15, 5, 1, 0);
            animation.translateLeft(logo, 0.01 ,5.2, -300);
            animation.fade(logo, 0.5, 6, 0, 1);
            animation.fade(containerRight, 0.5, 6, 0, 1);
            animation.fade(loginPane, 0.5, 6, 0, 1);

            startup = false;
        }
        else{
            logo.setOpacity(0);
            animation.fade(logo, 0.5, 0.5, 0, 1);
            animation.fade(containerRight, 0.5, 0.5, 0, 1);
            animation.fade(loginPane, 0.5, 0.5, 0, 1);
        }
        threadPoolLI = new ThreadPool(1, 2);

    }

    // Login Pane

    public void loginButtonClicked(ActionEvent event) throws Exception {
        loginError.setOpacity(1);
        loginError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(4,0,70);");
        loginErrorMessage.setText("Logging in ...");
        threadPoolLI.execute(()-> {
            System.out.println("Checking login credential ...");
            boolean status = Validation.loginUsingUsername(usernameLI.getText(), passwordLI.getText());
            if (status) {
                UserService userService = new UserService();
                System.out.println("Login successful");
                threadPoolLI.stop();
                new UserLogined(userService.findUserInfoByUsername(usernameLI.getText()).getFirst());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new StartUp(event, "/NewsFeedView.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });
            } else {
                loginError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(192, 64, 0);");
                loginErrorMessage.setText("Incorrect username or password");
            }
        });



    }

    public void signUpButtonClicked(ActionEvent event) {
        signUpPane.toFront();
        signUpHyperLink.setVisited(false);
        animation.fade(loginPane, 0.5, 0, 1, 0);
        animation.fade(signUpPane, 0.5, 0.5, 0, 1);
        newSignUpForm();
        loginErrorMessage.setOpacity(0);
        threadPoolLI.stop();
        threadPoolSUUsername = new ThreadPool(1, 1);
        threadPoolSUEmail = new ThreadPool(1, 1);
    }

    // Sign Up Pane

    public void signUpBackButtonClicked(ActionEvent event) {
        loginPane.toFront();
        animation.fade(signUpPane, 0.5, 0, 1, 0);
        animation.fade(loginPane, 0.5, 0.5, 0, 1);
        threadPoolSUUsername.stop();
        threadPoolSUEmail.stop();
    }

    public void signUpNextButtonClicked(ActionEvent event) {
        otpPane.toFront();
        threadPoolSU = new ThreadPool(1, 1);
        animation.fade(signUpPane, 0.5, 0, 1, 0);
        animation.fade(otpPane, 0.5, 0.5, 0, 1);
        threadPoolSUUsername.stop();
        threadPoolSUEmail.stop();
        signUpError.setOpacity(0);
    }

    public void usernameSUValidation(KeyEvent event) throws Exception {
        usernameValidate = false;
        threadPoolSUUsername.setUpdateFlag(false);
        if(usernameSU.getText().isBlank()){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username could not be blank");
        }
        else if(Validation.checkUsernameContainReservedID(usernameSU.getText())){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username should not contains reserved id \"admin\"");
        }
        else if(Validation.checkUsernameContainSpace(usernameSU.getText())){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username should not contains space");
        }
        else if(Validation.checkUsernameContainSpecialChar(usernameSU.getText())){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username could contains '_' only");

        }
        else if(Validation.checkUsernameLengthMin(usernameSU.getText())){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username should not less than 8 characters");
        }
        else if(Validation.checkUsernameLengthMax(usernameSU.getText())){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username should not more than 18 characters");
        }
        else {
            threadPoolSUUsername.setUpdateFlag(true);
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Checking username availability ...");
            threadPoolSUUsername.execute(()->{
                System.out.println("Checking username availability ...");
                try {
                    boolean status = Validation.checkUsernameExisted(usernameSU.getText());
                    if(threadPoolSUUsername.getQueueSize() == 0 && threadPoolSUUsername.getUpdateFlag()) {
                        if (status) {
                            usernameSU.setId("rejectedTFInput");
                            usernameSUMessage.setId("errorMessage");
                            usernameSUMessage.setText("Username existed");
                        } else {
                            usernameSU.setId("acceptedTFInput");
                            usernameSUMessage.setText("Username");
                            usernameSUMessage.setId("passMessage");
                            usernameValidate = true;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                unlockNextButton();
            });


        }
    }

    public void emailSUValidation(KeyEvent event) throws Exception {
        emailValidate = false;
        threadPoolSUEmail.setUpdateFlag(false);
        unlockNextButton();
        if(emailSU.getText().isBlank()){
            emailSU.setId("rejectedTFInput");
            emailSUMessage.setId("errorMessage");
            emailSUMessage.setText("Email Address could not be blank");
        }
        else if(!Validation.checkEmailFormat(emailSU.getText())){
            emailSU.setId("rejectedTFInput");
            emailSUMessage.setId("errorMessage");
            emailSUMessage.setText("Invalid email address");
        }
        else{
            threadPoolSUEmail.setUpdateFlag(true);
            emailSU.setId("rejectedTFInput");
            emailSUMessage.setId("errorMessage");
            emailSUMessage.setText("Checking email address ...");
            threadPoolSUEmail.execute(()->{
                System.out.println("Checking email address ...");
                boolean status = Validation.checkEmailExisted(emailSU.getText());
                if(threadPoolSUEmail.getQueueSize() == 0 && threadPoolSUEmail.getUpdateFlag()) {
                    if (status) {
                        emailSU.setId("rejectedTFInput");
                        emailSUMessage.setId("errorMessage");
                        emailSUMessage.setText("Email address existed. Login Instead");
                    } else {
                        emailSU.setId("acceptedTFInput");
                        emailSUMessage.setText("Email Address");
                        emailSUMessage.setId("passMessage");
                        emailValidate = true;
                    }
                    unlockNextButton();
                }
            });
        }
    }

    public void passwordSUValidation(KeyEvent event){
        passwordValidate = false;
        if(!conPasswordSU.getText().isBlank()){
            conPasswordSU.clear();
            conPasswordSU.setId("rejectedTFInput");
            confirmPasswordSUMessage.setId("errorMessage");
            confirmPasswordSUMessage.setText("Please re-enter password");
        }

        if(passwordSU.getText().isBlank()){
            passwordSU.setId("rejectedTFInput");
            passwordSUMessage.setText("Password should not be blank");
            passwordSUMessage.setId("errorMessage");
        }
        else{
            boolean length = Validation.checkPasswordLengthMin(passwordSU.getText());
            boolean lowercase = Validation.checkPasswordContainsLowercase(passwordSU.getText());
            boolean uppercase = Validation.checkPasswordContainsUppercase(passwordSU.getText());
            boolean digit = Validation.checkPasswordContainsDigit(passwordSU.getText());
            boolean specialChar = Validation.checkPasswordContainsSpecialCHar(passwordSU.getText());

            if(length || !lowercase || !uppercase || !digit || !specialChar) {
                String errorMessage = "Password should ";
                if(length){
                    errorMessage += "not less than 8 characters, ";
                }

                if(!lowercase || !uppercase || !digit || !specialChar){
                    errorMessage += "contains at least one ";
                }
                if(!lowercase){
                    errorMessage += "lowercase letter, ";
                }
                if(!uppercase){
                    errorMessage += "uppercase letter, ";
                }
                if(!digit){
                    errorMessage += "digit, ";
                }
                if(!specialChar){
                    errorMessage += "special character, ";
                }


                passwordSU.setId("rejectedTFInput");
                passwordSUMessage.setText(errorMessage.substring(0, errorMessage.length() - 2));
                passwordSUMessage.setId("errorMessage");
            }
            else{
                passwordSU.setId("acceptedTFInput");
                passwordSUMessage.setText("Password");
                passwordSUMessage.setId("passMessage");
                passwordValidate = true;
            }
            unlockNextButton();

        }
    }

    public void confirmPasswordSUValidation(KeyEvent event) {
        confirmPasswordValidate = false;
        if(conPasswordSU.getText().isBlank()){
            conPasswordSU.setId("rejectedTFInput");
            confirmPasswordSUMessage.setText("Confirm password should not be blank");
            confirmPasswordSUMessage.setId("errorMessage");
        }
        else if(!Validation.checkPasswordMatch(passwordSU.getText(), conPasswordSU.getText())){
            conPasswordSU.setId("rejectedTFInput");
            confirmPasswordSUMessage.setText("Password does not match");
            confirmPasswordSUMessage.setId("errorMessage");
        }
        else{
            conPasswordSU.setId("acceptedTFInput");
            confirmPasswordSUMessage.setText("Confirm Password");
            confirmPasswordSUMessage.setId("passMessage");
            confirmPasswordValidate = true;
        }
        unlockNextButton();
    }

    public void unlockNextButton(){
        nextSignUpButton.setDisable(!usernameValidate || !emailValidate || !passwordValidate || !confirmPasswordValidate);
    }

    // Sign Up Otp Pane

    public void otpBackButtonClicked(ActionEvent event) {
        signUpPane.toFront();
        threadPoolSUUsername = new ThreadPool(1,1);
        threadPoolSUEmail = new ThreadPool(1,1);
        animation.fade(otpPane, 0.5, 0, 1, 0);
        animation.fade(signUpPane, 0.5, 0.5, 0, 1);
    }

    public void otpVerifyButtonClicked(ActionEvent event) throws Exception {
        UserService userService = new UserService();
        signUpError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(4,0,70);");
        signUpErrorMessage.setText("Signing up ...");
        signUpError.setOpacity(1);
        threadPoolSU.execute(()->{
            System.out.println("Checking register status ...");
            boolean status = userService.registerNewUser(usernameSU.getText(), emailSU.getText(), passwordSU.getText()) == 1;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(status) {

                        loginPane.toFront();
                        animation.fade(otpPane, 0.5, 0, 1, 0);
                        animation.fade(loginPane, 0.5, 0.5, 0, 1);
                        threadPoolLI = new ThreadPool(1, 1);
                        loginErrorMessage.setText("Sign up successfully");
                        loginError.setOpacity(1);
                    } else {
                        signUpErrorMessage.setText("Error when signing up");
                        signUpError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(192, 64, 0);");
                    }
                }

            });

        });

    }

    // General

    public void newSignUpForm(){
        usernameSU.clear();
        emailSU.clear();
        passwordSU.clear();
        conPasswordSU.clear();
        usernameSU.setId("TFInput");
        emailSU.setId("TFInput");
        passwordSU.setId("TFInput");
        conPasswordSU.setId("TFInput");
        usernameSUMessage.setText("");
        emailSUMessage.setText("");
        passwordSUMessage.setText("");
        confirmPasswordSUMessage.setText("");
        unlockNextButton();
    }


}
