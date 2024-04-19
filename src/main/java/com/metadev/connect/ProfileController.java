package com.metadev.connect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProfileController {
    @FXML
    private Button postSectionButton, repliesSectionButton;
    @FXML private VBox vbox;

    public void profileButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/ProfileView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/NewsFeedView.fxml");
    }

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/SettingView.fxml");
    }

    public void postSectionButtonClicked(ActionEvent event) {
        repliesSectionButton.setId("profileNotSelectedButton");
        postSectionButton.setId("profileSelectedButton");
    }

    public void repliedSectionButtonClicked(ActionEvent event) {
        repliesSectionButton.setId("profileSelectedButton");
        postSectionButton.setId("profileNotSelectedButton");
    }



}
