package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.ContentRecommendationSystem;
import com.metadev.connect.Controller.Post.PostDisplaying;
import com.metadev.connect.Controller.Post.UserProfile;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NewsFeedController implements Initializable {

    // FXML Components
    @FXML private VBox newsFeedPane, searchPane, newFeedPostContainer,loadingContainer, middleContainer;
    @FXML private VBox rightContainer, commentContainer, commentPane, mainPane, loadingPane;
    @FXML private TextField searchTF;
    @FXML private Button usernameButton;
    @FXML private List<Post> listOfPost;

    // Instance variables
    public VBox newFeedPostBox;
    private PostContainerController<NewsFeedController> postContainerControllerComment, internal;
    private final PostDisplaying<NewsFeedController> display = new PostDisplaying<>();
    private NewsFeedController parentController;

    // Initializing the page (Displaying post)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ThreadPool threadPoolFetchPost = new ThreadPool(1, 1);
        usernameButton.setText(UserLogined.getUsername());
        parentController = this;
        try {
            threadPoolFetchPost.execute(()->{
                System.out.println("NEWFD: Fetching New Feeds ...");
                listOfPost = display.fetchPost();
                // Check whether a new post is created by the user
                if(UserLogined.getNewPost() != null) {
                    Post post = UserLogined.getNewPost();
                    post.setPostCreatedDate("just now");
                    listOfPost.addFirst(UserLogined.getNewPost());
                    UserLogined.setNewPost(null);
                }
                // Display post
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        middleContainer.getChildren().remove(loadingContainer);
                        try {
                            for(int i = 0; i < listOfPost.size(); i++) {
                                // Skip if post is deleted
                                if(listOfPost.get(i).getStatus() == 2)
                                    continue;
                                // Display post
                                display.displayPost(parentController, listOfPost.get(i), newFeedPostContainer, 1);
                            }
                        }catch (IOException | InterruptedException | ClassNotFoundException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                System.out.println("NEWFD: Fetching completed");
                ContentRecommendationSystem sys = new ContentRecommendationSystem();
                try {
                    sys.updatePreferred();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                threadPoolFetchPost.stop();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Show comment section
    public void showCommentSection(Post post, PostContainerController<NewsFeedController> external) throws Exception {
        commentPane.toFront();
        commentPane.setDisable(false);
        mainPane.setDisable(true);
        ThreadPool threadPoolShowComment = new ThreadPool(1,1);
        // Show the comments of the post
        threadPoolShowComment.execute(()->{
            System.out.println("NEWFD: Opening comment section ...");
            post.updateInfo();
            Platform.runLater(() -> {
                try {
                    PostContainerController<NewsFeedController> postContainerController = display.displayComment(parentController, post, commentContainer, external, 0);
                    internal = postContainerController;
                    postContainerControllerComment = postContainerController;
                    System.out.println("NEWFD: Comment section opened");
                }catch (IOException | InterruptedException | SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            threadPoolShowComment.stop();
        });
    }

    // Close the comment section
    public void closeCommentSection(){
        System.out.println("POSTC: Comment section closing ...");
        mainPane.toFront();
        mainPane.setDisable(false);
        commentPane.setDisable(true);
        Platform.runLater(() -> {
            commentContainer.getChildren().remove(newFeedPostBox);
            commentContainer.setStyle("-fx-background-color: transparent");
            System.out.println("POSTC: Comment section closed");
        });
    }

    // Exit from comment section by clicking outside the post container
    public void commentOuterAreaClicked(MouseEvent mouseEvent) {
        if(!postContainerControllerComment.isMouseInContainer()) {
            internal.updateParentPostContainer();
            closeCommentSection();
        }
    }

    // Transition when loading profile page
    public void showLoadingPane(){
        loadingPane.setVisible(true);
        loadingPane.toFront();
    }

    // Navigation Pane
    public void searchTFClicked(MouseEvent mouseEvent) throws IOException {
        ActionEvent event = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
        new StartUp(event, "/FXMLView/SearchView.fxml");
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        UserProfile.setUser(UserLogined.getUserLogined());
        new StartUp(event, "/FXMLView/ProfileView.fxml");
    }

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }
}
