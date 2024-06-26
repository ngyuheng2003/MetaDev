package com.metadev.connect.FXMLController;

import com.metadev.connect.Controller.Post.PostDisplaying;
import com.metadev.connect.Controller.Post.SearchPosts;
import com.metadev.connect.Controller.StartUpController.StartUp;
import com.metadev.connect.Entity.Post;
import com.metadev.connect.Entity.User;
import com.metadev.connect.Entity.UserLogined;
import com.metadev.connect.Service.PostService;
import com.metadev.connect.ThreadPool.ThreadPool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchController {

    @FXML
    private VBox commentContainer;

    @FXML
    private VBox commentPane,searchPostContainerMiddle;

    @FXML
    private VBox loadingContainer;

    @FXML
    private VBox mainPane;

    @FXML
    private VBox middleContainer;

    @FXML
    private VBox newsFeedPane;

    @FXML
    private VBox rightContainer;

    @FXML
    private VBox searchPane;

    @FXML
    private VBox searchPostContainer;

    @FXML
    private TextField searchTF;
    @FXML private Text messageText;

    @FXML
    private VBox searchUserContainer;
    public VBox newFeedPostBox;
    private final SearchPosts search = new SearchPosts();
    private PostContainerController<SearchController> postContainerControllerComment, internal;
    private SearchController parentController;
    private final PostDisplaying<SearchController> display = new PostDisplaying<>();
    private ThreadPool threadPoolFetchPost = new ThreadPool(1, 1);;


    @FXML
    public void exitAreaClicked(MouseEvent mouseEvent) throws IOException {
        if(searchTF.getText().isBlank()){
            ActionEvent event = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
            new StartUp(event, "/FXMLView/NewsFeedView.fxml");
        }
    }

    @FXML
    public void searchInput(KeyEvent event) throws Exception {
        if(searchTF.getText().isBlank()) {
            messageText.setText("Search for user or post");
            return;
        }
        messageText.setText("Fetching search result ...");
        searchPostContainerMiddle.setDisable(true);
        searchPostContainerMiddle.setVisible(false);
        searchUserContainer.getChildren().clear();
        searchPostContainer.getChildren().clear();
        threadPoolFetchPost.setUpdateFlag(false);
        threadPoolFetchPost.execute(()-> {
            if (!searchTF.getText().isBlank()) {
                if (searchTF.getText().charAt(0) == '#') {
                    List<Post> listPost;
                    try {
                        listPost = search.searchPostByTag(searchTF.getText().substring(1));
                        threadPoolFetchPost.setUpdateFlag(true);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    List<Post> finalListPost = listPost;
                    if(threadPoolFetchPost.getQueueSize() == 0 && threadPoolFetchPost.getUpdateFlag()) {
                        Platform.runLater(() -> {
                            if (finalListPost != null)
                                displayPost(finalListPost);
                            else
                                messageText.setText("No related post");
                        });
                    }
                } else {
                    List<User> listUser;
                    List<Post> listPost;
                    try {
                        listUser = search.searchUserByUserName(searchTF.getText());
                        listPost = search.searchPostByContent(searchTF.getText());
                        threadPoolFetchPost.setUpdateFlag(true);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    List<User> finalListUser = listUser;
                    List<Post> finalListPost = listPost;
                    if(threadPoolFetchPost.getQueueSize() == 0 && threadPoolFetchPost.getUpdateFlag()) {
                        Platform.runLater(() -> {
                            if (finalListUser != null)
                                displayUser(finalListUser);
                            if (finalListPost != null) {
                                displayPost(finalListPost);
                            }
                        });
                    }
                }
            }
        });
    }

    public void displayUser(List<User> list){
        System.out.println("NEWFD: Fetching search result (User) ...");

        messageText.setText("");
        try {
            int limit;
            if(list.size() < 3)
                limit = list.size();
            else
                limit = 3;
            for(int i = 0; i < limit; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FXMLContainer/SearchUserButton.fxml"));
                HBox searchUserButton = fxmlLoader.load();
                SearchUserButtonController searchUserButtonController = fxmlLoader.getController();
                searchUserButtonController.setSearchUserButtonContainer(list.get(i));
                searchUserContainer.getChildren().add(searchUserButton);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("NEWFD: Fetching completed");
    }

    public void displayPost(List<Post> list){
        parentController = this;
        System.out.println("NEWFD: Fetching search result (Post) ...");

        messageText.setText("");
        try {
            for(int i = 0; i < list.size(); i++) {
                // Skip if post is deleted
                if(list.get(i).getStatus() == 2)
                    continue;
                // Display post
                display.displayPost(parentController, list.get(i), searchPostContainer, 1);
                searchPostContainerMiddle.setDisable(false);
                searchPostContainerMiddle.setVisible(true);
            }
            System.out.println("NEWFD: Fetching completed");
        }catch (IOException | InterruptedException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public void showCommentSection(Post post, PostContainerController<SearchController> external) throws Exception {
        System.out.println("NEWFD: Opening comment section ...");
        commentPane.toFront();
        commentPane.setDisable(false);
        mainPane.setDisable(true);
        ThreadPool threadPoolShowComment = new ThreadPool(1,1);
        threadPoolShowComment.execute(()->{
            post.updateInfo();
            Platform.runLater(() -> {
                try {
                    PostContainerController<SearchController> postContainerController = display.displayComment(parentController, post, commentContainer, external, 0);
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
        if(!postContainerControllerComment.isMouseInContainer()) {
            internal.updateParentPostContainer();
            closeCommentSection();
        }
    }
}
