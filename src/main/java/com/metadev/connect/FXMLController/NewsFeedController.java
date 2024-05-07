package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.StartUp;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.springframework.security.core.parameters.P;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewsFeedController implements Initializable {

    @FXML private VBox newsFeedPane, searchPane, newFeedPostContainer,loadingContainer, middleContainer;
    @FXML private TextField searchTF;
    @FXML private Button usernameButton;

    private List<Post> listOfPost;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ThreadPool threadPoolFetchPost = new ThreadPool(1, 1);
        PostService postService = new PostService();
        usernameButton.setText(UserLogined.getUsername());
        try {
            threadPoolFetchPost.execute(()->{
                System.out.println("NEWFD: Fetching New Feeds ...");
                listOfPost = postService.fetchPost();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        middleContainer.getChildren().remove(loadingContainer);
                        try {
                            for(int i = 0; i < listOfPost.size(); i++) {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("/postContainer.fxml"));
                                VBox newFeedPostBox = fxmlLoader.load();
                                PostContainerController postContainerController = fxmlLoader.getController();
                                postContainerController.setPostContainer(listOfPost.get(i));
                                newFeedPostContainer.getChildren().add(newFeedPostBox);
                            }
                        }catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                System.out.println("NEWFD: Fetching completed ...");
                threadPoolFetchPost.stop();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
