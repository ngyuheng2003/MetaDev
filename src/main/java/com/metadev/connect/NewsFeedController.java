package com.metadev.connect;

import javafx.event.ActionEvent;

import java.io.IOException;

public class NewsFeedController {
    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/SettingView.fxml");
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/ProfileView.fxml");
    }
}
