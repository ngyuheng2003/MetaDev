package com.metadev.connect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class NewsFeedController {

    @FXML private VBox newsFeedPane, searchPane;
    @FXML private TextField searchTF;
    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/SettingView.fxml");
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/ProfileView.fxml");
    }

    public void searchInput(KeyEvent event) {
        if(searchTF.getText().isBlank()){
            newsFeedPane.toFront();
            newsFeedPane.setOpacity(1);
            searchPane.setOpacity(0);
        }
        else{
            searchPane.toFront();
            searchPane.setOpacity(1);
            newsFeedPane.setOpacity(0);
        }
    }
}
