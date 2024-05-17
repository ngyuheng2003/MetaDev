package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.UserProfile;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.Service.UserService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button postSectionButton, repliesSectionButton, usernameButton;
    @FXML
    private VBox commentContainer;
    @FXML private VBox vbox;
    @FXML
    private VBox profile_postContainer, mainPane, commentPane;

    @FXML
    private Text profile_username;

    private User user = UserProfile.getUser();
    private ProfileController parentController;
    private PostContainerController postContainerController;
    private PostContainerController postContainerControllerComment;
    private List<Post> listOfPost;
    private VBox newFeedPostBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profile_username.setText(user.getUsername());
        ThreadPool threadPoolFetchPost = new ThreadPool(1, 1);
        PostService postService = new PostService();
        usernameButton.setText(UserLogined.getUsername());
        parentController = this;
        try {
            threadPoolFetchPost.execute(()->{
                System.out.println("NEWFD: Fetching New Feeds ...");
                listOfPost = postService.fetchPostByUserId(user.getUserId());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //middleContainer.getChildren().remove(loadingContainer);
                        try {
                            for(int i = 0; i < listOfPost.size(); i++) {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostContainer.fxml"));
                                VBox newFeedPostBox = fxmlLoader.load();
                                postContainerController = fxmlLoader.getController();
                                postContainerController.setProfileController(parentController);
                                postContainerController.setPostContainer(listOfPost.get(i), 1);
                                profile_postContainer.getChildren().add(newFeedPostBox);
                                postContainerController.checkTypeOfPost();
                            }
                        }catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                System.out.println("NEWFD: Fetching completed");
                threadPoolFetchPost.stop();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        UserProfile.setUser(UserLogined.getUserLogined());
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }

    public void newsFeedButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/NewsFeedView.fxml");
    }

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }

    public void postSectionButtonClicked(ActionEvent event) {
        repliesSectionButton.setId("profileNotSelectedButton");
        postSectionButton.setId("profileSelectedButton");
    }

    public void repliedSectionButtonClicked(ActionEvent event) {
        repliesSectionButton.setId("profileSelectedButton");
        postSectionButton.setId("profileNotSelectedButton");
    }

    public void showCommentSection(Post post){
        System.out.println("NEWFD: Opening comment section ...");
        commentPane.toFront();
        commentPane.setDisable(false);
        mainPane.setDisable(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostContainer.fxml"));
                    newFeedPostBox = fxmlLoader.load();
                    PostContainerController postContainerController = fxmlLoader.getController();
                    postContainerControllerComment = postContainerController;
                    postContainerController.setPostContainer(post, 0);
                    postContainerController.setCommentSection(true);
                    postContainerController.setProfileController(parentController);
                    commentContainer.getChildren().add(newFeedPostBox);
                    commentContainer.setStyle("-fx-background-color:  rgb(255,255,255,0.5)");
                    System.out.println("NEWFD: Comment section opened");
                    postContainerController.displayComment();
                }catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void closeCommentSection(){
        System.out.println("POSTC: Comment section closing ...");
        mainPane.toFront();
        mainPane.setDisable(false);
        commentPane.setDisable(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                commentContainer.getChildren().remove(newFeedPostBox);
                commentContainer.setStyle("-fx-background-color: transparent");
                System.out.println("POSTC: Comment section closed");
            }
        });
    }


    public void commentOuterAreaClicked(MouseEvent mouseEvent) {
        if(!postContainerControllerComment.isMouseInContainer())
            closeCommentSection();
    }
}
