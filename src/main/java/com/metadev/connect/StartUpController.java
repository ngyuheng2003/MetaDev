package com.metadev.connect;

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
public class StartUpController implements Initializable {
    @FXML
    private AnchorPane loginPane, signUpPane, otpPane;
    @FXML private ImageView logo;
    @FXML private VBox containerRight;
    @FXML private TextField usernameSU, emailSU;
    @FXML private PasswordField passwordSU, conPasswordSU;
    @FXML private Hyperlink signUpHyperLink;
    @FXML private Text usernameSUMessage, emailSUMessage, passwordSUMessage, confirmPasswordSUMessage;
    @FXML private Button nextSignUpButton;

    private static boolean startup = true;
    private boolean usernameValidate = false, emailValidate = false;
    private boolean passwordValidate = false, confirmPasswordValidate = false;

    Animation animation = new Animation();

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

    public void loginButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/NewsFeedView.fxml");
    }

    public void signUpButtonClicked(ActionEvent event) {
        signUpPane.toFront();
        signUpHyperLink.setVisited(false);
        animation.fade(loginPane, 0.5, 0, 1, 0);
        animation.fade(signUpPane, 0.5, 0.5, 0, 1);
        newSignUpForm();
    }

    // Sign Up Pane

    public void signUpBackButtonClicked(ActionEvent event) {
        loginPane.toFront();
        animation.fade(signUpPane, 0.5, 0, 1, 0);
        animation.fade(loginPane, 0.5, 0.5, 0, 1);
    }

    public void signUpNextButtonClicked(ActionEvent event) {
        otpPane.toFront();
        animation.fade(signUpPane, 0.5, 0, 1, 0);
        animation.fade(otpPane, 0.5, 0.5, 0, 1);
    }

    public void usernameSUValidation(KeyEvent event) {
        usernameValidate = false;
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
        else if(Validation.checkUsernameExisted(usernameSU.getText())){
            usernameSU.setId("rejectedTFInput");
            usernameSUMessage.setId("errorMessage");
            usernameSUMessage.setText("Username existed");
        }
        else{
            usernameSU.setId("acceptedTFInput");
            usernameSUMessage.setText("Username");
            usernameSUMessage.setId("passMessage");
            usernameValidate = true;
        }
        unlockNextButton();
    }

    public void emailSUValidation(KeyEvent event) {
        emailValidate = false;
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
        else if(Validation.checkEmailExisted(emailSU.getText())){
            emailSU.setId("rejectedTFInput");
            emailSUMessage.setId("errorMessage");
            emailSUMessage.setText("Email address existed. Login Instead");
        }
        else{
            emailSU.setId("acceptedTFInput");
            emailSUMessage.setText("Email Address");
            emailSUMessage.setId("passMessage");
            emailValidate = true;
        }
        unlockNextButton();
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
        animation.fade(otpPane, 0.5, 0, 1, 0);
        animation.fade(signUpPane, 0.5, 0.5, 0, 1);
    }

    public void otpVerifyButtonClicked(ActionEvent event) {
        loginPane.toFront();
        animation.fade(otpPane, 0.5, 0, 1, 0);
        animation.fade(loginPane, 0.5, 0.5, 0, 1);
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
