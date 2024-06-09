package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.PostCreating;
import com.metadev.connect.Controller.Post.UserProfile;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {

    @FXML private AnchorPane addLocationPane, addTagsPane, emptyPane;
    @FXML private TextArea postText;
    @FXML private Text postTextCount;
    @FXML private Button usernameButton, createPostButton;
    private boolean locationPaneDisplay = false;
    private boolean tagsPaneDisplay = false;

    // Initializing the page
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameButton.setText(UserLogined.getUsername());
    }

    // Publish post
    public void postNextButtonClicked(ActionEvent event) throws Exception {
        createPostButton.setDisable(true);
        // Creating a thread for post creation
        ThreadPool threadPoolAddPost = new ThreadPool(1,1);
        PostCreating create = new PostCreating();
        // Convert user inputs into a post object
        create.convertPost(postText.getText());
        threadPoolAddPost.execute(()-> {
            System.out.println("Creating post ...");
            // Upload post to database
            boolean status = create.publishPost();
            if(status) {
                System.out.println("Post created successfully");
                Platform.runLater(() -> {
                    // Change page to newsfeed view
                    try {
                        new StartUp(event, "/FXMLView/NewsFeedView.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            threadPoolAddPost.stop();
        });

    }

    // Reset the page
    public void resetButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    // Open/Close addLocation section
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

    // Open/Close addTags section
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

    // Limiting the content in 300 words
    public void postTextKeyTyped(KeyEvent event) {
        if(postText.getText().length() > 300){
            postText.setText(postText.getText().substring(0, 300));
            postText.positionCaret(300);
        }
        postTextCount.setText(String.valueOf(300 - postText.getText().length()));
    }

    // Navigation pane
    public void profileButtonClicked(ActionEvent event) throws IOException {
        UserProfile.setUser(UserLogined.getUserLogined());
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/NewsFeedView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }
}
