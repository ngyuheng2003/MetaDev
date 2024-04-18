package com.metadev.connect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    @FXML private TextField usernameSU, emailSU, passwordSU, conPasswordSU;
    @FXML private Hyperlink signUpHyperLink;

    private static boolean startup = true;

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

    public void signUpButtonClicked(ActionEvent event) {
        signUpPane.toFront();
        signUpHyperLink.setVisited(false);
        animation.fade(loginPane, 0.5, 0, 1, 0);
        animation.fade(signUpPane, 0.5, 0.5, 0, 1);
        newSignUpForm();
    }

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

    public void newSignUpForm(){
        usernameSU.clear();
        emailSU.clear();
        passwordSU.clear();
        conPasswordSU.clear();
    }


    public void loginButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "NewsFeedView.fxml");
    }
}
