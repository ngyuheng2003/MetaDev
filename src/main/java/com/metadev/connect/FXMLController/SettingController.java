package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.StartUpController.StartUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SettingController {

    @FXML private AnchorPane settingMainPane, settingAboutPane, settingEditProfilePane, settingSnPPane;
    @FXML private AnchorPane settingChangePasswordPane, setting2FAPane, otp2FAPane;
    @FXML private TextField emailTF, otpTF;



    public void profileButtonClicked(ActionEvent event) throws IOException {
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
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }

    // Setting Main Pane

    public void editProfileButtonClicked(ActionEvent event) {
        settingEditProfilePane.toFront();
        settingMainPane.setOpacity(0);
        settingEditProfilePane.setOpacity(1);
    }

    public void securityAndPrivacyButtonClicked(ActionEvent event) {
        settingSnPPane.toFront();
        settingMainPane.setOpacity(0);
        settingSnPPane.setOpacity(1);
    }

    public void aboutButtonClicked(ActionEvent event) {
        settingAboutPane.toFront();
        settingMainPane.setOpacity(0);
        settingAboutPane.setOpacity(1);
    }

    public void logoutButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/StartUpView.fxml");
    }

    // Edit Profile Pane

    public void updateProfileButtonClicked(ActionEvent event) {
    }

    // 2. Security and Privacy Pane

    public void changePasswordButtonClicked(ActionEvent event) {
        settingChangePasswordPane.toFront();
        settingChangePasswordPane.setOpacity(1);
        settingSnPPane.setOpacity(0);
    }

    public void TFAButtonClicked(ActionEvent event) {
        setting2FAPane.toFront();
        setting2FAPane.setOpacity(1);
        settingSnPPane.setOpacity(0);
        otp2FAPane.setOpacity(0);
        otp2FAPane.setDisable(true);
        emailTF.clear();
        otpTF.clear();
    }

    // Change Password Pane

    public void backChangePasswordButtonClicked(ActionEvent event) {
        settingSnPPane.toFront();
        settingChangePasswordPane.setOpacity(0);
        settingSnPPane.setOpacity(1);
    }

    public void updatePasswordButtonClicked(ActionEvent event) {
    }


    // 2.2. 2FA Pane

    public void backTFAButtonClicked(ActionEvent event) {
        settingSnPPane.toFront();
        settingSnPPane.setOpacity(1);
        setting2FAPane.setOpacity(0);

    }

    public void enable2FAButtonClicked(ActionEvent event) {
    }



    public void sendOTPButtonClicked(ActionEvent event) {
        otp2FAPane.setOpacity(1);
        otp2FAPane.setDisable(false);
    }



}
