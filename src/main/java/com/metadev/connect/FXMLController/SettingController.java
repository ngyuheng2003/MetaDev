package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.UserProfile;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Controller.Validation;
import com.metadev.connect.Email.EmailAPI;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML private AnchorPane settingMainPane, settingAboutPane, settingEditProfilePane, settingSnPPane;
    @FXML private AnchorPane settingChangePasswordPane, setting2FAPane, otp2FAPane;
    @FXML private TextField emailTF, otpTF;
    @FXML private Button usernameButton;


    // Edit Profile
    @FXML private TextField usernameEditTF, nameEditTF, bioEditTF, emailEditTF;
    @FXML private Text usernameEditMessage;
    @FXML private Button updateProfileButton;
    @FXML
    private Button topic_1_clicked;

    @FXML
    private Button topic_2_clicked;

    @FXML
    private Button topic_3_clicked;

    @FXML
    private Button topic_4_clicked;

    @FXML
    private Button topic_5_clicked;

    @FXML
    private Button topic_6_clicked;

    @FXML
    private Button topic_7_clicked;

    @FXML
    private Button topic_8_clicked;

    @FXML
    private Button topic_9_clicked;
    private Button[] buttonList;

    private int[] suggested_preferred_topic;

    // Change Password
    @FXML Text passwordCMessage, confirmPasswordCMessage, changePasswordErrorMessage;
    @FXML PasswordField oldPasswordCTF, passwordCTF, confirmPasswordCTF;
    @FXML Button updatePasswordButton;
    @FXML VBox changePasswordError;

    @FXML Text errorMessage, status2FA;
    @FXML Button enableButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        suggested_preferred_topic = UserLogined.getSuggested_preferred_topic();
        buttonList = new Button[]{topic_1_clicked, topic_2_clicked, topic_3_clicked, topic_4_clicked, topic_5_clicked,
                topic_6_clicked, topic_7_clicked, topic_8_clicked, topic_9_clicked};
        for(int i = 0; i < 9; i++){
            if(suggested_preferred_topic[i] == 1){
                buttonList[i].setId("profileSelectedButton");
            }
        }
        usernameButton.setText(UserLogined.getUsername());
        usernameEditTF.setText(UserLogined.getUsername());
        if(UserLogined.getName() != null)
            nameEditTF.setText(UserLogined.getName());
        if(UserLogined.getBio() != null)
            bioEditTF.setText(UserLogined.getBio());
        emailEditTF.setText(UserLogined.getEmail());
        if(UserLogined.getStatus() == 0){
            settingEditProfilePane.toFront();
            settingMainPane.setOpacity(0);
            settingEditProfilePane.setOpacity(1);
            settingMainPane.setDisable(true);
            settingAboutPane.setDisable(true);
            settingEditProfilePane.setDisable(false);
            settingSnPPane.setDisable(true);
            usernameEditTF.setDisable(true);
        }
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        UserProfile.setUser(UserLogined.getUserLogined());
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }
    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/NewsFeedView.fxml");
    }

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    public void backButtonClicked(ActionEvent event) {
        settingMainPane.toFront();
        settingMainPane.setOpacity(1);
        settingAboutPane.setOpacity(0);
        settingEditProfilePane.setOpacity(0);
        settingSnPPane.setOpacity(0);
        settingMainPane.setDisable(false);
        settingAboutPane.setDisable(true);
        settingEditProfilePane.setDisable(true);
        settingSnPPane.setDisable(true);

    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }

    // Setting Main Pane

    public void editProfileButtonClicked(ActionEvent event) {
        settingEditProfilePane.toFront();
        settingMainPane.setOpacity(0);
        settingEditProfilePane.setOpacity(1);
        settingMainPane.setDisable(true);
        settingAboutPane.setDisable(true);
        settingEditProfilePane.setDisable(false);
        settingSnPPane.setDisable(true);
    }

    public void securityAndPrivacyButtonClicked(ActionEvent event) {
        settingSnPPane.toFront();
        settingMainPane.setOpacity(0);
        settingSnPPane.setOpacity(1);
        settingMainPane.setDisable(true);
        settingAboutPane.setDisable(true);
        settingEditProfilePane.setDisable(true);
        settingSnPPane.setDisable(false);
    }

    public void aboutButtonClicked(ActionEvent event) {
        settingAboutPane.toFront();
        settingMainPane.setOpacity(0);
        settingAboutPane.setOpacity(1);
        settingMainPane.setDisable(true);
        settingAboutPane.setDisable(false);
        settingEditProfilePane.setDisable(true);
        settingSnPPane.setDisable(true);
    }

    public void logoutButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/StartUpView.fxml");
    }

    // Edit Profile Pane

    public void updateProfileButtonClicked(ActionEvent event) throws Exception {
        updateProfileButton.setDisable(true);
        UserService userService = new UserService();
        String str  = "";
        for(int a : suggested_preferred_topic){
            str += a + ",";
        }
        ThreadPool threadPoolSetting = new ThreadPool(1,1);
        String finalStr = str;
        threadPoolSetting.execute(()->{
            if(userService.updateProfile(UserLogined.getUserId(), usernameEditTF.getText(), nameEditTF.getText(), bioEditTF.getText(), 1, finalStr.substring(0, finalStr.length()-1)) == 1){
                new UserLogined(userService.findUserInfoById(UserLogined.getUserId()).getFirst());
                System.out.println("SETTS: Update profile complete");
                if(UserLogined.getStatus() == 0){
                    Platform.runLater(() -> {
                        try {
                            new StartUp(event, "/FXMLView/NewsFeedView.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else{
                    Platform.runLater(() -> {
                        try {
                            new StartUp(event, "/FXMLView/SettingView.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }
            }
            threadPoolSetting.stop();
        });
    }

    public void topic1Clicked(ActionEvent event) {
        changePreferredTopic(0);
    }

    public void topic2Clicked(ActionEvent event) {
        changePreferredTopic(1);
    }

    public void topic3Clicked(ActionEvent event) {
        changePreferredTopic(2);
    }

    public void topic4Clicked(ActionEvent event) {
        changePreferredTopic(3);
    }

    public void topic5Clicked(ActionEvent event) {
        changePreferredTopic(4);
    }

    public void topic6Clicked(ActionEvent event) {
        changePreferredTopic(5);
    }

    public void topic7Clicked(ActionEvent event) {
        changePreferredTopic(6);
    }

    public void topic8Clicked(ActionEvent event) {
        changePreferredTopic(7);
    }

    public void topic9Clicked(ActionEvent event) {
        changePreferredTopic(8);
    }

    public void changePreferredTopic(int topic){
        if(suggested_preferred_topic[topic] == 0){
            suggested_preferred_topic[topic] = 1;
            buttonList[topic].setId("profileSelectedButton");
        }
        else{
            suggested_preferred_topic[topic] = 0;
            buttonList[topic].setId("profileNotSelectedButton");
        }
    }



    // 2. Security and Privacy Pane

    public void changePasswordButtonClicked(ActionEvent event) {
        oldPasswordCTF.clear();
        passwordCTF.clear();
        confirmPasswordCTF.clear();
        settingChangePasswordPane.toFront();
        settingChangePasswordPane.setDisable(false);
        settingChangePasswordPane.setOpacity(1);
        settingSnPPane.setOpacity(0);
    }
    int statusSetting2FA;
    public void TFAButtonClicked(ActionEvent event) throws Exception {
        setting2FAPane.toFront();
        setting2FAPane.setOpacity(1);
        settingSnPPane.setOpacity(0);
        otp2FAPane.setOpacity(0);
        otp2FAPane.setDisable(true);
        emailTF.clear();
        otpTF.clear();
        setting2FAPane.setDisable(false);
        emailTF.setText(UserLogined.getEmail());
        UserService userService = new UserService();
        ThreadPool threadPoolSetting = new ThreadPool(1,1);
        threadPoolSetting.execute(()-> {
            statusSetting2FA = userService.fetch2FA(UserLogined.getUserId()).getFirst();
            if (statusSetting2FA == 1) {
                Platform.runLater(() -> {
                    enableButton.setText("Disable");
                    enableButton.setDisable(false);
                    status2FA.setText("ENABLE");
                    status2FA.setStyle("-fx-fill: green");
                });
            } else{
                Platform.runLater(() -> {
                    enableButton.setText("Enable");
                    enableButton.setDisable(true);
                    status2FA.setText("DISABLE");
                    status2FA.setStyle("-fx-fill: red");
                });
            }
                threadPoolSetting.stop();
            });

    }

    // Change Password Pane
    boolean passwordValidate = false;
    boolean confirmPasswordValidate = false;

    public void passwordCTyped(KeyEvent event){
        passwordValidate = false;
        if(!confirmPasswordCTF.getText().isBlank()){
            confirmPasswordCTF.clear();
            confirmPasswordCTF.setId("rejectedTFInput");
            confirmPasswordCMessage.setId("errorMessage");
            confirmPasswordCMessage.setText("Please re-enter password");
        }

        if(passwordCTF.getText().isBlank()){
            passwordCTF.setId("rejectedTFInput");
            passwordCMessage.setText("Password should not be blank");
            passwordCMessage.setId("errorMessage");
        }
        else{
            boolean length = Validation.checkPasswordLengthMin(passwordCTF.getText());
            boolean lowercase = Validation.checkPasswordContainsLowercase(passwordCTF.getText());
            boolean uppercase = Validation.checkPasswordContainsUppercase(passwordCTF.getText());
            boolean digit = Validation.checkPasswordContainsDigit(passwordCTF.getText());
            boolean specialChar = Validation.checkPasswordContainsSpecialCHar(passwordCTF.getText());

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


                passwordCTF.setId("rejectedTFInput");
                passwordCMessage.setText(errorMessage.substring(0, errorMessage.length() - 2));
                passwordCMessage.setId("errorMessage");
            }
            else{
                passwordCTF.setId("acceptedTFInput");
                passwordCMessage.setText("Password");
                passwordCMessage.setId("passMessage");
                passwordValidate = true;
            }
            unlockNextButton();

        }
    }

    public void confirmPasswordCTyped(KeyEvent event) {
        confirmPasswordValidate = false;
        if(confirmPasswordCTF.getText().isBlank()){
            confirmPasswordCTF.setId("rejectedTFInput");
            confirmPasswordCMessage.setText("Confirm password should not be blank");
            confirmPasswordCMessage.setId("errorMessage");
        }
        else if(!Validation.checkPasswordMatch(passwordCTF.getText(), confirmPasswordCTF.getText())){
            confirmPasswordCTF.setId("rejectedTFInput");
            confirmPasswordCMessage.setText("Password does not match");
            confirmPasswordCMessage.setId("errorMessage");
        }
        else{
            confirmPasswordCTF.setId("acceptedTFInput");
            confirmPasswordCMessage.setText("Confirm Password");
            confirmPasswordCMessage.setId("passMessage");
            confirmPasswordValidate = true;
        }
        unlockNextButton();
    }

    public void unlockNextButton(){
        updatePasswordButton.setDisable(!passwordValidate || !confirmPasswordValidate);
    }



    public void backChangePasswordButtonClicked(ActionEvent event) {
        settingSnPPane.toFront();
        settingChangePasswordPane.setOpacity(0);
        settingSnPPane.setOpacity(1);
        settingChangePasswordPane.setDisable(true);
    }



    public void updatePasswordButtonClicked(ActionEvent event) throws Exception {
        updatePasswordButton.setDisable(true);
        changePasswordError.setOpacity(1);
        changePasswordErrorMessage.setText("Changing password ...");
        ThreadPool threadPoolSetting = new ThreadPool(1,1);
        threadPoolSetting.execute(()-> {
            if (oldPasswordCTF.getText().equals(passwordCTF.getText())) {
                Platform.runLater(() -> {
                    changePasswordErrorMessage.setText("New password cannot be same as old password");
                    updatePasswordButton.setDisable(true);
                });
            } else if (Validation.loginUsingUsername(UserLogined.getUsername(), oldPasswordCTF.getText())) {
                Validation.updateNewPassword(passwordCTF.getText());
                    Platform.runLater(() -> {
                        try {
                            new StartUp(event, "/FXMLView/SettingView.fxml");
                            updatePasswordButton.setDisable(true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            } else {
                Platform.runLater(() -> {
                    changePasswordErrorMessage.setText("Invalid old password");
                    updatePasswordButton.setDisable(true);
                });
            threadPoolSetting.stop();
            }});
    }


    // 2.2. 2FA Pane

    public void backTFAButtonClicked(ActionEvent event) {
        settingSnPPane.toFront();
        settingSnPPane.setOpacity(1);
        setting2FAPane.setOpacity(0);
        setting2FAPane.setDisable(true);
        otp2FAPane.setOpacity(0);
        otp2FAPane.setDisable(true);

    }
    int attempt = 3;
    public void enable2FAButtonClicked(ActionEvent event) throws Exception {
        UserService userService = new UserService();
        if(statusSetting2FA == 1){
            ThreadPool threadPoolSetting = new ThreadPool(1,1);
            threadPoolSetting.execute(() -> {
                System.out.println("Checking status ...");
                boolean status = userService.update2FA(UserLogined.getUserId(), 0) == 1;
                Platform.runLater(() -> {
                    if (status) {
                        settingSnPPane.toFront();
                        settingSnPPane.setOpacity(1);
                        setting2FAPane.setOpacity(0);
                        setting2FAPane.setDisable(true);
                        otp2FAPane.setOpacity(0);
                        otp2FAPane.setDisable(true);
                    }

                });
                threadPoolSetting.stop();
            });
            return;
        }
        enableButton.setDisable(true);
        if (!(Validation.verifyOTP(Integer.parseInt(otpTF.getText())))) {
            attempt--;
            errorMessage.setText("Invalid verification code. " + attempt + " attempt(s) left");
            if (attempt == 0) {
                enableButton.setDisable(true);
                errorMessage.setText("Please try again later");
                return;
            }
            enableButton.setDisable(false);
        }else {
            ThreadPool threadPoolSetting = new ThreadPool(1,1);
            threadPoolSetting.execute(() -> {
                System.out.println("Checking status ...");
                boolean status = userService.update2FA(UserLogined.getUserId(), 1) == 1;
                Platform.runLater(() -> {
                    if (status) {
                        settingSnPPane.toFront();
                        settingSnPPane.setOpacity(1);
                        setting2FAPane.setOpacity(0);
                        setting2FAPane.setDisable(true);
                        otp2FAPane.setOpacity(0);
                        otp2FAPane.setDisable(true);
                    }

                });
                threadPoolSetting.stop();
            });
        }
    }



    public void sendOTPButtonClicked(ActionEvent event) {
        otp2FAPane.setOpacity(1);
        otp2FAPane.setDisable(false);
        sendOTPEmail(UserLogined.getEmail());
        attempt = 3;
        enableButton.setDisable(false);
    }

    public void sendOTPEmail(String email) {
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


    public void usernameEditTyped(KeyEvent event) throws Exception {
        updateProfileButton.setDisable(true);
        ThreadPool threadPoolEditUsername = new ThreadPool(1,1);
        threadPoolEditUsername.setUpdateFlag(false);
        if(usernameEditTF.getText().isBlank()){
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Username could not be blank");
        }
        else if(usernameEditTF.getText().equals(UserLogined.getUsername())){
            usernameEditTF.setId("acceptedTFInput");
            usernameEditMessage.setText("");
            usernameEditMessage.setId("passMessage");
            updateProfileButton.setDisable(false);
        }
        else if(Validation.checkUsernameContainReservedID(usernameEditTF.getText())){
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Username should not contains reserved id \"admin\"");
        }
        else if(Validation.checkUsernameContainSpace(usernameEditTF.getText())){
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Username should not contains space");
        }
        else if(Validation.checkUsernameContainSpecialChar(usernameEditTF.getText())){
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Username could contains '_' only");

        }
        else if(Validation.checkUsernameLengthMin(usernameEditTF.getText())){
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Username should not less than 8 characters");
        }
        else if(Validation.checkUsernameLengthMax(usernameEditTF.getText())){
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Username should not more than 18 characters");
        }
        else {
            threadPoolEditUsername.setUpdateFlag(true);
            usernameEditTF.setId("rejectedTFInput");
            usernameEditMessage.setId("errorMessage");
            usernameEditMessage.setText("Checking username availability ...");
            threadPoolEditUsername.execute(()->{
                System.out.println("Checking username availability ...");
                try {
                    boolean status = Validation.checkUsernameExisted(usernameEditTF.getText());
                    if(threadPoolEditUsername.getQueueSize() == 0 && threadPoolEditUsername.getUpdateFlag()) {
                        if (status) {
                            usernameEditTF.setId("rejectedTFInput");
                            usernameEditMessage.setId("errorMessage");
                            usernameEditMessage.setText("Username existed");
                        } else {
                            usernameEditTF.setId("acceptedTFInput");
                            usernameEditMessage.setText("Username");
                            usernameEditMessage.setId("passMessage");
                            updateProfileButton.setDisable(false);
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });


        }
    }
}
