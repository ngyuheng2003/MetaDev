package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {
    @FXML
    private AnchorPane addLocationPane, addTagsPane, emptyPane;
    @FXML private TextArea postText;
    @FXML private Text postTextCount;
    @FXML private Button usernameButton;
    boolean locationPaneDisplay = false;
    boolean tagsPaneDisplay = false;
    private ThreadPool threadPoolAddPost;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameButton.setText(UserLogined.getUsername());
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }
    public void postNextButtonClicked(ActionEvent event) throws Exception {
        threadPoolAddPost = new ThreadPool(1,1);
        String[] word = postText.getText().split(" ");
        ArrayList<String> tags = new ArrayList();
        for(int i = 0; i < word.length; i++){
            if(word[i].charAt(0) == '#')
                tags.add(word[i].substring(1));
        }
        String[] tagging = new String[tags.size()];
        for(int i = 0; i < tags.size(); i++){
            tagging[i] = tags.get(i);
        }
        Post newPost = new Post(null,UserLogined.getUserId(), UserLogined.getUsername(), 0,postText.getText(), tagging, null,0,  0,null);
        UserLogined.setNewPostFlag(true);
        UserLogined.setNewPost(newPost);
        threadPoolAddPost.execute(()-> {
            System.out.println("Creating post ...");
            PostService postService = new PostService();
            boolean status = postService.createPost(newPost) == 1;
            if(status) {
                System.out.println("Post created successfully");
                threadPoolAddPost.stop();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new StartUp(event, "/FXMLView/NewsFeedView.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });
            }

        });
    }

    public void resetButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/NewsFeedView.fxml");
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
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }


}
