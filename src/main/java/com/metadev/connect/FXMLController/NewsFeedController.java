package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.Post;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NewsFeedController implements Initializable {


    @FXML private VBox newsFeedPane, searchPane, newFeedPostContainer,loadingContainer, middleContainer;
    @FXML private VBox rightContainer, commentContainer, commentPane, mainPane;
    @FXML private TextField searchTF;
    @FXML private Button usernameButton;
    @FXML private List<Post> listOfPost;
    @FXML PostContainerController postContainerController;

    private VBox newFeedPostBox;
    private PostContainerController postContainerControllerComment;
    private NewsFeedController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ThreadPool threadPoolFetchPost = new ThreadPool(1, 1);
        PostService postService = new PostService();
        usernameButton.setText(UserLogined.getUsername());
        parentController = this;
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
                                fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/PostContainer.fxml"));
                                VBox newFeedPostBox = fxmlLoader.load();
                                postContainerController = fxmlLoader.getController();
                                postContainerController.setParentController(parentController);
                                postContainerController.setPostContainer(listOfPost.get(i), 1);
                                newFeedPostContainer.getChildren().add(newFeedPostBox);
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

    public void addPostButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/AddPostView.fxml");
    }

    public void settingButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/SettingView.fxml");
    }

    public void profileButtonClicked(ActionEvent event) throws IOException {
        new StartUp(event, "/FXMLView/ProfileView.fxml");
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
                    postContainerController.setParentController(parentController);
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
