package com.metadev.connect;

import javafx.event.ActionEvent;

import java.io.IOException;

public class SettingController {

    public void logoutButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "StartUpView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "NewsFeedView.fxml");
    }

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "AddPostView.fxml");
    }
}
