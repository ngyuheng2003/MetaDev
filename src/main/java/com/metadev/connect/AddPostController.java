package com.metadev.connect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class AddPostController {
    @FXML
    private AnchorPane addLocationPane, addTagsPane, emptyPane;
    @FXML private TextArea postText;
    @FXML private Text postTextCount;
    boolean locationPaneDisplay = false;
    boolean tagsPaneDisplay = false;
    public void postNextButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/NewsFeedView.fxml");
    }

    public void resetButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/AddPostView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/NewsFeedView.fxml");
    }

    public void addLocationButtonClicked(ActionEvent event) {
        if(!locationPaneDisplay){
            addLocationPane.toFront();
            addLocationPane.setOpacity(1);
            addTagsPane.setOpacity(0);
            tagsPaneDisplay = false;
            locationPaneDisplay = true;
        }
        else{
            emptyPane.toFront();
            addLocationPane.setOpacity(0);
            locationPaneDisplay = false;
        }
    }

    public void addTagsButtonClicked(ActionEvent event) {
        if(!tagsPaneDisplay){
            addTagsPane.toFront();
            addTagsPane.setOpacity(1);
            addLocationPane.setOpacity(0);
            locationPaneDisplay = false;
            tagsPaneDisplay = true;
        }
        else{
            emptyPane.toFront();
            addTagsPane.setOpacity(0);
            tagsPaneDisplay = false;
        }
    }

    public void postTextKeyTyped(KeyEvent event) {
        if(postText.getText().length() > 300){
            postText.setText(postText.getText().substring(0, 300));
            postText.positionCaret(300);
        }
        postTextCount.setText(String.valueOf(300 - postText.getText().length()));
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/SettingView.fxml");
    }
}
