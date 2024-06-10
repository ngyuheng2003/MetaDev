package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.*;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Email.EmailAPI;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StartUpController implements Initializable{
    @FXML
    private AnchorPane loginPane, signUpPane, otpPane, forgotPasswordPane;
    @FXML private ImageView logo;
    @FXML private VBox containerRight, loginError, signUpError;
    @FXML private TextField usernameSU, emailSU, usernameLI, signupOTPInput;
    @FXML private PasswordField passwordSU, conPasswordSU, passwordLI;
    @FXML private Hyperlink signUpHyperLink;
    @FXML private Text usernameSUMessage, emailSUMessage, passwordSUMessage, confirmPasswordSUMessage;
    @FXML private Text loginErrorMessage,signUpErrorMessage;
    @FXML private Button nextSignUpButton, loginButton;
    private int attempt = 3;
    private static boolean startup = true;
    private boolean usernameValidate = false, emailValidate = false;
    private boolean passwordValidate = false, confirmPasswordValidate = false;

    Animation animation = new Animation();

    private ThreadPool threadPoolSUUsername, threadPoolSUEmail, threadPoolSU;
    int otpType;

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
    }

    // Login Pane

    public void loginButtonClicked(ActionEvent event) throws Exception {
        otpType = 0;
        loginError.setOpacity(1);
        loginButton.setDisable(true);
        // Checking whether any input is blank
        if(usernameLI.getText().isBlank() || passwordLI.getText().isBlank()){
            loginError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(192, 64, 0);");
            loginErrorMessage.setText("Username or password shouldn't be blank");
            System.out.println("ERROR: Input is blanked");
            loginButton.setDisable(false);
        }
        else{
            // Telling the user the program is logging the user in
            loginError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(4,0,70); -fx-font-size: 18px");
            loginErrorMessage.setText("Logging in ...");
            // Creating a thread for checking credentials in database
            ThreadPool threadPoolLI = new ThreadPool(1, 2);
            // Using the thread to search in the database
            threadPoolLI.execute(() -> {
                System.out.println("LOGIN: Checking login credential ...");
                // Get login status based on user input
                boolean status = Validation.loginUsingUsername(usernameLI.getText(), passwordLI.getText());
                // Using result from database to decide next step
                if (status) {
                    // Allow user to enter the program
                    UserService userService = new UserService();
                    System.out.println("LOGIN: Login successful");
                    new UserLogined(userService.findUserInfoByUsername(usernameLI.getText()).getFirst());
                    int otpA = userService.fetch2FA(UserLogined.getUserId()).getFirst();
                    if(otpA == 1)
                        sendOTPEmail2(UserLogined.getEmail());
                    threadPoolSU = new ThreadPool(1,1);
                    // Switching to the next scene
                    Platform.runLater(() -> {
                        try {
                            if(UserLogined.getStatus() == 0){
                                new StartUp(event, "/FXMLView/SettingView.fxml");
                            }
                            else {
                                if(otpA ==1){
                                    otpPane.toFront();
                                    otpPane.setOpacity(1);
                                    loginPane.setOpacity(0);
                                    signUpError.setOpacity(0);
                                    attempt = 3;
                                    signupOTPInput.clear();
                                }
                                else {
                                    new StartUp(event, "/FXMLView/NewsFeedView.fxml");
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    // Prompt user that the input from them is wrong and need to try again
                    loginError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(192, 64, 0);");
                    loginErrorMessage.setText("Incorrect username or password");
                    loginButton.setDisable(false);
                    System.out.println("ERROR: Incorrect credentials");
                }
                // Terminating the thread after finish executing
                threadPoolLI.stop();
            });
        }
    }

    public void signUpButtonClicked(ActionEvent event) {
        signUpPane.toFront();
        signUpHyperLink.setVisited(false);
        animation.fade(loginPane, 0.5, 0, 1, 0);
        animation.fade(signUpPane, 0.5, 0.5, 0, 1);
        newSignUpForm();
        threadPoolSUUsername = new ThreadPool(1, 1);
        threadPoolSUEmail = new ThreadPool(1, 1);
    }

    // Sign Up Pane

    public void signUpBackButtonClicked(ActionEvent event) {
        // Switching back to login pane
        loginPane.toFront();
        loginError.setOpacity(0);
        animation.fade(signUpPane, 0.5, 0, 1, 0);
        animation.fade(loginPane, 0.5, 0.5, 0, 1);
        // Terminating thread
        threadPoolSUUsername.stop();
        threadPoolSUEmail.stop();

    }

    public void signUpNextButtonClicked(ActionEvent event) {
        otpType = 1;
        otpPane.toFront();
        threadPoolSU = new ThreadPool(1, 1);
        animation.fade(signUpPane, 0.5, 0, 1, 0);
        animation.fade(otpPane, 0.5, 0.5, 0, 1);
        threadPoolSUUsername.stop();
        threadPoolSUEmail.stop();
        signUpError.setOpacity(0);
        attempt = 3;
        sendOTPEmail(emailSU.getText());
        signupOTPInput.clear();
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
        if (!(Validation.verifyOTP(Integer.parseInt(signupOTPInput.getText())))) {
            signUpError.setOpacity(1);
            attempt--;
            signUpErrorMessage.setText("Invalid verification code. " + attempt + " attempt(s) left");
            if (attempt == 0) {
                loginPane.toFront();
                animation.fade(otpPane, 0.5, 0, 1, 0);
                animation.fade(loginPane, 0.5, 0.5, 0, 1);
                loginErrorMessage.setText("Too many wrong attempts");
                loginError.setOpacity(1);
            }
        }else {
            if(otpType ==1) {
                threadPoolSU.execute(() -> {
                    System.out.println("Checking register status ...");
                    boolean status = userService.registerNewUser(usernameSU.getText(), emailSU.getText(), passwordSU.getText()) == 1;
                    Platform.runLater(() -> {
                        if (status) {
                            loginPane.toFront();
                            animation.fade(otpPane, 0.5, 0, 1, 0);
                            animation.fade(loginPane, 0.5, 0.5, 0, 1);
                            loginErrorMessage.setText("Sign up successfully");
                            loginError.setOpacity(1);
                        } else {
                            signUpErrorMessage.setText("Error when signing up");
                            signUpError.setStyle("-fx-background-radius: 10; -fx-background-color: rgb(192, 64, 0);");
                        }

                    });


                });
            }else{
                threadPoolSU.execute(() -> {
                    Platform.runLater(() -> {
                        try {
                            new StartUp(event, "/FXMLView/NewsFeedView.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            }
        }
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

    public void sendOTPEmail(String emailSU) {
        String subject = "Verification Code - New User Sign Up";
        int otpCode = Validation.generateOTP();
        String html = "<div class=\"container\" style=\"border-radius: 15px; ;background-color: #0b2e59; width: 300px; height: 400px; margin: 150px auto; padding: 30px;\">\n" +
                "        <img src=\"https://1drv.ms/i/c/8e89b564baebfc78/IQPek6lFrjkBTo63-YCOry88Aacals08rzgKLamzhulEumQ?width=1024\" style=\"height: 30px;margin-top: 10px;margin-bottom: 10px\">\n" +
                "        <h1 style=\"color: white; font-family: 'Arial';margin-top: 30px; font-size: 30px\">\n" +
                "            One-Time-Password\n" +
                "        </h1>\n" +
                "        <p style=\"color: white; font-family: 'Arial'; font-size: 18px;\">\n" +
                "            This is your verification code:\n" +
                "        </p>\n" +
                "        <p style=\"color: white; font-family: 'Arial'; font-size: 35px; text-align: center;margin-top: 0\">"+otpCode+"</p>\n" +
                "        <p style=\"color: white; font-family: 'Arial'; font-size: 18px; align-items: center\">Do not share this code with anyone else</p>\n" +
                "        <p style=\"margin-top: 40px; color: white; font-family: 'Arial'; font-size: 18px\">\n" +
                "            MetaDev Team\n" +
                "        </p>\n" +
                "    </div>";
        EmailAPI.sendEmail(emailSU, subject, html);
    }

    public void forgotPasswordBackButtonClicked(ActionEvent event){
        // Switching back to login pane
        loginPane.toFront();
        loginError.setOpacity(0);
        animation.fade(forgotPasswordPane, 0.5, 0, 1, 0);
        animation.fade(loginPane, 0.5, 0.5, 0, 1);
    }

    public void sendOTPEmail2(String email) {
        String subject = "Verification Code - Two Factor Authentication";
        int otpCode = Validation.generateOTP();
        String html = "<div class=\"container\" style=\"border-radius: 15px; ;background-color: #0b2e59; width: 300px; height: 400px; margin: 150px auto; padding: 30px;\">\n" +
                "        <img src=\"https://1drv.ms/i/c/8e89b564baebfc78/IQPek6lFrjkBTo63-YCOry88Aacals08rzgKLamzhulEumQ?width=1024\" style=\"height: 30px;margin-top: 10px;margin-bottom: 10px\">\n" +
                "        <h1 style=\"color: white; font-family: 'Arial';margin-top: 30px; font-size: 30px\">\n" +
                "            One-Time-Password\n" +
                "        </h1>\n" +
                "        <p style=\"color: white; font-family: 'Arial'; font-size: 18px;\">\n" +
                "            This is your verification code:\n" +
                "        </p>\n" +
                "        <p style=\"color: white; font-family: 'Arial'; font-size: 35px; text-align: center;margin-top: 0\">"+otpCode+"</p>\n" +
                "        <p style=\"color: white; font-family: 'Arial'; font-size: 18px; align-items: center\">Do not share this code with anyone else</p>\n" +
                "        <p style=\"margin-top: 40px; color: white; font-family: 'Arial'; font-size: 18px\">\n" +
                "            MetaDev Team\n" +
                "        </p>\n" +
                "    </div>";
        EmailAPI.sendEmail(email, subject, html);
    }


    public void forgotPasswordLinkClicked(ActionEvent event) {
        // Switching back to login pane
        forgotPasswordPane.toFront();
        loginError.setOpacity(0);
        animation.fade(loginPane, 0.5, 0, 1, 0);
        animation.fade(forgotPasswordPane, 0.5, 0.5, 0, 1);
    }
}
